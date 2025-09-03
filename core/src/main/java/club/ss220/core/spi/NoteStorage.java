package club.ss220.core.spi;

import club.ss220.core.shared.NoteData;

import java.util.List;
import java.util.Optional;

public interface NoteStorage {

    Optional<NoteData> findById(Integer id);

    List<NoteData> findByQuery(NoteQuery query);
}
