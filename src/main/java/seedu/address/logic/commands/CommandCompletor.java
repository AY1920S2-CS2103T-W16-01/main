package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Represents a command with hidden internal logic and the ability to be executed. */
public class CommandCompletor {
    private ArrayList<String> commands = new ArrayList<>();

    public CommandCompletor() {
        this.commands.add(AddCommand.COMMAND_WORD);
        this.commands.add(EditCommand.COMMAND_WORD);
        this.commands.add(DoneCommand.COMMAND_WORD);
        this.commands.add(DeleteCommand.COMMAND_WORD);
        this.commands.add(FindCommand.COMMAND_WORD);
        this.commands.add(ClearCommand.COMMAND_WORD);
        this.commands.add(PomCommand.COMMAND_WORD);
        this.commands.add(ExitCommand.COMMAND_WORD);
    }

    public String getSuggestedCommand(String input) {
        String[] trimmedInputWords =
                input.toLowerCase()
                        .split("\\s+"); // get the first command, autoComplete based on that.

        if (trimmedInputWords.length > 0) {
            for (String commandWord : this.commands) {
                Pattern commandPattern = Pattern.compile(String.format("^%s", commandWord));
                Matcher commandMatcher = commandPattern.matcher(trimmedInputWords[0]);
                if (commandMatcher
                        .matches()) { // need to check for match before we can check for hitEnd
                    break; // command found then return original
                }
                if (commandMatcher.hitEnd()) {
                    trimmedInputWords[0] = commandWord;
                    return String.join(" ", trimmedInputWords);
                }
            }
        }
        // if no matches, return original
        return input;
    }

    public String getSuccessMessage() {
        return "Command auto completed!";
    }

    public String getFailureMessage() {
        return "No command auto complete found :(";
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CommandCompletor)) {
            return false;
        } else {
            CommandCompletor otherCommandCompletor = (CommandCompletor) other;
            return otherCommandCompletor.commands.equals(this.commands);
        }
    }
}
