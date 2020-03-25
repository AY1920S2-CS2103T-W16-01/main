package seedu.address.ui;

import java.util.Comparator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.task.Task;

/** An UI component that displays information of a {@code Task}. */
public class TaskListCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX. As
     * a consequence, UI elements' variable names cannot be set to such keywords or an exception
     * will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on TaskList
     *     level 4</a>
     */
    public final Task task;

    @FXML private HBox cardPane;
    @FXML private Label name;
    @FXML private Label id;
    @FXML private Label priority;
    @FXML private Label description;
    @FXML private Label reminder;
    @FXML private FlowPane tags;

    public TaskListCard(Task task, int displayedIndex) {
        super(FXML);
        this.task = task;
        id.setText(displayedIndex + ". ");
        name.setText(String.format("[%s] %s", task.getDone().toString(), task.getName().fullName));
        priority.setText(task.getPriority().value);
        description.setText(task.getDescription().value);
        task.getOptionalReminder().ifPresent(rem -> reminder.setText(rem.displayReminder()));
        task.getTags()
                .stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TaskListCard)) {
            return false;
        }

        // state check
        TaskListCard card = (TaskListCard) other;
        return id.getText().equals(card.id.getText()) && task.equals(card.task);
    }
}
