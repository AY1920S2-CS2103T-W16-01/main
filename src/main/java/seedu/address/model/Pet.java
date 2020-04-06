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

    public String exp;
    public String level;
    public String name;
    public String mood;
    public String lastDoneTaskTime;

    public Pet(String name, String exp, String level, String mood, String lastDoneTaskTime) {
        this.name = name;
        this.exp = exp;
        this.level = level;
        this.mood = mood;
        this.lastDoneTaskTime = lastDoneTaskTime;
    }

    public Pet(ReadOnlyPet source) {
        this.exp = source.getExp();
        this.level = source.getLevel();
        this.name = source.getName();
        this.mood = source.getMood();
        this.lastDoneTaskTime = source.getLastDoneTaskTime();
    }

    public Pet() {
        this(DEFAULT_NAME, DEFAULT_EXP, DEFAULT_LEVEL, DEFAULT_MOOD, DEFAULT_LAST_DONE_TASK_TIME);
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
    public String toString() {
        return String.format("Hi I'm pet %s! my Exp is %s and my level is %s", name, exp, level);
    }
}
