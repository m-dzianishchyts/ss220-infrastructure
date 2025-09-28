package club.ss220.manager.feature.merge.view;

import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.WorkflowRunStatus;
import club.ss220.manager.shared.presentation.GameBuildStyle;
import club.ss220.manager.shared.presentation.UiConstants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MergeView {

    public MessageEmbed renderDispatchSuccess(GameBuild build, WorkflowRunStatus workflowRun) {
        GameBuildStyle buildStyle = GameBuildStyle.fromName(build.getName());
        return new EmbedBuilder()
                .setTitle("Мерж апстрима")
                .setDescription("Инициирована новая операция.")
                .addField("Билд", buildStyle.getEmoji().getFormatted() + " " + build.getName(), true)
                .addField("Статус", Optional.ofNullable(workflowRun.status()).orElse("N/A"), true)
                .addField("Workflow Run", "[#" + workflowRun.id() + "](" + workflowRun.htmlUrl() + ")", true)
                .setFooter("По завершении работы будет отправлено уведомление.")
                .setColor(UiConstants.COLOR_INFO)
                .build();
    }

    public MessageEmbed renderSuccess(GameBuild build, WorkflowRunStatus workflowRun) {
        if (workflowRun.success() == null) {
            throw new IllegalArgumentException("Workflow run is completed but it's status is null");
        }

        GameBuildStyle buildStyle = GameBuildStyle.fromName(build.getName());
        return new EmbedBuilder()
                .setTitle("Мерж апстрима")
                .setDescription("Операция успешно завершена.")
                .addField("Билд", buildStyle.getEmoji().getFormatted() + " " + build.getName(), true)
                .addField("Результат", Optional.ofNullable(workflowRun.conclusion()).orElse("N/A"), true)
                .addField("Workflow Run", "[#" + workflowRun.id() + "](" + workflowRun.htmlUrl() + ")", true)
                .setColor(UiConstants.COLOR_SUCCESS)
                .build();
    }

    public MessageEmbed renderFail(GameBuild build, WorkflowRunStatus workflowRun) {
        if (workflowRun.success() == null) {
            throw new IllegalArgumentException("Workflow run is completed but it's status is null");
        }

        GameBuildStyle buildStyle = GameBuildStyle.fromName(build.getName());
        return new EmbedBuilder()
                .setTitle("Мерж апстрима")
                .setDescription("Операция завершилась неудачно.")
                .addField("Билд", buildStyle.getEmoji().getFormatted() + " " + build.getName(), true)
                .addField("Результат", Optional.ofNullable(workflowRun.conclusion()).orElse("N/A"), true)
                .addField("Workflow Run", "[#" + workflowRun.id() + "](" + workflowRun.htmlUrl() + ")", true)
                .setColor(UiConstants.COLOR_ERROR)
                .build();
    }

    public MessageEmbed renderTimeout(GameBuild build, WorkflowRunStatus workflowRun) {
        GameBuildStyle buildStyle = GameBuildStyle.fromName(build.getName());
        return new EmbedBuilder()
                .setTitle("Мерж апстрима")
                .setDescription("Превышено время ожидания выполнения операции.")
                .addField("Билд", buildStyle.getEmoji().getFormatted() + " " + build.getName(), true)
                .addField("Статус", Optional.ofNullable(workflowRun.status()).orElse("N/A"), true)
                .addField("Workflow Run", "[#" + workflowRun.id() + "](" + workflowRun.htmlUrl() + ")", true)
                .setColor(UiConstants.COLOR_ERROR)
                .build();
    }

    public MessageEmbed renderError(GameBuild build, Throwable error) {
        GameBuildStyle buildStyle = GameBuildStyle.fromName(build.getName());
        return new EmbedBuilder()
                .setTitle("Мерж апстрима")
                .setDescription("Произошла ошибка при отслеживании операции.")
                .addField("Билд", buildStyle.getEmoji().getFormatted() + " " + build.getName(), true)
                .addField("Ошибка", error.getClass().getSimpleName(), true)
                .setColor(UiConstants.COLOR_ERROR)
                .build();
    }
}
