package club.ss220.manager.infrastructure.discord;

import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.GameServerData;
import club.ss220.manager.shared.ActiveGameServerData;
import io.github.freya022.botcommands.api.commands.application.slash.options.SlashCommandOption;
import io.github.freya022.botcommands.api.core.service.annotations.Resolver;
import io.github.freya022.botcommands.api.parameters.ClassParameterResolver;
import io.github.freya022.botcommands.api.parameters.resolvers.SlashParameterResolver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

@Resolver
public class ActiveGameServerResolver
        extends ClassParameterResolver<ActiveGameServerResolver, ActiveGameServerData>
        implements SlashParameterResolver<ActiveGameServerResolver, ActiveGameServerData> {

    private final GameConfig gameConfig;

    public ActiveGameServerResolver(GameConfig gameConfig) {
        super(ActiveGameServerData.class);
        this.gameConfig = gameConfig;
    }

    @NotNull
    @Override
    public OptionType getOptionType() {
        return OptionType.STRING;
    }

    @NotNull
    @Override
    public Collection<Command.Choice> getPredefinedChoices(@Nullable Guild guild) {
        List<Command.Choice> choices = gameConfig.getSupportedServers().stream()
                .filter(GameServerData::active)
                .map(s -> new Command.Choice(s.fullName(), s.id()))
                .toList();
        if (choices.isEmpty()) {
            // Only for development purposes
            return List.of(new Command.Choice("<STUB>No active servers found</STUB>", ""));
        }
        return choices;
    }

    @Nullable
    @Override
    public ActiveGameServerData resolve(@NotNull SlashCommandOption option,
                                  @NotNull CommandInteractionPayload event,
                                  @NotNull OptionMapping optionMapping) {
        return new ActiveGameServerData(gameConfig.getServerById(optionMapping.getAsString()).orElseThrow());
    }
}
