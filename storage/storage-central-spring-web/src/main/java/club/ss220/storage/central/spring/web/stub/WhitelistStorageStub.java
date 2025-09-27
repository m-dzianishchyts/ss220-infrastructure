package club.ss220.storage.central.spring.web.stub;

import club.ss220.core.shared.NewWhitelistEntry;
import club.ss220.core.shared.WhitelistData;
import club.ss220.core.spi.WhitelistQuery;
import club.ss220.core.spi.WhitelistStorage;
import club.ss220.storage.central.spring.web.exception.StorageDisabledException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@ConditionalOnProperty(name = "ss220.api.central.endpoint", havingValue = "false", matchIfMissing = true)
public class WhitelistStorageStub implements WhitelistStorage {

    @Override
    public Optional<WhitelistData> findById(Integer id) {
        throw new StorageDisabledException(WhitelistStorage.class);
    }

    @Override
    public List<WhitelistData> findByQuery(WhitelistQuery query) {
        throw new StorageDisabledException(WhitelistStorage.class);
    }

    @Override
    public WhitelistData save(NewWhitelistEntry request) {
        throw new StorageDisabledException(WhitelistStorage.class);
    }
}
