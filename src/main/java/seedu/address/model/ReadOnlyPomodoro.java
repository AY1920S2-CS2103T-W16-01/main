package seedu.address.model;

import seedu.address.model.task.Task;

public interface ReadOnlyPomodoro {
    public Task getRunningTask();

    public String getDefaultTime();

    public String getRestTime();

    public String getTimeLeft();
}
