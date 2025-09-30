package club.ss220.port.redis.listener;

import club.ss220.core.shared.IngamePost;
import club.ss220.core.shared.event.NewsPublishedEvent;
import club.ss220.port.redis.config.IngameNewsRedisConfig;
import club.ss220.port.redis.mapper.IngamePostMapper;
import club.ss220.port.redis.presentation.RedisIngamePost;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class IngameNewsRedisListener {

    private final IngameNewsRedisConfig ingameNewsConfig;
    private final IngamePostMapper mapper;
    private final ApplicationEventPublisher eventPublisher;

    @SuppressWarnings("unused")
    public void onNewsPublished(RedisIngamePost redisIngamePost) {
        Duration publishDelay = Optional.ofNullable(ingameNewsConfig.getPublishDelay()).orElse(Duration.ofSeconds(0));
        Executor executor = CompletableFuture.delayedExecutor(publishDelay.toMillis(), TimeUnit.MILLISECONDS);
        try {
            IngamePost ingamePost = mapper.toIngamePost(redisIngamePost);
            CompletableFuture.runAsync(() -> handleNews(ingamePost), executor);
        } catch (IOException e) {
            log.error("Error mapping Redis news message", e);
        }
    }

    private void handleNews(IngamePost ingamePost) {
        try {
            eventPublisher.publishEvent(new NewsPublishedEvent(ingamePost));
        } catch (Exception e) {
            log.error("Error handling Redis news message", e);
        }
    }
}
