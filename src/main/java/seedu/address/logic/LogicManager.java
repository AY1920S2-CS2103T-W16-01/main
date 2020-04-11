package seedu.address.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandCompletor;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.CompletorResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.exceptions.CompletorException;
import seedu.address.logic.parser.TaskListParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyPomodoro;
import seedu.address.model.ReadOnlyTaskList;
import seedu.address.model.TaskList;
import seedu.address.model.task.Task;
import seedu.address.storage.Storage;

/** The main LogicManager of the app. */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final TaskListParser taskListParser;
    private final CommandCompletor commandCompletor;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        this.model.setTaskSaver(this::taskSaver);
        taskListParser = new TaskListParser();
        commandCompletor = new CommandCompletor();
    }

    private void taskSaver(TaskList tasklist) {
        try {
            storage.saveTaskList(model.getTaskList());
        } catch (IOException ioe) {
        }
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        CommandResult commandResult;
        Command command = taskListParser.parseCommand(commandText);
        commandResult = command.execute(model);

        try {
            storage.saveTaskList(model.getTaskList());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        try {
            storage.savePet(model.getPet());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        try {
            storage.savePomodoro(model.getPomodoro());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        try {
            storage.saveStatistics(model.getStatistics());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        return commandResult;
    }

    @Override
    public CompletorResult suggestCommand(String userInput) throws CompletorException {
        return commandCompletor.getSuggestedCommand(userInput, this.getFilteredTaskList().size());
    }

    @Override
    public ReadOnlyTaskList getTaskList() {
        return model.getTaskList();
    }

    @Override
    public ObservableList<Task> getFilteredTaskList() {
        ObservableList<Task> tasklist = model.getFilteredTaskList();
        return tasklist;
    }

    @Override
    public Path getTaskListFilePath() {
        return model.getTaskListFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    @Override
    public ReadOnlyPomodoro getPomodoro() {
        return model.getPomodoro();
    }

    // @Override
    // public void update() throws CommandException {
    //     try {
    //         storage.saveTaskList(model.getTaskList());
    //     } catch (IOException ioe) {
    //         throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
    //     }
    // }
}
