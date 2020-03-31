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

    public static final String MESSAGE_SUCCESS =
            "Successfuly set! Pet Name: %s, Pomodoro Duation: %s, Daily Challenge: %s";

    private PetName petName;
    private PomDuration pomDuration;
    private DailyTarget dailyTarget;

    // public SetCommand(PetName petName, PomDuration pomDuration, DailyTarget dailyTarget) {
    //     this.petName = petName;
    //     this.pomDuration = pomDuration;
    //     this.dailyTarget = dailyTarget;
    // }
    public SetCommand(PetName petName) {
        this.petName = petName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!petName.isEmpty()) {
            model.setPetName(petName.toString());
        }

        // if(!pomDuration.isEmpty()) {
        //     model.setPomDuration(pomDuration);
        // }

        // if(!dailyTarget.isEmpty()) {
        //     model.setDailyTarget(dailyTarget);
        // }

        return new CommandResult(String.format(MESSAGE_SUCCESS, petName, pomDuration, dailyTarget));
    }
}
