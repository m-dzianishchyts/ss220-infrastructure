package club.ss220.storage.paradise.spring.jpa.note;

import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.NoteData;
import club.ss220.core.spi.NoteQuery;
import club.ss220.core.spi.NoteStorage;
import club.ss220.storage.paradise.spring.jpa.note.mapper.ParadiseNoteMapper;
import club.ss220.storage.paradise.spring.jpa.note.repository.ParadiseNoteJpaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static club.ss220.core.config.GameConfig.BUILD_PARADISE;

@Component
@Qualifier(BUILD_PARADISE)
public class ParadiseJpaNoteStorage implements NoteStorage {

    private final ParadiseNoteJpaRepository jpaRepository;
    private final ParadiseNoteMapper mapper;

    public ParadiseJpaNoteStorage(ParadiseNoteJpaRepository jpaRepository, ParadiseNoteMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<NoteData> findById(Integer id) {
        return jpaRepository.findById(id).map(mapper::toNoteData);
    }

    @Override
    public List<NoteData> findByQuery(NoteQuery query) {
        GameServerData server = query.getServer();
        String serverId = Optional.ofNullable(server).map(GameServerData::id).orElse(null);
        Boolean deleted = Optional.ofNullable(query.getActive()).map(active -> !active).orElse(null);
        PageRequest pageable = PageRequest.of(query.getPage(), query.getPageSize());
        return jpaRepository.findByQuery(query.getCkey(), query.getAdminCkey(), serverId, query.getRoundId(),
                                         deleted, pageable)
                .stream()
                .map(mapper::toNoteData)
                .toList();
    }
}
