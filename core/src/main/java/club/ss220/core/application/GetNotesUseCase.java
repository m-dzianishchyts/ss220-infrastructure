package club.ss220.core.application;

import club.ss220.core.shared.GameBuild;
import club.ss220.core.shared.NoteData;
import club.ss220.core.spi.NoteQuery;
import club.ss220.core.spi.NoteStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class GetNotesUseCase {

    private final Map<GameBuild, NoteStorage> noteStorages;

    public List<NoteData> execute(NoteQuery query) {
        if (query.getServer() != null) {
            return noteStorages.get(query.getServer().build()).findByQuery(query);
        }
        return noteStorages.values().stream()
                .flatMap(storage -> storage.findByQuery(query).stream())
                .sorted(Comparator.comparing(NoteData::timestamp).reversed())
                .limit(query.getPageSize())
                .toList();
    }
}
