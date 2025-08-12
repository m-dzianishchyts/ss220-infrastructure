package club.ss220.storage.paradise.spring.jpa.character.converter;

import club.ss220.core.shared.GameCharacterData;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ParadiseCharacterSpeciesConverter implements AttributeConverter<GameCharacterData.Species, String> {

    @Override
    public String convertToDatabaseColumn(GameCharacterData.Species species) {
        return species.getName();
    }

    @Override
    public GameCharacterData.Species convertToEntityAttribute(String dbData) {
        return GameCharacterData.Species.fromName(dbData);
    }
}
