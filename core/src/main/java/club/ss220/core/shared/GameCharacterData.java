package club.ss220.core.shared;

import club.ss220.core.shared.exception.UnknownCharacterSpeciesException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;

@Builder
public record GameCharacterData(
        @NotNull
        Integer id,
        @NotNull
        @NotBlank
        String ckey,
        @NotNull
        @PositiveOrZero
        Integer slot,
        @NotNull
        @NotBlank
        String realName,
        @NotNull
        Gender gender,
        @NotNull
        @PositiveOrZero
        Short age,
        @NotNull
        Species species
) {

    public enum Gender {
        MALE,
        FEMALE,
        PLURAL,
        OTHER;

        public static Gender fromValue(String value) {
            try {
                return Gender.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return OTHER;
            }
        }
    }

    @Getter
    public enum Species {
        HUMAN("Human"),
        DIONA("Diona"),
        DRASK("Drask"),
        GREY("Grey"),
        KIDAN("Kidan"),
        MACHINE("Machine"),
        NIAN("Nian"),
        PLASMAMAN("Plasmaman"),
        SKRELL("Skrell"),
        SLIME_PEOPLE("Slime People"),
        TAJARAN("Tajaran"),
        UNATHI("Unathi"),
        VOX("Vox"),
        VULPKANIN("Vulpkanin"),
        NUCLEATION("Nucleation");

        private final String name;

        Species(String name) {
            this.name = name;
        }

        public static Species fromName(String name) {
            return Arrays.stream(Species.values())
                    .filter(species -> species.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .orElseThrow(() -> new UnknownCharacterSpeciesException(name));
        }
    }
}
