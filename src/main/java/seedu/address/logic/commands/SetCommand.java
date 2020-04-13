package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAILY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POM;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.settings.DailyTarget;
import seedu.address.model.settings.PetName;
import seedu.address.model.settings.PomDuration;

public class SetCommand extends Command {
    public static final String COMMAND_WORD = "set";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD
                    + ": Customises the name of the Pet, duration of Pomodoro and the target of the daily challenge."
                    + "Parameters: "
                    + PREFIX_PET
                    + "NAME OF PET "
                    + PREFIX_POM
                    + "DURATION OF POMODORO "
                    + PREFIX_DAILY
                    + "TARGET OF DAILY CHALLENGE (MINS) \n"
                    + "Example: "
                    + COMMAND_WORD
                    + " "
                    + PREFIX_PET
                    + "Momu "
                    + PREFIX_POM
                    + "30 "
                    + PREFIX_DAILY
                    + "150 ";

    public String MESSAGE_SUCCESS = "Successfuly set!";

    private PetName petName;
    private PomDuration pomDuration;
    private DailyTarget dailyTarget;

    public SetCommand(PetName petName, PomDuration pomDuration, DailyTarget dailyTarget) {
        this.petName = petName;
        this.pomDuration = pomDuration;
        this.dailyTarget = dailyTarget;
    }

    @Override
    public SetCommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        StringBuilder builder = new StringBuilder(MESSAGE_SUCCESS);

        if (!petName.isEmpty()) {
            model.setPetName(petName.toString());
            builder.append(" Pet Name: ").append(petName);
        }

        if (!pomDuration.isEmpty()) {
            String s = pomDuration.toString();
            model.setPomodoroDefaultTime(Float.parseFloat(s));
            builder.append(" Pomodoro Duration: ").append(pomDuration).append(" mins");
        }

        if (!dailyTarget.isEmpty()) {
            builder.append(" Daily Target: ").append(dailyTarget).append(" mins");
        }

        MESSAGE_SUCCESS = builder.toString();

        return new SetCommandResult(MESSAGE_SUCCESS, petName, pomDuration, dailyTarget);
    }

    public PetName getPetName() {
        return this.petName;
    }

    public PomDuration getPomDuration() {
        return this.pomDuration;
    }

    public DailyTarget getDailyTarget() {
        return this.dailyTarget;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                        && petName.equals(((SetCommand) other).getPetName())
                        && pomDuration.equals(((SetCommand) other).getPomDuration())
                        && dailyTarget.equals(((SetCommand) other).getDailyTarget()));
    }
}
