package club.ss220.storage.bandastation.spring.jpa.stub;

import club.ss220.core.config.GameConfig;
import club.ss220.core.shared.NoteData;
import club.ss220.core.spi.NoteQuery;
import club.ss220.core.spi.NoteStorage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Qualifier(GameConfig.BUILD_BANDASTRATION)
@ConditionalOnProperty(name = "spring.datasource.bandastation.url", havingValue = "false", matchIfMissing = true)
public class BandaStationNoteStorageStub implements NoteStorage {

    @Override
    public Optional<NoteData> findById(Integer id) {
        throw new IllegalStateException("Build 'bandastation' is disabled or datasource URL is not configured. Set 'spring.datasource.bandastation.url' or remove 'bandastation' from disabled builds.");
    }

    @Override
    public List<NoteData> findByQuery(NoteQuery query) {
        throw new IllegalStateException("Build 'bandastation' is disabled or datasource URL is not configured. Set 'spring.datasource.bandastation.url' or remove 'bandastation' from disabled builds.");
    }
}
