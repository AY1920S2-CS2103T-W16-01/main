package seedu.address.logic.commands.exceptions;

/** Represents an error which occurs during execution of a {@link Command}. */
public class CompletorDeletionException extends CompletorException {
    private final String newCommand;

    public CompletorDeletionException(String newCommand, String message) {
        super(message);
        this.newCommand = newCommand;
    }

    /**
     * Constructs a new {@code CompletorDeletionException} with the specified detail {@code message} and
     * {@code cause}.
     */
    public CompletorDeletionException(String newCommand, String message, Throwable cause) {
        super(message, cause);
        this.newCommand = newCommand;
    }
    
    public String getCommand() {
        return this.newCommand;
    }
}
