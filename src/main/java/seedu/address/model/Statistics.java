package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.List;
import javafx.collections.ObservableList;
import seedu.address.model.dayData.CustomQueue;
import seedu.address.model.dayData.Date;
import seedu.address.model.dayData.DayData;
import seedu.address.model.dayData.exceptions.DayDataNotFoundException;
import seedu.address.model.dayData.exceptions.InvalidTableException;
import seedu.address.model.settings.DailyTarget;

/**
 * Stores information with respect to StatisticsDisplay. Wraps all DayData objects through
 * CustomQueue and stores daily target.
 */
public class Statistics implements ReadOnlyStatistics {

    public static final String DEFAULT_DAILY_TARGET = "100";

    private final CustomQueue customQueue;
    private DailyTarget dailyTarget;

    public Statistics() {
        customQueue = new CustomQueue();
        try {
            customQueue.init();
        } catch (InvalidTableException e) {
        }
        this.dailyTarget = new DailyTarget(DEFAULT_DAILY_TARGET);
    }

    /**
     * Initialises CustomQueue from localDate specified.
     *
     * @param localDate localDate in CustomQueue created.
     */
    public Statistics(LocalDate localDate) {
        customQueue = new CustomQueue();
        try {
            customQueue.init(localDate);
        } catch (InvalidTableException e) {
        }
        this.dailyTarget = new DailyTarget(DEFAULT_DAILY_TARGET);
    }

    /** Creates a Statistics using the DayDatas and dailyTarget in {@code toBeCopied}. */
    public Statistics(ReadOnlyStatistics toBeCopied) {
        this();
        resetData(toBeCopied);
        this.dailyTarget = toBeCopied.getDailyTarget();
    }

    //// daily challenge operations

    /**
     * Sets daily target to new value.
     *
     * @param dailyTargetValue dailyTarget to be set.
     */
    public void setDailyTarget(String dailyTargetValue) {
        requireNonNull(dailyTargetValue);
        this.dailyTarget = new DailyTarget(dailyTargetValue);
    }

    /**
     * Get current daily target value.
     *
     * @return current daily target value.
     */
    public DailyTarget getDailyTarget() {
        return dailyTarget;
    }

    //// list overwrite operations

    /** Replaces the contents of the list with {@code dayDatas}. */
    public void setDayDatas(List<DayData> dayDatas) {
        try {
            customQueue.setDayDatas(dayDatas);
        } catch (InvalidTableException e) {
        }
    }

    /** Resets the existing data of this {@code Statistics} with {@code newData}. */
    public void resetData(ReadOnlyStatistics newData) {
        requireNonNull(newData);
        setDayDatas(newData.getCustomQueue());
    }

    //// customQueue operations

    /** reinitialises dayDatas to current day while retaining stored data. */
    public void updateDataDates() {
        try {
            customQueue.updateDataDatesCustom();
        } catch (InvalidTableException e) {
        }
    }

    /**
     * Reinitialises dayDatas to localDate while retaining stored data.
     *
     * @param localDate localDate to be reset to.
     */
    public void updateDataDates(LocalDate localDate) {
        try {
            customQueue.updateDataDatesCustom(localDate);
        } catch (InvalidTableException e) {
        }
    }

    /**
     * Replaces the current DayData object from CustomQueue of the same date with a new DayData
     * object at the same Date.
     *
     * @param dayData new DayData object to replace.
     */
    public void updateDayData(DayData dayData) {
        try {
            customQueue.updateDayDataCustom(dayData);
        } catch (DayDataNotFoundException e) {
        }
    }

    /**
     * Gets DayData object at current date.
     *
     * @param date date to identify DayData object in customQueue.
     */
    public DayData getDayDataFromDate(Date date) {
        try {
            return customQueue.getDayDataFromDateCustom(date);
        } catch (DayDataNotFoundException e) {
            return null;
        }
    }

    // util methods

    /**
     * Pops oldest dayData at head of the customQueue and adds a dayData to the end of the
     * customQueue.
     *
     * @param dayData dayData to be added.
     */
    public void update(DayData dayData) {
        customQueue.pop();
        customQueue.add(dayData);
    }

    @Override
    public String toString() {
        return "Statistics: " + customQueue.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Statistics)) {
            return false;
        }

        Statistics otherStatistics = (Statistics) other;
        return getCustomQueue().equals(otherStatistics.getCustomQueue())
                && getDailyTarget().equals(otherStatistics.getDailyTarget());
    }

    @Override
    public ObservableList<DayData> getCustomQueue() {
        return customQueue.asUnmodifiableObservableList();
    }
}
