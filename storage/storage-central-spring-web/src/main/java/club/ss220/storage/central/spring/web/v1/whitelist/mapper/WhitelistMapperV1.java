package club.ss220.storage.central.spring.web.v1.whitelist.mapper;

import club.ss220.core.shared.NewWhitelistEntry;
import club.ss220.core.shared.WhitelistData;
import club.ss220.storage.central.spring.web.v1.whitelist.presentation.NewWhitelistDiscordV1;
import club.ss220.storage.central.spring.web.v1.whitelist.presentation.WhitelistPresentationV1;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface WhitelistMapperV1 {

    @Mapping(target = "playerData", ignore = true)
    @Mapping(target = "adminData", ignore = true)
    WhitelistData toWhitelistData(WhitelistPresentationV1 whitelist);

    @Mapping(target = "playerDiscordId", qualifiedByName = "longToString")
    @Mapping(target = "adminDiscordId", qualifiedByName = "longToString")
    NewWhitelistDiscordV1 toNewWhitelistDiscord(NewWhitelistEntry request);

    @Named("longToString")
    default String longToString(long value) {
        return String.valueOf(value);
    }
}
