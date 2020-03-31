package seedu.address.model.settings;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class PetName implements Comparable {

    public static final String MESSAGE_CONSTRAINTS = "NANIIII";
    public static final String VALIDATION_REGEX = "^[a-zA-Z0-9]+$";
    public final String fullName;

    public PetName(String petName) {
        requireNonNull(petName);
        checkArgument(isValidPetName(petName), MESSAGE_CONSTRAINTS);
        fullName = petName;
    }

    public static boolean isValidPetName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    public boolean isEmpty() {
        return fullName.isEmpty();
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PetName // instanceof handles nulls
                        && fullName.equals(((PetName) other).fullName)); // state check
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

    @Override
    public int compareTo(Object obj) {
        if (!(obj instanceof PetName)) {
            return 0;
        }
        PetName other = (PetName) obj;
        return other.fullName.compareToIgnoreCase(this.fullName);
    }
}
