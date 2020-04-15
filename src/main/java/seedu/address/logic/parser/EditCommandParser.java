package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PRIORITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RECURRING;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMINDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.TASK_PREFIXES;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.CompletorResult;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditTaskDescriptor;
import seedu.address.logic.commands.exceptions.CompletorException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;
import seedu.address.model.task.Priority;
import seedu.address.model.task.Reminder;

/** Parses input arguments and creates a new EditCommand object */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand and returns an
     * EditCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(
                        args,
                        PREFIX_NAME,
                        PREFIX_PRIORITY,
                        PREFIX_DESCRIPTION,
                        PREFIX_TAG,
                        PREFIX_REMINDER,
                        PREFIX_RECURRING);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        EditTaskDescriptor editTaskDescriptor = new EditTaskDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editTaskDescriptor.setName(
                    ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PRIORITY).isPresent()) {
            editTaskDescriptor.setPriority(
                    ParserUtil.parsePriority(argMultimap.getValue(PREFIX_PRIORITY).get()));
        }
        if (argMultimap.getValue(PREFIX_DESCRIPTION).isPresent()) {
            editTaskDescriptor.setDescription(
                    ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION).get()));
        }

        if (argMultimap.getValue(PREFIX_REMINDER).isPresent()) {
            editTaskDescriptor.setReminder(
                    ParserUtil.parseReminder(argMultimap.getValue(PREFIX_REMINDER).get()));
        }

        if (argMultimap.getValue(PREFIX_RECURRING).isPresent()) {
            editTaskDescriptor.setRecurring(
                    ParserUtil.parseRecurring(argMultimap.getValue(PREFIX_RECURRING).get()));
        }

        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG))
                .ifPresent(editTaskDescriptor::setTags);

        if (!editTaskDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editTaskDescriptor);
    }

    /**
     * Uses argMultimap to detect existing prefixes used so that it won't add double prefixes. Adds
     * priority and reminder prefixes. Note that we ignore the second word when adding prefixes as
     * it should be the index Also note that if the index is invalid, an exception is thrown
     *
     * @param input trimmed
     * @param listSize
     * @return contains userFeedback and suggestedCommand
     * @throws CompletorException throws an exception when index is invalid
     */
    public CompletorResult completeCommand(String input, int listSize) throws CompletorException {
        String[] trimmedInputs = input.split("\\s+");

        if (trimmedInputs.length > 1) {
            String errorMessage =
                    String.format(Messages.COMPLETE_INDEX_OUT_OF_RANGE_FAILURE, trimmedInputs[1]);
            if (StringUtil.isNonZeroUnsignedInteger(trimmedInputs[1])) {
                int editIndex = Integer.parseInt(trimmedInputs[1]);
                if (editIndex > listSize) {
                    throw new CompletorException(errorMessage);
                }
            } else {
                throw new CompletorException(errorMessage);
            }
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(input, TASK_PREFIXES);
        boolean hasReminder = ParserUtil.arePrefixesPresent(argMultimap, PREFIX_REMINDER);
        boolean hasPriority = ParserUtil.arePrefixesPresent(argMultimap, PREFIX_PRIORITY);
        StringBuilder prefixesBuilder = new StringBuilder();

        for (int i = trimmedInputs.length - 1; i > 1; i--) {
            String currentArgument = trimmedInputs[i];
            if (Reminder.isValidReminder(currentArgument) && !hasReminder) {
                trimmedInputs[i] = CliSyntax.PREFIX_REMINDER.toString() + currentArgument;
                hasReminder = true;
                prefixesBuilder.append(CliSyntax.PREFIX_REMINDER.toString());
                prefixesBuilder.append(" ");
            } else if (Priority.isValidPriority(currentArgument) && !hasPriority) {
                trimmedInputs[i] = CliSyntax.PREFIX_PRIORITY.toString() + currentArgument;
                hasPriority = true;
                prefixesBuilder.append(CliSyntax.PREFIX_PRIORITY.toString());
                prefixesBuilder.append(" ");
            }
        }

        String newCommand = String.join(" ", trimmedInputs);
        String prefixesAdded = prefixesBuilder.length() == 0 ? "nil" : prefixesBuilder.toString();
        String feedbackToUser = String.format(Messages.COMPLETE_PREFIX_SUCCESS, prefixesAdded);

        return new CompletorResult(newCommand, feedbackToUser);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet =
                tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }
}
