package club.ss220.manager;

import club.ss220.manager.config.ManagerApplicationConfig;
import club.ss220.manager.shared.presentation.Commands;
import io.github.freya022.botcommands.api.core.JDAService;
import io.github.freya022.botcommands.api.core.config.JDAConfiguration;
import io.github.freya022.botcommands.api.core.events.BReadyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = "club.ss220")
public class ManagerApplication extends JDAService {

    private final JDAConfiguration jdaConfig;
    private final ManagerApplicationConfig appConfig;
    private final ResourceLoader resourceLoader;
    private final Commands commands;

    @NotNull
    @Override
    public Set<CacheFlag> getCacheFlags() {
        return jdaConfig.getCacheFlags();
    }

    @NotNull
    @Override
    public Set<GatewayIntent> getIntents() {
        return jdaConfig.getIntents();
    }

    @Override
    protected void createJDA(@NotNull BReadyEvent bReadyEvent, @NotNull IEventManager iEventManager) {
        createDefault(appConfig.getToken())
                .setActivity(Optional.ofNullable(appConfig.getStatus()).map(Activity::customStatus).orElse(null))
                .addEventListeners(new OnReadyListener())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }

    private class OnReadyListener extends ListenerAdapter {

        @Override
        public void onReady(@NotNull ReadyEvent event) {
            JDA jda = event.getJDA();

            if (appConfig.getProfile().autoUpdate()) {
                String profileName = appConfig.getProfile().name();
                Icon profileAvatar = loadIcon(appConfig.getProfile().avatar());
                jda.getSelfUser().getManager().setName(profileName).setAvatar(profileAvatar).queue(
                        _ -> log.info("Bot profile updated"),
                        e -> log.error("Error updating bot profile", e)
                );
                jda.getGuilds().forEach(guild -> guild.modifyNickname(guild.getSelfMember(), profileName).queue(
                        _ -> log.info("Bot nickname updated for guild {}", guild.getIdLong()),
                        e -> log.error("Error updating bot nickname for guild {}", guild.getIdLong(), e)
                ));
            }

            long globalCommands = jda.retrieveCommands().complete().stream()
                    .mapToLong(c -> commands.flattenCommandInfo(c).size())
                    .sum();
            long guildCommands = jda.getGuilds().stream()
                    .map(guild -> guild.retrieveCommands().complete())
                    .mapToLong(cmds -> cmds.stream().mapToLong(c -> commands.flattenCommandInfo(c).size()).sum())
                    .max().orElse(0);
            log.info("Bot started with {} global commands and {} guild commands", globalCommands, guildCommands);
        }

        private Icon loadIcon(String path) {
            try (InputStream inputStream = resourceLoader.getResource(path).getInputStream()) {
                return Icon.from(inputStream);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}
