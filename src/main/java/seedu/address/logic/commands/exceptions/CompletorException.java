package seedu.address.logic.commands.exceptions;

/** Represents an error which occurs during execution of a {@link Command}. */
public class CompletorException extends Exception {
    public CompletorException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code CompletorException} with the specified detail {@code message} and
     * {@code cause}.
     */
    public CompletorException(String message, Throwable cause) {
        super(message, cause);
    }
}
