package club.ss220.storage.paradise.spring.jpa.character.mapper;

import club.ss220.storage.paradise.spring.jpa.character.entity.ParadiseCharacterEntity;
import club.ss220.core.shared.GameCharacterData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ParadiseCharacterMapper {
    
    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderFromValue")
    GameCharacterData toCharacterData(ParadiseCharacterEntity character);

    @Named("genderFromValue")
    default GameCharacterData.Gender genderFromValue(String value) {
        return GameCharacterData.Gender.fromValue(value);
    }
}
