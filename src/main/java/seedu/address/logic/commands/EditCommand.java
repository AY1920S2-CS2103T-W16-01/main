package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Description;
import seedu.address.model.task.Done;
import seedu.address.model.task.Name;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Recurring;
import seedu.address.model.task.Reminder;
import seedu.address.model.task.Task;

/** Edits the details of an existing task in the task list. */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE =
            COMMAND_WORD
                    + ": Edits the details of the task identified "
                    + "by the index number used in the displayed task list. "
                    + "Existing values will be overwritten by the input values.\n"
                    + "Parameters: INDEX (must be a positive integer) "
                    + "["
                    + PREFIX_NAME
                    + "NAME] "
                    + "["
                    + PREFIX_PRIORITY
                    + "PRIORITY] "
                    + "["
                    + PREFIX_DESCRIPTION
                    + "DESCRIPTION] "
                    + "["
                    + PREFIX_TAG
                    + "TAG]...\n"
                    + "Example: "
                    + COMMAND_WORD
                    + " 1 "
                    + PREFIX_PRIORITY
                    + "2 ";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: \n%1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK =
            "This task already exists in the task list.";

    private final Index index;
    private final EditTaskDescriptor editTaskDescriptor;

    /**
     * @param index of the task in the filtered task list to edit
     * @param editTaskDescriptor details to edit the task with
     */
    public EditCommand(Index index, EditTaskDescriptor editTaskDescriptor) {
        requireNonNull(index);
        requireNonNull(editTaskDescriptor);

        this.index = index;
        this.editTaskDescriptor = new EditTaskDescriptor(editTaskDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        Task taskToEdit = lastShownList.get(index.getZeroBased());
        Task editedTask = createEditedTask(taskToEdit, editTaskDescriptor);

        if (!taskToEdit.isSameTask(editedTask) && model.hasTask(editedTask)) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }

        model.setTask(taskToEdit, editedTask);

        Task pommedTask = model.getPomodoroTask();
        if (pommedTask != null && pommedTask.equals(taskToEdit)) {
            model.setPomodoroTask(editedTask);
            model.getPomodoroManager()
                    .getPomodoroDisplay()
                    .setTaskInProgressText(editedTask.getName().toString());
        }

        model.showAllTasks();
        return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, editedTask));
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit} edited with {@code
     * editTaskDescriptor}.
     */
    private static Task createEditedTask(Task taskToEdit, EditTaskDescriptor editTaskDescriptor) {
        assert taskToEdit != null;

        Name updatedName = editTaskDescriptor.getName().orElse(taskToEdit.getName());
        Priority updatedPriority =
                editTaskDescriptor.getPriority().orElse(taskToEdit.getPriority());
        Description updatedDescription =
                editTaskDescriptor.getDescription().orElse(taskToEdit.getDescription());
        Done updatedDone = editTaskDescriptor.getDone().orElse(taskToEdit.getDone());
        Set<Tag> updatedTags = editTaskDescriptor.getTags().orElse(taskToEdit.getTags());
        Optional<Reminder> updatedOptionalReminder =
                editTaskDescriptor.getReminder().isPresent()
                        ? editTaskDescriptor.getReminder()
                        : taskToEdit.getOptionalReminder();
        Optional<Recurring> updatedOptionalRecurring =
                editTaskDescriptor.getRecurring().isPresent()
                        ? editTaskDescriptor.getRecurring()
                        : taskToEdit.getOptionalRecurring();

        return new Task(
                updatedName,
                updatedPriority,
                updatedDescription,
                updatedDone,
                updatedTags,
                updatedOptionalReminder,
                updatedOptionalRecurring);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index) && editTaskDescriptor.equals(e.editTaskDescriptor);
    }

    /**
     * Stores the details to edit the task with. Each non-empty field value will replace the
     * corresponding field value of the task.
     */
    public static class EditTaskDescriptor {
        private Name name;
        private Priority priority;
        private Description description;
        private Done done;
        private Set<Tag> tags;
        private Reminder reminder;
        private Recurring recurring;

        public EditTaskDescriptor() {}

        /** Copy constructor. A defensive copy of {@code tags} is used internally. */
        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            setName(toCopy.name);
            setPriority(toCopy.priority);
            setDescription(toCopy.description);
            setDone(toCopy.done);
            setTags(toCopy.tags);
            setReminder(toCopy.reminder);
            setRecurring(toCopy.recurring);
        }

        /** Returns true if at least one field is edited. */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(
                    name, priority, description, tags, reminder, recurring);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setDone(Done done) {
            this.done = done;
        }

        public Optional<Done> getDone() {
            return Optional.ofNullable(done);
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        public Optional<Priority> getPriority() {
            return Optional.ofNullable(priority);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}. A defensive copy of {@code tags} is used
         * internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException} if
         * modification is attempted. Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null)
                    ? Optional.of(Collections.unmodifiableSet(tags))
                    : Optional.empty();
        }

        public void setReminder(Reminder reminder) {
            this.reminder = reminder;
        }

        public Optional<Reminder> getReminder() {
            return Optional.ofNullable(reminder);
        }

        public void setRecurring(Recurring recurring) {
            this.recurring = recurring;
        }

        public Optional<Recurring> getRecurring() {
            return Optional.ofNullable(recurring);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTaskDescriptor)) {
                return false;
            }

            // state check
            EditTaskDescriptor e = (EditTaskDescriptor) other;

            return getName().equals(e.getName())
                    && getPriority().equals(e.getPriority())
                    && getDescription().equals(e.getDescription())
                    && getDone().equals(e.getDone())
                    && getTags().equals(e.getTags());
        }
    }
}
