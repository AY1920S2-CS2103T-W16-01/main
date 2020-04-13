package seedu.address.model.settings;

import static java.util.Objects.requireNonNull;

public class PetName implements Comparable {

    public static final String MESSAGE_CONSTRAINTS =
            "Pet Names should only contain alphanumeric characters and spaces, and it should not be blank";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";
    public final String fullName;

    public PetName(String petName) {
        requireNonNull(petName);
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
