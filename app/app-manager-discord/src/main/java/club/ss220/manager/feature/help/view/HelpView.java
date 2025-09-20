package club.ss220.manager.feature.help.view;

import club.ss220.manager.shared.CommandInfo;
import club.ss220.manager.shared.presentation.BasicView;
import club.ss220.manager.shared.presentation.Embeds;
import club.ss220.manager.shared.presentation.Formatters;
import club.ss220.manager.shared.presentation.UiConstants;
import dev.freya02.jda.emojis.unicode.Emojis;
import jakarta.annotation.Nullable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class HelpView extends BasicView {

    protected HelpView(Embeds embeds, Formatters formatters) {
        super(embeds, formatters);
    }

    public MessageEmbed renderHelpList(Map<Command.Type, List<CommandInfo>> commands) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Доступные команды");
        String description = Stream.of(
                slashCommandsBlock(commands.get(Command.Type.SLASH)),
                userCommandsBlock(commands.get(Command.Type.USER)),
                messageCommandsBlock(commands.get(Command.Type.MESSAGE))
        ).filter(Objects::nonNull).collect(Collectors.joining("\n\n"));

        embed.setDescription(description.isBlank() ? "Нет доступных команд." : description);
        embed.setColor(UiConstants.COLOR_INFO);
        return embed.build();
    }

    public MessageEmbed renderCommandDetails(CommandInfo command) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("/" + command.name());
        embed.setDescription(command.description());

        List<Command.Option> options = command.options();
        List<Command.Option> requiredOptions = options.stream().filter(Command.Option::isRequired).toList();
        List<Command.Option> optionalOptions = ListUtils.subtract(options, requiredOptions);
        if (!requiredOptions.isEmpty()) {
            embed.addField("Обязательные параметры", formatOptions(requiredOptions), false);
        }
        if (!optionalOptions.isEmpty()) {
            embed.addField("Опциональные параметры", formatOptions(optionalOptions), false);
        }
        embed.setColor(UiConstants.COLOR_INFO);
        return embed.build();
    }

    public MessageEmbed renderCommandNotFound(String name) {
        return embeds.error("Команда /" + name + " не существует или недоступна.");
    }

    @Nullable
    private String slashCommandsBlock(@Nullable List<CommandInfo> commands) {
        if (commands == null || commands.isEmpty()) {
            return null;
        }

        return Emojis.KEYBOARD.getFormatted() + " **Текстовые команды**\n" + commands.stream()
                .map(info -> info.mention() + " — " + info.description())
                .sorted()
                .collect(Collectors.joining("\n"));
    }

    @Nullable
    private String userCommandsBlock(@Nullable List<CommandInfo> commands) {
        if (commands == null || commands.isEmpty()) {
            return null;
        }
        return Emojis.MOUSE_THREE_BUTTON.getFormatted() + " " + Emojis.BUST_IN_SILHOUETTE.getFormatted()
               + " **Контекстные команды (пользователь)**\n" + commands.stream()
                       .map(e -> "`" + e.name() + "`")
                       .sorted()
                       .collect(Collectors.joining("\n"));
    }

    @Nullable
    private String messageCommandsBlock(@Nullable List<CommandInfo> commands) {
        if (commands == null || commands.isEmpty()) {
            return null;
        }
        return Emojis.MOUSE_THREE_BUTTON.getFormatted() + " " + Emojis.MEMO.getFormatted()
               + " **Контекстные команды (сообщение)**\n" + commands.stream()
                       .map(e -> "`" + e.name() + "`")
                       .sorted()
                       .collect(Collectors.joining("\n"));
    }

    private String formatOptions(List<Command.Option> options) {
        return options.stream()
                .map(o -> "`" + o.getName() + "` — " + o.getDescription())
                .collect(Collectors.joining("\n"));
    }
}
