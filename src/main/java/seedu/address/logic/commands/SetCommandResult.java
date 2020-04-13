package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.settings.DailyTarget;
import seedu.address.model.settings.PetName;
import seedu.address.model.settings.PomDuration;

public class SetCommandResult extends CommandResult {
    PetName petName;
    PomDuration pomDuration;
    DailyTarget dailyTarget;

    public SetCommandResult(
            String feedbackToUser,
            PetName petName,
            PomDuration pomDuration,
            DailyTarget dailyTarget) {
        super(requireNonNull(feedbackToUser));
        this.petName = petName;
        this.pomDuration = pomDuration;
        this.dailyTarget = dailyTarget;
    }

    public PetName getPetName() {
        return petName;
    }

    public PomDuration getPomDuration() {
        return pomDuration;
    }

    public DailyTarget getDailyTarget() {
        return dailyTarget;
    }
}
