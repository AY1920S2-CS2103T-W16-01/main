package seedu.address.model.dayData.exceptions;

/** Signals that some given data does not fulfill some constraints. */
public class InvalidTableException extends Exception {
    /** @param message should contain relevant information on the failed constraint(s) */
    public InvalidTableException(String message) {
        super(message);
    }
}
