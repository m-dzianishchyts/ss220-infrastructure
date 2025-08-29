package club.ss220.manager.infrastructure.logging.slash;

import io.github.freya022.botcommands.api.core.annotations.BEventListener;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SlashCommandListener {

    @BEventListener
    public void onSlashCommand(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        String command = event.getFullCommandName();
        List<String> options = event.getOptions().stream().map(o -> o.getName() + "=" + o.getAsString()).toList();
        log.info("User {} executed command /{}", user.getName(), command);
        log.debug("Executing command /{} with options: {}", command, options);
    }
}
