package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import seedu.address.commons.core.GuiSettings;

/** Represents User's preferences. */
public class UserPrefs implements ReadOnlyUserPrefs {

    private GuiSettings guiSettings = new GuiSettings();
    private Path taskListFilePath = Paths.get("data", "tasklist.json");
    private Path petFilePath = Paths.get("data", "pet.json");
    private Path pomodoroFilePath = Paths.get("data", "pomodoro.json");

    /** Creates a {@code UserPrefs} with default values. */
    public UserPrefs() {}

    /** Creates a {@code UserPrefs} with the prefs in {@code userPrefs}. */
    public UserPrefs(ReadOnlyUserPrefs userPrefs) {
        this();
        resetData(userPrefs);
    }

    /** Resets the existing data of this {@code UserPrefs} with {@code newUserPrefs}. */
    public void resetData(ReadOnlyUserPrefs newUserPrefs) {
        requireNonNull(newUserPrefs);
        setGuiSettings(newUserPrefs.getGuiSettings());
        setTaskListFilePath(newUserPrefs.getTaskListFilePath());
        setPetFilePath(newUserPrefs.getPetFilePath());
        setPomodoroFilePath(newUserPrefs.getPomodoroFilePath());
    }

    public GuiSettings getGuiSettings() {
        return guiSettings;
    }

    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        this.guiSettings = guiSettings;
    }

    public Path getTaskListFilePath() {
        return taskListFilePath;
    }

    public Path getPetFilePath() {
        return petFilePath;
    }

    public Path getPomodoroFilePath() {
        return pomodoroFilePath;
    }

    public void setTaskListFilePath(Path taskListFilePath) {
        requireNonNull(taskListFilePath);
        this.taskListFilePath = taskListFilePath;
    }

    public void setPetFilePath(Path petFilePath) {
        requireNonNull(petFilePath);
        this.petFilePath = petFilePath;
    }

    public void setPomodoroFilePath(Path pomodoroFilePath) {
        requireNonNull(pomodoroFilePath);
        this.pomodoroFilePath = pomodoroFilePath;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { // this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return guiSettings.equals(o.guiSettings) && taskListFilePath.equals(o.taskListFilePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, taskListFilePath);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings);
        sb.append("\nLocal data file location : " + taskListFilePath);
        return sb.toString();
    }
}
