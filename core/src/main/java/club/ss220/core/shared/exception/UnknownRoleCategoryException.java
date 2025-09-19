package club.ss220.core.shared.exception;

import lombok.Getter;

@Getter
public class UnknownRoleCategoryException extends DomainException {

    private final String roleCategory;

    public UnknownRoleCategoryException(String roleCategory) {
        super("Unknown role category: " + roleCategory);
        this.roleCategory = roleCategory;
    }
}
