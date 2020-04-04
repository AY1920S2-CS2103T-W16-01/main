package seedu.address.model;

import seedu.address.model.task.Task;

public class Pomodoro implements ReadOnlyPomodoro {
    private static final String DEFAULT_TIME = "25.0";
    private static final String DEFAULT_REST = "5.0";
    public static final String TIME_REGEX = "\\d+.?\\d*";

    public String defaultTime;
    public String restTime;
    public String timeLeft;
    public Task runningTask; // runningTask will be null if not running tasks

    public Pomodoro(String defaultTime, String restTime, String timeLeft, Task runningTask) {
        if (defaultTime == null || !defaultTime.matches(TIME_REGEX)) {
            defaultTime = DEFAULT_TIME;
        }

        if (restTime == null || !restTime.matches(TIME_REGEX)) {
            restTime = DEFAULT_REST;
        }

        if (timeLeft == null || !timeLeft.matches(TIME_REGEX)) {
            timeLeft = defaultTime;
        }

        this.defaultTime = defaultTime;
        this.restTime = restTime;
        this.timeLeft = timeLeft;
        this.runningTask = runningTask;
    }

    public Pomodoro(ReadOnlyPomodoro source) {
        this(source.getDefaultTime(), source.getRestTime(), source.getTimeLeft(), source.getRunningTask());
    }

    public Pomodoro() {
        this(DEFAULT_TIME, DEFAULT_REST, DEFAULT_TIME, null);
    }

    public void setTask(Task runningTask) {
        this.runningTask = runningTask;
    }

    public void setDefaultTime(String defaultTime) {
        this.defaultTime = defaultTime;
    }

    public void setRestTime(String restTime) {
        this.restTime = restTime;
    }

    @Override
    public Task getRunningTask() {
        return this.runningTask;
    }

    @Override
    public String getDefaultTime() {
        return this.defaultTime;
    }

    @Override
    public String getRestTime() {
        return this.restTime;
    }

    @Override
    public String getTimeLeft() {
        return this.timeLeft;
    }

    @Override
    public String toString() {
        return String.format(
                "Hi running task is: %s! my timeleft is %s and my default time is %s",
                runningTask == null ? "No tasks!" : runningTask.toString(), timeLeft, defaultTime);
    }
}
