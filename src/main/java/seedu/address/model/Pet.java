package seedu.address.model;

import java.time.LocalDateTime;

public class Pet implements ReadOnlyPet {
    private static final String DEFAULT_NAME = "BB";
    private static final String DEFAULT_EXP = "0";
    private static final String DEFAULT_LEVEL = "1";
    private static final String DEFAULT_MOOD = "HAPPY";
    private static final String DEFAULT_LAST_DONE_TASK_TIME = LocalDateTime.now().toString();

    public final String HANGRY_MOOD_STRING = "HANGRY";
    public final String HAPPY_MOOD_STRING = "HAPPY";

    private String exp;
    private String level;
    private String name;
    private String mood;
    private String lastDoneTaskTime;

    public Pet(String name, String exp, String level, String mood, String lastDoneTaskTime) {

        this.name = name;
        this.exp = exp;
        this.level = level;
        this.mood = mood;
        this.lastDoneTaskTime = lastDoneTaskTime;
    }

    public Pet(ReadOnlyPet source) throws InvalidPetException {
        this(
                source.getName(),
                source.getExp(),
                source.getLevel(),
                source.getMood(),
                source.getLastDoneTaskTime());
    }

    public Pet() {
        this.name = DEFAULT_NAME;
        this.exp = DEFAULT_EXP;
        this.level = DEFAULT_LEVEL;
        this.mood = DEFAULT_MOOD;
        this.lastDoneTaskTime = DEFAULT_LAST_DONE_TASK_TIME;
    }

    public void setName(String name) {
        this.name = name;
    }

    // When a task is done, exp level increases by 5
    public void incrementExp() {
        int newExp = Integer.parseInt(this.exp) + 5;
        this.exp = Integer.toString(newExp);

        // update level
        int expInteger = Integer.parseInt(this.exp);
        int levelInteger = (int) Math.ceil(expInteger / 99.0);
        this.level = Integer.toString(levelInteger);
    }

    public void incrementPomExp() {
        int newExp = Integer.parseInt(this.exp) + 25;
        this.exp = Integer.toString(newExp);

        // update level
        int expInteger = Integer.parseInt(this.exp);
        int levelInteger = (int) Math.ceil(expInteger / 99.0);
        this.level = Integer.toString(levelInteger);
    }

    public void changeHangry() {
        this.mood = HANGRY_MOOD_STRING;
    }

    public void changeHappy() {
        this.mood = HAPPY_MOOD_STRING;
    }

    public void setLastDoneTaskTime(String lastDoneTaskTime) {
        this.lastDoneTaskTime = lastDoneTaskTime;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLevel() {
        return level;
    }

    @Override
    public String getExp() {
        return this.exp;
    }

    @Override
    public String getMood() {
        if (this.mood == null) {
            this.mood = DEFAULT_MOOD;
        }
        return this.mood;
    }

    @Override
    public String getLastDoneTaskTime() {
        if (this.lastDoneTaskTime == null) {
            this.lastDoneTaskTime = DEFAULT_LAST_DONE_TASK_TIME;
        }
        return this.lastDoneTaskTime;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else {
            if (other instanceof Pet) {
                Pet otherPet = (Pet) other;
                return (this.name.equals(otherPet.getName())
                        && this.level.equals(otherPet.getLevel())
                        && this.exp.equals(otherPet.getExp())
                        && this.mood.equals(otherPet.getMood())
                        && this.lastDoneTaskTime.equals(otherPet.getLastDoneTaskTime()));
            } else {
                return false;
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Hi I'm pet %s! my Exp is %s and my level is %s", name, exp, level);
    }
}
