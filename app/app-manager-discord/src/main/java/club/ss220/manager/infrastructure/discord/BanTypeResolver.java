package club.ss220.manager.infrastructure.discord;

import club.ss220.core.shared.BanData;
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

import java.util.Arrays;
import java.util.Collection;

@Resolver
public class BanTypeResolver
        extends ClassParameterResolver<BanTypeResolver, BanData.BanType>
        implements SlashParameterResolver<BanTypeResolver, BanData.BanType> {

    public BanTypeResolver() {
        super(BanData.BanType.class);
    }

    @NotNull
    @Override
    public OptionType getOptionType() {
        return OptionType.STRING;
    }

    @NotNull
    @Override
    public Collection<Command.Choice> getPredefinedChoices(@Nullable Guild guild) {
        return Arrays.stream(BanData.BanType.values())
                .map(banType -> new Command.Choice(banType.name(), banType.name()))
                .toList();
    }

    @Nullable
    @Override
    public BanData.BanType resolve(@NotNull SlashCommandOption option,
                                   @NotNull CommandInteractionPayload event,
                                   @NotNull OptionMapping optionMapping) {
        return BanData.BanType.valueOf(optionMapping.getAsString());
    }
}
