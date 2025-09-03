package club.ss220.storage.bandastation.spring.jpa.note;

import club.ss220.core.shared.GameServerData;
import club.ss220.core.shared.NoteData;
import club.ss220.core.spi.NoteQuery;
import club.ss220.core.spi.NoteStorage;
import club.ss220.core.util.InetUtils;
import club.ss220.storage.bandastation.spring.jpa.note.mapper.BandaStationMessageMapper;
import club.ss220.storage.bandastation.spring.jpa.note.repository.BandaMessageJpaRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static club.ss220.core.config.GameConfig.BUILD_BANDASTRATION;

@Component
@Qualifier(BUILD_BANDASTRATION)
public class BandaStationJpaNoteStorage implements NoteStorage {

    private static final String MESSAGE_TYPE = "note";

    private final BandaMessageJpaRepository repository;
    private final BandaStationMessageMapper mapper;

    public BandaStationJpaNoteStorage(BandaMessageJpaRepository repository,
                                      BandaStationMessageMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<NoteData> findById(Integer id) {
        return repository.findByIdAndType(id, MESSAGE_TYPE).map(mapper::toNoteData);
    }

    @Override
    public List<NoteData> findByQuery(NoteQuery query) {
        GameServerData server = query.getServer();
        Long serverIp = Optional.ofNullable(server).map(GameServerData::ip).map(InetUtils::toCompactIPv4).orElse(null);
        Integer serverPort = Optional.ofNullable(server).map(GameServerData::port).orElse(null);
        Boolean deleted = Optional.ofNullable(query.getActive()).map(active -> !active).orElse(null);
        PageRequest pageable = PageRequest.of(query.getPage(), query.getPageSize());
        return repository.findByQuery(serverIp, serverPort, query.getRoundId(), MESSAGE_TYPE,
                                      query.getCkey(), query.getAdminCkey(), deleted, pageable)
                .stream()
                .map(mapper::toNoteData)
                .toList();
    }
}
