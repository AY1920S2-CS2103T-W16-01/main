package seedu.address.ui;

import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.task.Task;

/** Panel containing the list of persons. */
public class TaskListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);

    @FXML private ListView<Task> personListView;

    public TaskListPanel(ObservableList<Task> personList) {
        super(FXML);
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new TaskListViewCell());
    }

    // try try new method
    public void setTaskList(ObservableList<Task> newTaskList) {
        this.personListView.setItems(newTaskList);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Task} using a {@code
     * TaskListCard}.
     */
    class TaskListViewCell extends ListCell<Task> {
        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new TaskListCard(task, getIndex() + 1).getRoot());
            }
        }
    }
}
