package seedu.address.ui;

import java.util.Comparator;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import seedu.address.model.task.Task;

/** An UI component that displays information of a {@code Task}. */
public class TaskListCard extends UiPart<Region> {

    private static final String FXML = "TaskListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX. As
     * a consequence, UI elements' variable names cannot be set to such keywords or an exception
     * will be thrown by JavaFX during runtime.
     */
    public final Task task;

    @FXML private HBox cardPane;
    @FXML private Label name;
    @FXML private Label id;
    @FXML private Label priority;
    @FXML private Label description;
    @FXML private Label recurring;
    @FXML private Label reminder;
    @FXML private FlowPane tags;
    @FXML private CheckBox done;

    public TaskListCard(Task task, int displayedIndex) {
        super(FXML);
        this.task = task;
        id.setText(displayedIndex + ". ");
        name.setText(task.getName().fullName);
        done.setSelected(task.getDone().getIsDone());
        done.setDisable(true);
        priority.setText(getPriorityString());
        priority.setTextFill(Color.web(getPriorityColor()));
        description.setText(task.getDescription().value);
        task.getOptionalReminder().ifPresent(rem -> reminder.setText(rem.displayReminder()));
        task.getOptionalRecurring().ifPresent(rec -> recurring.setText(rec.displayRecurring()));
        task.getTags()
                .stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    private String getPriorityString() {
        String value = task.getPriority().value;
        switch (value) {
            case "1":
                return "low";
            case "2":
                return "medium";
            case "3":
                return "high";
            default:
                return "low";
        }
    }

    private String getPriorityColor() {
        String value = task.getPriority().value;
        switch (value) {
            case "1":
                return "#2EBE04";
            case "2":
                return "#F8713D";
            case "3":
                return "#FF0000";
            default:
                return "#2EBE04";
        }
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
