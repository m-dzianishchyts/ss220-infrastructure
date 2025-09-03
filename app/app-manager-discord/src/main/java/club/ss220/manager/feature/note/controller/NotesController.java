package club.ss220.manager.feature.note.controller;

import club.ss220.core.application.GetNotesUseCase;
import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.MemberData;
import club.ss220.core.shared.NoteData;
import club.ss220.core.spi.NoteQuery;
import club.ss220.manager.feature.note.view.NotesView;
import club.ss220.manager.shared.MemberTarget;
import club.ss220.manager.shared.application.MemberDataProvider;
import club.ss220.manager.shared.pagination.GenericPaginationController;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class NotesController {

    private static final int PAGE_SIZE = 5;

    private final NotesView view;
    private final GenericPaginationController paginationController;
    private final MemberDataProvider memberDataProvider;
    private final GetNotesUseCase getNotesUseCase;

    public void showNotes(InteractionHook hook,
                          @Nullable MemberTarget playerTarget,
                          @Nullable MemberTarget adminTarget,
                          @Nullable GameServerData server,
                          @Nullable Integer roundId,
                          @Nullable Boolean active) {
        try {
            NoteQuery.NoteQueryBuilder builder = NoteQuery.builder();

            if (playerTarget != null) {
                Optional<MemberData> playerData = memberDataProvider.getByTarget(playerTarget);
                if (playerData.isEmpty()) {
                    view.renderMemberNotFound(hook, playerTarget);
                    return;
                }
                builder.ckey(playerData.get().userData().ckey());
            }

            if (adminTarget != null) {
                Optional<MemberData> adminData = memberDataProvider.getByTarget(adminTarget);
                if (adminData.isEmpty()) {
                    view.renderMemberNotFound(hook, adminTarget);
                    return;
                }
                builder.adminCkey(adminData.get().userData().ckey());
            }

            NoteQuery query = builder
                    .server(server)
                    .roundId(roundId)
                    .active(active)
                    .build();

            List<NoteData> notes = getNotesUseCase.execute(query);
            if (notes.isEmpty()) {
                view.renderNoNotesFound(hook);
            } else {
                paginationController.show(hook, notes, PAGE_SIZE, view);
            }

            log.debug("Displayed {} notes for query {}", notes.size(), query);
        } catch (Exception e) {
            throw new RuntimeException("Error displaying notes", e);
        }
    }
}
