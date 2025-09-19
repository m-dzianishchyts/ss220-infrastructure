package club.ss220.storage.central.spring.web.v1.blacklist.mapper;

import club.ss220.core.shared.BlacklistEntryData;
import club.ss220.core.shared.NewBlacklistEntry;
import club.ss220.storage.central.spring.web.v1.blacklist.presentation.BlacklistPresentationV1;
import club.ss220.storage.central.spring.web.v1.blacklist.presentation.NewBlacklistEntryDiscordIdV1;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BlacklistMapperV1 {

    @Mapping(target = "playerData", ignore = true)
    @Mapping(target = "adminData", ignore = true)
    BlacklistEntryData toBlacklistEntryData(BlacklistPresentationV1 blacklist);

    @Mapping(target = "playerDiscordId", qualifiedByName = "longToString")
    @Mapping(target = "adminDiscordId", qualifiedByName = "longToString")
    NewBlacklistEntryDiscordIdV1 toNewWhitelistEntry(NewBlacklistEntry request);

    @Named("longToString")
    default String longToString(long value) {
        return String.valueOf(value);
    }
}
