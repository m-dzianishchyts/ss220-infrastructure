package club.ss220.storage.central.spring.web.stub;

import club.ss220.core.shared.BlacklistEntryData;
import club.ss220.core.shared.NewBlacklistEntry;
import club.ss220.core.spi.BlacklistQuery;
import club.ss220.core.spi.BlacklistStorage;
import club.ss220.storage.central.spring.web.exception.StorageDisabledException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@ConditionalOnProperty(name = "ss220.api.central.endpoint", havingValue = "false", matchIfMissing = true)
public class BlacklistStorageStub implements BlacklistStorage {

    @Override
    public Optional<BlacklistEntryData> findById(Integer id) {
        throw new StorageDisabledException(BlacklistStorage.class);
    }

    @Override
    public List<BlacklistEntryData> findByQuery(BlacklistQuery query) {
        throw new StorageDisabledException(BlacklistStorage.class);
    }

    @Override
    public BlacklistEntryData save(NewBlacklistEntry request) {
        throw new StorageDisabledException(BlacklistStorage.class);
    }
}
