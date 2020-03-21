package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import seedu.address.model.dayData.DayData;

/**
 * Wraps all data at the address-book level Duplicates are not allowed (by .isSameTask comparison)
 */
public class Statistics implements ReadOnlyStatistics {

    private final ArrayList<DayData> dayDataList;

    public Statistics() {
        dayDataList = new ArrayList<>();
    }

    /** Creates an DayDataList using the DayDatas in the {@code toBeCopied} */
    public Statistics(ReadOnlyStatistics toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations
    /**
     * Replaces the contents of the person list with {@code persons}. {@code persons} must not
     * contain duplicate persons.
     */
    public void setDayDatas(List<DayData> dayDataList) {
        this.dayDataList.clear();
        this.dayDataList.addAll(dayDataList);
    }

    /** Resets the existing data of this {@code TaskList} with {@code newData}. */
    public void resetData(ReadOnlyStatistics newData) {
        requireNonNull(newData);
        setDayDatas(newData.getDayDataList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasTask(DayData dayData) {
        requireNonNull(dayData);
        return dayDataList.contains(dayData);
    }

    /** Adds a person to the address book. The person must not already exist in the address book. */
    public void addDayData(DayData dayData) {
        dayDataList.add(dayData);
    }

    /**
     * Removes {@code key} from this {@code TaskList}. {@code key} must exist in the address book.
     */
    public void removeTask(DayData dayData) {
        dayDataList.remove(dayData);
    }

    //// util methods

    @Override
    public String toString() {
        return dayDataList.size() + " dayDats";
        // TODO: refine later
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Statistics // instanceof handles nulls
                        && dayDataList.equals(((Statistics) other).dayDataList));
    }

    @Override
    public List<DayData> getDayDataList() {
        return dayDataList;
    }
}
