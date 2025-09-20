package club.ss220.manager.feature.member.controller;

import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.MemberData;
import club.ss220.manager.feature.member.view.MemberInfoView;
import club.ss220.manager.shared.MemberTarget;
import club.ss220.manager.shared.application.MemberDataProvider;
import club.ss220.manager.shared.presentation.Senders;
import io.github.freya022.botcommands.api.components.event.StringSelectEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class MemberInfoController {

    private final MemberInfoView view;
    private final MemberDataProvider memberDataProvider;
    private final Senders senders;

    public void renderMemberInfo(IReplyCallback interaction, User viewer) {
        MemberTarget target = MemberTarget.fromUser(viewer);
        boolean isConfidential = false;
        renderMemberInfo(interaction, viewer, target, isConfidential);
    }

    public void renderMemberInfo(IReplyCallback interaction, User viewer, User target) {
        MemberTarget memberTarget = MemberTarget.fromUser(target);
        boolean isConfidential = true;
        renderMemberInfo(interaction, viewer, memberTarget, isConfidential);
    }

    public void renderMemberInfo(IReplyCallback interaction, User viewer, MemberTarget target) {
        boolean isConfidential = true;
        renderMemberInfo(interaction, viewer, target, isConfidential);
    }

    public void renderMemberInfo(IReplyCallback interaction, User viewer, MemberTarget target, boolean isConfidential) {
        interaction.deferReply().setEphemeral(true).queue();

        Optional<MemberData> memberOptional = memberDataProvider.getByTarget(target);
        if (memberOptional.isEmpty()) {
            senders.sendEmbed(interaction, view.renderMemberNotFound(target));
            return;
        }

        MemberData member = memberOptional.get();
        GameBuild defaultBuild = member.gameInfo().firstKey();
        MemberInfoContext context = isConfidential
                                    ? MemberInfoContext.confidentialInfo(member, defaultBuild)
                                    : MemberInfoContext.publicInfo(member, defaultBuild);

        InteractionHook hook = interaction.getHook();
        hook.sendMessage(view.buildInitialMessage(
                viewer,
                context,
                e -> handleBuildSelection(e, context))
        ).queue();
        log.debug("Displayed {} member info for target {}", isConfidential ? "confidential" : "public", target);
    }

    public void handleBuildSelection(StringSelectEvent selectEvent, MemberInfoContext context) {
        selectEvent.deferEdit().queue();

        String selectedValue = selectEvent.getValues().getFirst();
        GameBuild selectedBuild = GameBuild.valueOf(selectedValue);
        MemberInfoContext newContext = context.withBuild(selectedBuild);

        selectEvent.getHook().editOriginal(view.buildUpdateMessage(
                selectEvent.getUser(),
                newContext,
                e -> handleBuildSelection(e, context)
        )).queue();
        log.debug("Displayed updated info with build selection: {}", selectedBuild.getName());
    }

    @Value
    @Builder(toBuilder = true)
    public static class MemberInfoContext {

        MemberData member;
        GameBuild selectedBuild;
        boolean confidential;

        public static MemberInfoContext publicInfo(MemberData member, GameBuild selectedBuild) {
            return MemberInfoContext.builder()
                    .member(member)
                    .selectedBuild(selectedBuild)
                    .confidential(false)
                    .build();
        }

        public static MemberInfoContext confidentialInfo(MemberData member, GameBuild selectedBuild) {
            return MemberInfoContext.builder()
                    .member(member)
                    .selectedBuild(selectedBuild)
                    .confidential(true)
                    .build();
        }

        public MemberInfoContext withBuild(GameBuild newBuild) {
            return toBuilder()
                    .selectedBuild(newBuild)
                    .build();
        }
    }
}
