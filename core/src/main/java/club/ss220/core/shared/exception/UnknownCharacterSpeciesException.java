package club.ss220.core.shared.exception;

import lombok.Getter;

@Getter
public class UnknownCharacterSpeciesException extends DomainException {

    private final String species;

    public UnknownCharacterSpeciesException(String species) {
        super("Unknown species: " + species);
        this.species = species;
    }
}
