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
    // Command completor messages
    public static final String COMPLETE_SUCCESS = "Message auto completed: ";
    public static final String COMPLETE_PREFIX_SUCCESS =
            "Message auto completed with these prefixes:\n%1$s";
    public static final String COMPLETE_UNFOUND_FAILURE =
            "Auto complete not possible %1$s not found!";
    public static final String COMPLETE_INDEX_OUT_OF_RANGE_REMOVAL =
            "These indexes removed as they are out of range/invalid:\n%1$s";
    public static final String COMPLETE_UNKNOWN_SORT_FIELDS =
            "These fields removed as they are unknown:\n%1$s";
    public static final String COMPLETE_INDEX_OUT_OF_RANGE_FAILURE =
            "Index %1$s is out of range/invalid";
    public static final String MESSAGE_TASK_IN_PROGRESS =
            "Task is being pommed! Please use \"pom stop\" first.";
}
