package club.ss220.manager.feature.member.controller;

import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.MemberData;
import club.ss220.manager.feature.member.view.MemberInfoView;
import club.ss220.manager.shared.MemberTarget;
import club.ss220.manager.shared.application.MemberDataProvider;
import io.github.freya022.botcommands.api.components.event.StringSelectEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class MemberInfoController {

    private final MemberInfoView view;
    private final MemberDataProvider memberDataProvider;

    public void showMemberInfo(InteractionHook hook, User viewer) {
        MemberTarget target = MemberTarget.fromUser(viewer);
        boolean isConfidential = false;
        showMemberInfo(hook, viewer, target, isConfidential);
    }

    public void showMemberInfo(InteractionHook hook, User viewer, User target) {
        MemberTarget memberTarget = MemberTarget.fromUser(target);
        boolean isConfidential = true;
        showMemberInfo(hook, viewer, memberTarget, isConfidential);
    }

    public void showMemberInfo(InteractionHook hook, User viewer, MemberTarget target) {
        boolean isConfidential = true;
        showMemberInfo(hook, viewer, target, isConfidential);
    }

    public void showMemberInfo(InteractionHook hook, User viewer, MemberTarget target, boolean isConfidential) {
        try {
            Optional<MemberData> memberOptional = memberDataProvider.getByTarget(target);
            if (memberOptional.isEmpty()) {
                view.renderMemberNotFound(hook, target);
                log.debug("Member not found for target {}", target);
                return;
            }

            MemberData member = memberOptional.get();
            GameBuild defaultBuild = member.gameInfo().firstKey();
            MemberInfoContext context = isConfidential
                                        ? MemberInfoContext.confidentialInfo(member, defaultBuild)
                                        : MemberInfoContext.publicInfo(member, defaultBuild);

            view.renderMemberInfo(hook, viewer, context);

            log.debug("Displayed {} member info for target {}", isConfidential ? "confidential" : "public", target);
        } catch (Exception e) {
            throw new RuntimeException("Error displaying member info for target " + target, e);
        }
    }

    public void handleBuildSelection(StringSelectEvent selectEvent, MemberInfoContext context,
                                     GameBuild selectedBuild) {
        try {
            selectEvent.deferEdit().queue();

            MemberInfoContext newContext = context.withBuild(selectedBuild);
            view.updateMemberInfo(selectEvent.getHook(), selectEvent.getUser(), newContext);

            log.debug("Displayed updated info with build selection: {}", selectedBuild.getName());
        } catch (Exception e) {
            throw new RuntimeException("Error handling build selection for build " + selectedBuild, e);
        }
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
