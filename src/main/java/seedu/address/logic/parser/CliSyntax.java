package seedu.address.logic.parser;

/** Contains Command Line Interface (CLI) syntax definitions common to multiple commands */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PRIORITY = new Prefix("p/");
    public static final Prefix PREFIX_DESCRIPTION = new Prefix("des/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_REMINDER = new Prefix("r/");
    public static final Prefix PREFIX_RECURRING = new Prefix("rec/");
    public static final Prefix PREFIX_TIMER = new Prefix("tm/");
    public static final Prefix[] TASK_PREFIXES =
            new Prefix[] {
                PREFIX_NAME, PREFIX_PRIORITY, PREFIX_DESCRIPTION, PREFIX_TAG, PREFIX_REMINDER
            };
    public static final Prefix PREFIX_PET = new Prefix("pet/");
    public static final Prefix PREFIX_POM = new Prefix("pom/");
    public static final Prefix PREFIX_DAILY = new Prefix("daily/");
}
