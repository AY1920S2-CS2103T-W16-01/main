package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import javafx.collections.ObservableList;
import seedu.address.model.dayData.CustomQueue;
import seedu.address.model.dayData.Date;
import seedu.address.model.dayData.DayData;
import seedu.address.model.dayData.exceptions.DayDataNotFoundException;
import seedu.address.model.dayData.exceptions.InvalidTableException;

/** Wraps all DayData objects. */
public class Statistics implements ReadOnlyStatistics {

    private final CustomQueue customQueue;

    public Statistics() {
        customQueue = new CustomQueue();
        try {
            customQueue.init();
        } catch (InvalidTableException e) {
        }
    }

    /** Creates an DayDataList using the DayDatas in the {@code toBeCopied} */
    public Statistics(ReadOnlyStatistics toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /** Clears list */
    public void clearList() {
        this.customQueue.clear();
    }

    /** Replaces the contents of the list with {@code dayDataList}. */
    public void setDayDatas(List<DayData> dayDataList) {
        customQueue.clear();
        try {
            customQueue.setDayDatas(dayDataList);
        } catch (InvalidTableException e) {
        }
    }

    /** Resets the existing data of this {@code Statistics} with {@code newData}. */
    public void resetData(ReadOnlyStatistics newData) {
        requireNonNull(newData);
        setDayDatas(newData.getCustomQueue());
    }

    //// customQueue operations

    /** reinitialises dayDataList to current day while retaining stored data. */
    public void updateDataDates() {
        try {
            customQueue.updateDataDatesCustom();
        } catch (InvalidTableException e) {

        }
    }

    /**
     * Auto-checks and replaces a new DayData object at the same Date
     *
     * @param dayData
     */
    public void updatesDayData(DayData dayData) throws DayDataNotFoundException {
        try {
            customQueue.updatesDayDataCustom(dayData);
        } catch (DayDataNotFoundException | InvalidTableException e) {
        }
    }

    /**
     * Gets DayData object at current date.
     *
     * @param date
     */
    public DayData getDayDataFromDate(Date date) throws DayDataNotFoundException {
        try {
            return customQueue.getDayDataFromDateCustom(date);
        } catch (DayDataNotFoundException e) {
            return null;
        }
    }

    // util method
    /** Adds a dayData to the end of the internallist. */
    public void addDayData(DayData dayData) {
        customQueue.add(dayData);
    }

    @Override
    public String toString() {
        return customQueue.asUnmodifiableObservableList().size() + " dayDatas";
        // TODO: refine later
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Statistics // instanceof handles nulls
                        && customQueue.equals(((Statistics) other).customQueue));
    }

    @Override
    public ObservableList<DayData> getCustomQueue() {
        return customQueue.asUnmodifiableObservableList();
    }
}
