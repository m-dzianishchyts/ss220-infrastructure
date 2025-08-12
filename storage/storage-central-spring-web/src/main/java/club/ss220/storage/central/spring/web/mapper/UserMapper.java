package club.ss220.storage.central.spring.web.mapper;

import club.ss220.storage.central.spring.web.v1.presentation.UserPresentationV1;
import club.ss220.core.shared.UserData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserData toUserData(UserPresentationV1 userPresentationV1);
}
