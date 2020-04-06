package seedu.address.model.task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TimerTask;
import java.util.Timer;
import javafx.application.Platform;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TASKS;

import seedu.address.logic.LogicManager;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;
import seedu.address.storage.Storage;

public class Recurring {
   private final RecurType type;
   private final LocalDateTime referenceDateTime;

   public static final String MESSAGE_CONSTRAINTS = "Recurring should be in the format d or w, for eg: rec/d";
   public static final String MESSAGE_RECURRING_TASK_SUCCESS = "Recurring Task: %1$s";

   public static final String VALIDATION_REGEX = "[dw]"; 
   public static final DateTimeFormatter stringFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yy@HH:mm");

   public Recurring(String recurringStringStorage) throws ParseException {
        String recurTypeString = recurringStringStorage.substring(0,1);
        String dateTimeString = recurringStringStorage.substring(1);
        this.type = parseRecurType(recurTypeString);
        this.referenceDateTime = parseDateTime(dateTimeString);
   }

   public Recurring(String recurringString, LocalDateTime referenceDateTime) throws ParseException{
       this.type = parseRecurType(recurringString);
       this.referenceDateTime = referenceDateTime;
   }

   public LocalDateTime parseDateTime(String dateTimeString) {
       return stringFormatter.parse(dateTimeString, LocalDateTime::from);
   }

   public RecurType parseRecurType(String recurringString) throws ParseException {
       if (recurringString.equals("d")) {
           return RecurType.DAILY;
       } else if (recurringString.equals("w")) {
           return RecurType.WEEKLY;
       } else {
           throw new ParseException(Recurring.MESSAGE_CONSTRAINTS);
       }
   }
   
   /**
    * Returns a copy of the task that is being reset with everything being the same except for whether the method is Done
    * @param taskToReset
    * @return copied task with done set to undone
    */
   public Task resetDone(Task taskToReset) {
       assert taskToReset != null;

        Name updatedName = taskToReset.getName();
        Priority updatedPriority = taskToReset.getPriority();
        Description updatedDescription = taskToReset.getDescription();
        Set<Tag> updatedTags = taskToReset.getTags();
        Optional<Reminder> sameOptReminder = taskToReset.getOptionalReminder();
        Optional<Recurring> sameOptRecurring = taskToReset.getOptionalRecurring();
        return new Task(
                updatedName, updatedPriority, updatedDescription, new Done("N"), updatedTags, sameOptReminder, sameOptRecurring);
   }

   /** Returns true if a given string is a valid name. */
   public static boolean isValidRecurring(String test) {
       return test.matches(VALIDATION_REGEX);
   }
   
   /**
    * Returns Daily or Weekly for display on the card.
    */
   public String displayRecurring() {
       String result = type.name().substring(0, 1) + type.name().substring(1).toLowerCase();
       return result;
   } 


   public TimerTask generateTimerTask(Model model, Task taskToReset) {    
    
    return new TimerTask(){
        @Override
        public void run() {
            Platform.runLater(() -> {
                requireNonNull(model);

                Task resetedTask = resetDone(taskToReset);

                model.setTask(taskToReset, resetedTask);
                model.updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
                }
            );
            
        }
    };
   }

   public void triggerRecurring(Model model, Task taskToReset) {
       TimerTask repeatedTask = generateTimerTask(model, taskToReset);
       Timer timer = new Timer("Timer");
       long period = getInterval();
       long delayToFirstTrigger = Duration.between(LocalDateTime.now(), referenceDateTime).getSeconds();
       delayToFirstTrigger = delayToFirstTrigger >= 0 ? delayToFirstTrigger*1000 : 0;
       timer.scheduleAtFixedRate(repeatedTask, delayToFirstTrigger, period); //might run twice in the first time
   }
   
   public long getInterval() {
       long period = 0;
       if (type == RecurType.DAILY) {
           period = 1000l*60*60*24;
        // period = 12000l;  //for testing      
       } else if (type == RecurType.WEEKLY) {
           period = 1000l*60*60*24*7;
       }
       return period;
   }

   @Override
   public String toString() {
       String typeString = type.name().substring(0, 1).toLowerCase();
       String dateTimeString = referenceDateTime.format(stringFormatter);
       return typeString + dateTimeString;
   }


}