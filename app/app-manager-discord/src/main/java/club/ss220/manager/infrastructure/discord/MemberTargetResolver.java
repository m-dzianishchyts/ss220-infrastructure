package club.ss220.manager.infrastructure.discord;

import club.ss220.core.util.ByondUtils;
import club.ss220.manager.shared.MemberTarget;
import io.github.freya022.botcommands.api.commands.application.slash.options.SlashCommandOption;
import io.github.freya022.botcommands.api.core.service.annotations.Resolver;
import io.github.freya022.botcommands.api.parameters.ClassParameterResolver;
import io.github.freya022.botcommands.api.parameters.resolvers.SlashParameterResolver;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.commands.CommandInteractionPayload;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Slf4j
@Resolver
public class MemberTargetResolver
        extends ClassParameterResolver<MemberTargetResolver, MemberTarget>
        implements SlashParameterResolver<MemberTargetResolver, MemberTarget> {

    private final ByondUtils byondUtils;

    public MemberTargetResolver(ByondUtils byondUtils) {
        super(MemberTarget.class);
        this.byondUtils = byondUtils;
    }

    @NotNull
    @Override
    public OptionType getOptionType() {
        return OptionType.STRING;
    }

    @Nullable
    @Override
    public MemberTarget resolve(@NotNull SlashCommandOption option,
                                @NotNull CommandInteractionPayload event,
                                @NotNull OptionMapping optionMapping) {
        String query = optionMapping.getAsString();
        String queryTrimmed = query.trim();
        if (query.isEmpty()) {
            log.debug("Could not resolve empty query");
            return null;
        }

        MemberTarget target = targetFromMention(queryTrimmed);
        if (target != null) {
            log.debug("Resolved mention: {}", target);
            return target;
        }
        target = targetFromDiscordId(queryTrimmed);
        if (target != null) {
            log.debug("Resolved discordId: {}", target);
            return target;
        }
        target = targetFromCkey(queryTrimmed);
        if (target != null) {
            log.debug("Resolved ckey: {}", target);
            return target;
        }
        log.debug("Could not resolve query: {}", query);
        return null;
    }

    private MemberTarget targetFromMention(String query) {
        if (!query.startsWith("<@") || !query.endsWith(">")) {
            return null;
        }

        String discordId = query.substring(2, query.length() - 1);
        if (discordId.startsWith("!")) {
            discordId = discordId.substring(1);
        }
        return targetFromDiscordId(discordId);
    }

    private MemberTarget targetFromDiscordId(String query) {
        try {
            long idLong = Long.parseUnsignedLong(query);
            return MemberTarget.fromDiscordId(idLong);
        } catch (NumberFormatException _) {
            return null;
        }
    }

    private MemberTarget targetFromCkey(String query) {
        String ckey = byondUtils.sanitizeCkey(query);
        if (ckey.isBlank()) {
            return null;
        }
        return MemberTarget.fromCkey(ckey);
    }
}
