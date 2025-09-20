package club.ss220.manager.shared.presentation;

import club.ss220.manager.shared.CommandInfo;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class Commands {

    public List<CommandInfo> flattenCommandInfo(Command command) {
        List<Command.Subcommand> subcommands = Stream.concat(
                command.getSubcommands().stream(),
                command.getSubcommandGroups().stream().flatMap(group -> group.getSubcommands().stream())
        ).toList();

        return subcommands.isEmpty()
                ? List.of(CommandInfo.from(command))
                : subcommands.stream().map(CommandInfo::from).toList();
    }
}
