package club.ss220.core.shared.exception;

public class UnknownCharacterSpeciesException extends RuntimeException {

    private final String species;

    public UnknownCharacterSpeciesException(String species) {
        super("Unknown species: " + species);
        this.species = species;
    }
}
