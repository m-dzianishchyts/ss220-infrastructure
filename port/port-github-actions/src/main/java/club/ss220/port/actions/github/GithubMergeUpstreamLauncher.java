package club.ss220.port.actions.github;

import club.ss220.core.shared.WorkflowRunStatus;
import club.ss220.core.spi.MergeUpstreamLauncher;
import club.ss220.port.actions.github.config.MergeWorkflowConfig;
import club.ss220.port.actions.github.mapper.WorkflowRunMapper;
import club.ss220.port.actions.github.util.GitHubUtils;
import club.ss220.port.actions.github.util.JwtUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHApp;
import org.kohsuke.github.GHAppInstallationToken;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHWorkflow;
import org.kohsuke.github.GHWorkflowRun;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
public class GithubMergeUpstreamLauncher implements MergeUpstreamLauncher {

    private static final Duration POLLING_DELAY = Duration.ofSeconds(10);
    private static final Duration JWT_LIFETIME = Duration.ofMinutes(10);

    private final MergeWorkflowConfig workflowConfig;
    private final WorkflowRunMapper workflowRunMapper;

    @Override
    @SneakyThrows
    public CompletableFuture<WorkflowRunStatus> launch(boolean translateChangelog) {
        log.debug("Dispatching workflow run, config: {}, translateCL: {}", workflowConfig, translateChangelog);

        GitHub github = github();
        GHRepository repository = repository(github);
        GHWorkflow workflow = workflow(repository);
        workflow.dispatch(repository.getDefaultBranch(), Map.of("translate", translateChangelog));
        return CompletableFuture.supplyAsync(() -> StreamSupport.stream(workflow.listRuns().spliterator(), false)
                        .filter(run -> run.getWorkflowId() == workflow.getId())
                        .findFirst(), CompletableFuture.delayedExecutor(5, TimeUnit.SECONDS))
                .thenApply(run -> run.map(workflowRunMapper::toWorkflowRunStatus).orElseThrow());
    }

    @Override
    @SneakyThrows
    public CompletableFuture<WorkflowRunStatus> awaitConclusion(long workflowRunId) {
        log.debug("Awaiting workflow run #{} conclusion", workflowRunId);

        GitHub github = github();
        GHRepository repository = repository(github);
        AtomicReference<WorkflowRunStatus> lastStatus = new AtomicReference<>();

        Duration timeout = workflowConfig.timeout();
        long pollingAttempts = timeout.dividedBy(POLLING_DELAY);
        return Mono.defer(() -> Mono.just(fetchWorkflowRun(repository, workflowRunId, lastStatus)))
                .retryWhen(Retry.backoff(pollingAttempts, POLLING_DELAY)
                                   .maxBackoff(POLLING_DELAY)
                                   .doBeforeRetry(retry -> {
                                       long attempt = retry.totalRetries() + 1;
                                       log.trace("Polling workflow run #{} (attempt {})", workflowRunId, attempt);
                                   }))
                .timeout(timeout)
                .onErrorResume(TimeoutException.class, e -> {
                    WorkflowRunStatus lastRunStatus = lastStatus.get();
                    if (lastRunStatus != null) {
                        log.warn("Timeout after {} polling workflow run, status: {}", timeout, lastRunStatus);
                        return Mono.just(lastRunStatus);
                    } else {
                        return Mono.error(e);
                    }
                })
                .toFuture();
    }

    private @NotNull WorkflowRunStatus fetchWorkflowRun(GHRepository repository, long workflowRunId,
                                                        AtomicReference<WorkflowRunStatus> lastStatus) {
        GHWorkflowRun workflowRun = getWorkflowRun(repository, workflowRunId);
        WorkflowRunStatus workflowRunStatus = workflowRunMapper.toWorkflowRunStatus(workflowRun);
        lastStatus.set(workflowRunStatus);

        if (!GitHubUtils.isWorkflowCompleted(workflowRun)) {
            throw new IllegalStateException("Workflow run is not completed");
        }
        return workflowRunStatus;
    }

    @SneakyThrows
    private GitHub github() {
        String applicationId = workflowConfig.applicationId();
        Path keyPath = workflowConfig.privateKeySource();
        String ownerName = workflowConfig.repoOwner();
        String repoName = workflowConfig.repoName();

        String jwt = JwtUtils.createJwt(applicationId, keyPath, JWT_LIFETIME);
        GHApp gitHub = new GitHubBuilder().withJwtToken(jwt).build().getApp();
        GHAppInstallationToken token = gitHub.getInstallationByRepository(ownerName, repoName).createToken().create();
        return new GitHubBuilder().withAppInstallationToken(token.getToken()).build();
    }

    @SneakyThrows
    private GHRepository repository(GitHub gitHub) {
        return gitHub.getRepository(workflowConfig.repoOwner() + "/" + workflowConfig.repoName());
    }

    @SneakyThrows
    private GHWorkflow workflow(GHRepository repo) {
        return repo.getWorkflow(workflowConfig.workflow());
    }

    @SneakyThrows
    private static GHWorkflowRun getWorkflowRun(GHRepository repository, long workflowRunId) {
        return repository.getWorkflowRun(workflowRunId);
    }
}
