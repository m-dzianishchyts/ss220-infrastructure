package club.ss220.manager.infrastructure.discord;

import club.ss220.manager.config.GameDiscordConfig;
import club.ss220.manager.shared.GameServerType;
import io.github.freya022.botcommands.api.commands.application.slash.options.SlashCommandOption;
import io.github.freya022.botcommands.api.core.service.annotations.Resolver;
import io.github.freya022.botcommands.api.parameters.ClassParameterResolver;
import io.github.freya022.botcommands.api.parameters.resolvers.SlashParameterResolver;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Collection;

@Slf4j
@Resolver
public class WhitelistTypeResolver
        extends ClassParameterResolver<WhitelistTypeResolver, GameServerType>
        implements SlashParameterResolver<WhitelistTypeResolver, GameServerType> {

    private final GameDiscordConfig gameDiscordConfig;

    public WhitelistTypeResolver(GameDiscordConfig gameDiscordConfig) {
        super(GameServerType.class);
        this.gameDiscordConfig = gameDiscordConfig;
    }

    @Override
    public @NotNull OptionType getOptionType() {
        return OptionType.STRING;
    }

    @Override
    public @NotNull Collection<Command.Choice> getPredefinedChoices(@Nullable Guild guild) {
        return gameDiscordConfig.getServerTypes().stream()
                .map(t -> new Command.Choice(t.name(), t.name()))
                .toList();
    }

    @Nullable
    @Override
    public GameServerType resolve(@NotNull SlashCommandOption option,
                                  @NotNull CommandInteractionPayload event,
                                  @NotNull OptionMapping optionMapping) {
        return gameDiscordConfig.getGameServerTypeByName(optionMapping.getAsString()).orElseThrow();
    }
}
