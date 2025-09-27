package club.ss220.storage.paradise.spring.jpa.stub;

import club.ss220.core.shared.NoteData;
import club.ss220.core.spi.NoteQuery;
import club.ss220.core.spi.NoteStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static club.ss220.core.config.GameConfig.BUILD_PARADISE;

@Component
@Qualifier(BUILD_PARADISE)
@ConditionalOnProperty(name = "spring.datasource.paradise.url", havingValue = "false", matchIfMissing = true)
public class ParadiseNoteStorageStub implements NoteStorage {

    @Override
    public Optional<NoteData> findById(Integer id) {
        throw new IllegalStateException("Build 'paradise' is disabled. Enable 'ss220.build.paradise.enabled=true' to use NoteStorage for paradise.");
    }

    @Override
    public List<NoteData> findByQuery(NoteQuery query) {
        throw new IllegalStateException("Build 'paradise' is disabled. Enable 'ss220.build.paradise.enabled=true' to use NoteStorage for paradise.");
    }
}
