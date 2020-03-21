package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/** Represents a command with hidden internal logic and the ability to be executed. */
public class CommandCompletor {

    public CommandCompletor() {

    }

    public String getSuggestedCommand(String input) {
        String[] trimmedInput = input.split("//s+"); // get the first command, autoComplete based on that. 

        if (trimmedInput.length > 0) {
            
        }
        
        
        // we could potentially add tags for users automatically 
        
        return "a";
    }

    
}
