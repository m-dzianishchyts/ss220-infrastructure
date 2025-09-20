package club.ss220.manager.feature.help.controller;

import club.ss220.manager.feature.help.view.HelpView;
import club.ss220.manager.shared.CommandInfo;
import club.ss220.manager.shared.application.CommandService;
import club.ss220.manager.shared.presentation.Commands;
import club.ss220.manager.shared.presentation.Senders;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class HelpController {

    private final HelpView helpView;
    private final CommandService commandService;
    private final Commands commands;
    private final Senders senders;

    public void showHelp(IReplyCallback interaction, @Nullable String commandName) {
        interaction.deferReply().setEphemeral(true).queue();

        Guild guild = interaction.getGuild();
        User user = interaction.getUser();
        Map<Command.Type, List<CommandInfo>> availableCommands = getAvailableCommands(guild, user);

        if (commandName == null || commandName.isBlank()) {
            senders.sendEmbed(interaction, helpView.renderHelpList(availableCommands));
            log.debug("Displayed help ({} commands)", availableCommands.values().stream().mapToLong(List::size).sum());
            return;
        }

        Optional<CommandInfo> targetCommand = availableCommands.values().stream()
                .flatMap(List::stream)
                .filter(command -> commandName.equalsIgnoreCase(command.name()))
                .findAny();

        if (targetCommand.isEmpty()) {
            senders.sendEmbed(interaction, helpView.renderCommandNotFound(commandName));
            return;
        }

        CommandInfo command = targetCommand.get();
        senders.sendEmbed(interaction, helpView.renderCommandDetails(command));
        log.debug("Displayed help for command /{}", command.name());
    }

    private Map<Command.Type, List<CommandInfo>> getAvailableCommands(Guild guild, User user) {
        return commandService.getAvailableCommands(guild, user).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream()
                                .map(commands::flattenCommandInfo)
                                .flatMap(List::stream)
                                .toList())
                );
    }
}
