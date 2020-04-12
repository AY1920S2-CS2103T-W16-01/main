package seedu.address.ui;

import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.StringUtil;
import seedu.address.model.task.Task;

/** Panel containing the list of tasks. */
public class TaskListPanel extends UiPart<Region> {
    private static final String FXML = "TaskListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);

    @FXML private ListView<Task> taskListView;
    @FXML private Label tasksHeader;

    public TaskListPanel(ObservableList<Task> taskList) {
        super(FXML);
        taskListView.setItems(taskList);
        taskListView.setCellFactory(listView -> new TaskListViewCell());
    }

    public void setTaskList(ObservableList<Task> newTaskList) {
        this.taskListView.setItems(newTaskList);
    }

    public void setSortOrder(String sortOrder) {
        String capitalized = StringUtil.capitalizeWord(sortOrder);
        this.tasksHeader.setText(String.format("Tasks by %s", capitalized));
    }

    public void removeSortOrder() {
        this.tasksHeader.setText("Tasks");
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
