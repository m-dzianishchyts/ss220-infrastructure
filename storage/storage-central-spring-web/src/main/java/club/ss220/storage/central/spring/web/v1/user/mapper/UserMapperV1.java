package club.ss220.storage.central.spring.web.v1.user.mapper;

import club.ss220.core.shared.UserData;
import club.ss220.storage.central.spring.web.v1.user.presentation.UserPresentationV1;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapperV1 {

    UserData toUserData(UserPresentationV1 user);
}
