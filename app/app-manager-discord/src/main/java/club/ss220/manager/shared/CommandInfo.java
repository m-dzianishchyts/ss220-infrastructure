package club.ss220.manager.shared;

import jakarta.annotation.Nullable;
import lombok.Builder;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.List;

@Builder
public record CommandInfo(String name, String description, List<Command.Option> options, @Nullable String mention) {

    public static CommandInfo from(Command command) {
        return CommandInfo.builder()
                .name(command.getName())
                .description(command.getDescription())
                .options(command.getOptions())
                .mention(command.getType() == Command.Type.SLASH ? command.getAsMention() : null)
                .build();
    }

    public static CommandInfo from(Command.Subcommand subcommand) {
        return CommandInfo.builder()
                .name(subcommand.getName())
                .description(subcommand.getDescription())
                .options(subcommand.getOptions())
                .mention(subcommand.getAsMention())
                .build();
    }
}
