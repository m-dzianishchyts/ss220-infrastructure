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
        return gameConfig.getServers().stream()
                .filter(GameServerData::active)
                .map(s -> new Command.Choice(s.fullName(), s.ip().getHostAddress() + ":" + s.port()))
                .toList();
    }

    @Nullable
    @Override
    public ActiveGameServerData resolve(@NotNull SlashCommandOption option,
                                  @NotNull CommandInteractionPayload event,
                                  @NotNull OptionMapping optionMapping) {
        String[] address = optionMapping.getAsString().split(":");
        String host = address[0];
        int port = Integer.parseInt(address[1]);
        return new ActiveGameServerData(gameConfig.getServerByAddress(host, port).orElseThrow());
    }
}
