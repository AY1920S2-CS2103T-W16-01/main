package seedu.address.commons.core;

/** Container for user visible messages. */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_TASK_DISPLAYED_INDEX =
            "The task index provided is invalid";
    public static final String MESSAGE_TASKS_LISTED_OVERVIEW = "%1$d tasks listed!";
    public static final String MESSAGE_INVALID_TASK_TO_BE_DONED =
            "Task has already been marked as done!";
    public static final String MESSAGE_FIELD_UNKNOWN = "The fields do not exist %1$s";
    public static final String COMPLETE_SUCCESS = "Message auto completed: ";
    public static final String COMPLETE_PREFIX_SUCCESS =
            "Message auto completed with these prefixes %1$s";
    public static final String UNCHANGED_SUCCESS = "Command has nothing to complete :)";
    public static final String COMMAND_UNFOUND_FAILURE =
            "Auto complete not possible %1$s not found!";
    public static final String COMPLETE_FAILURE_COMMAND = "Auto complete not possible!";
}
