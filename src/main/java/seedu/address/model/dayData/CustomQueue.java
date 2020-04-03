package seedu.address.model.dayData;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.dayData.exceptions.DayDataNotFoundException;
import seedu.address.model.dayData.exceptions.InvalidTableException;

/**
 * A list of dayDatas that enforces CONSTANT_SIZE, days must be continuous between its elements and
 * does not allow nulls. A dayData is considered unique by comparing using {@code
 * DayData#isSameDayData(DayData)}. As such, adding and updating of tasks uses
 * DayData#isSameDayData(DayData) for equality so as to ensure that the dayDaya being added or
 * updated is unique in terms of identity in the CustomQueue. However, the removal of a task uses
 * DayData#equals(Object) so as to ensure that the dayData with exactly the same fields will be
 * removed.
 *
 * <p>Supports a minimal set of list operations.
 *
 * @see DayData#isSameDayData(DayData)
 */
public class CustomQueue implements Iterable<DayData> {

    public static final String MESSAGE_CONSTRAINTS =
            "CONSTANT_SIZE enforced, days must be continuous";
    public static final int CONSTANT_SIZE = 7;

    private final ObservableList<DayData> internalList = FXCollections.observableArrayList();
    private final ObservableList<DayData> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /** Initialises empty DayData for past MAX_SIZE days */
    public void init() throws InvalidTableException {
        LocalDate currDate = LocalDate.now();
        this.init(currDate);
    }

    /**
     * Initialises empty DayData for past MAX_SIZE days starting from localDate.
     *
     * @param localDate localDate to start with.
     * @throws InvalidTableException
     */
    public void init(LocalDate localDate) throws InvalidTableException {
        LocalDate currDate = localDate;
        for (int i = CONSTANT_SIZE - 1; i >= 0; i--) {
            LocalDate tempLocalDate = currDate.minusDays(i);
            String tempLocalDateStr = tempLocalDate.toString();
            Date tempDate = new Date(tempLocalDateStr);
            DayData dayData = new DayData(tempDate);
            this.add(dayData);
        }

        if (!tableConstraintsAreEnforced(internalList)) {
            throw new InvalidTableException(CustomQueue.MESSAGE_CONSTRAINTS);
        }
    }

    /** reinitialises dayDataList to current day while retaining stored data. */
    public void updateDataDatesCustom() throws InvalidTableException {
        LocalDate todayLocalDate = LocalDate.now();
        this.updateDataDatesCustom(todayLocalDate);
    }

    public void updateDataDatesCustom(LocalDate localDate) throws InvalidTableException {
        DayData currDayData = this.getLatestDayData();
        LocalDate currLocalDate = currDayData.getDate().value;

        long daysBetween = DAYS.between(localDate, currLocalDate);
        if (daysBetween > CONSTANT_SIZE) {
            this.init();
        } else {
            while (!currLocalDate.equals(localDate)) { // keep adding new date from last date
                this.pop(); // poll oldest day from queue

                currLocalDate = currLocalDate.plusDays(1); // create new day LocalDate

                String currLocalDateStr = currLocalDate.toString(); // construct DayData
                Date tempDate = new Date(currLocalDateStr);
                DayData tempDayData = new DayData(tempDate);

                this.push(tempDayData); // add to queue

                assert (this.size() <= CONSTANT_SIZE);
            }
        }

        if (!tableConstraintsAreEnforced(internalList)) {
            throw new InvalidTableException(CustomQueue.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Auto-checks and replaces a new DayData object at the same Date
     *
     * @param dayData
     */
    public void updatesDayDataCustom(DayData dayData) throws DayDataNotFoundException {
        requireNonNull(dayData);

        Date currDate = dayData.getDate();
        for (int i = this.size() - 1; i >= 0; i--) {
            DayData currDayData = this.get(i);
            Date currDayDataDate = currDayData.getDate();
            if (currDayDataDate.equals(currDate)) { // correct date
                this.setDayData(currDayData, dayData); // replace currDayData with dayData
                return;
            }
        }

        throw new DayDataNotFoundException(); // dayData not found
    }

    /**
     * Gets DayData object at current date.
     *
     * @param date
     */
    public DayData getDayDataFromDateCustom(Date date) {
        requireNonNull(date);

        for (int i = this.size() - 1; i >= 0; i--) {
            DayData currDayData = this.get(i);
            Date currDayDataDate = currDayData.getDate();
            if (currDayDataDate.equals(date)) {
                return currDayData;
            }
        }

        throw new DayDataNotFoundException(); // dayData not found
    }

    /** Removes oldest DayData from head of the queue. */
    public void pop() {
        this.internalList.remove(0);
    }

    /**
     * Add Daydata to end of queue.
     *
     * @param dayData dayData to be added.
     */
    private void push(DayData dayData) throws InvalidTableException {
        this.add(dayData);
    }

    /** Identify latest DayData in the queue. */
    private DayData getLatestDayData() {
        return this.get(CONSTANT_SIZE - 1);
    }

    /** Returns true if the list contains an equivalent dayData as the given argument. */
    public boolean contains(DayData toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameDayData);
    }

    /** Adds a dayData to the end of the queue. */
    public void add(DayData toAdd)  {
        requireNonNull(toAdd);
        internalList.add(toAdd);
    }

    public void clear() {
        internalList.clear();
    }

    public int size() {
        return internalList.size();
    }

    public DayData get(int i) {
        return internalList.get(i);
    }

    /**
     * Replaces the dayData {@code target} in the list with {@code editedDayData}. {@code target}
     * must exist in the list. The dayData identity of {@code editedDayData} must not be the same as
     * another existing task in the list.
     */
    public void setDayData(DayData target, DayData editedDayData) {
        requireAllNonNull(target, editedDayData);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new DayDataNotFoundException();
        }

        internalList.set(index, editedDayData);
    }

    /** Removes the equivalent dayData from the list. The dayData must exist in the list. */
    public void remove(DayData toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new DayDataNotFoundException();
        }
    }

    /**
     * public void setDayDatas(CustomQueue replacement) { requireNonNull(replacement);
     * internalList.setAll(replacement.internalList); }
     */

    /**
     * Replaces the contents of this list with {@code dayDatas}. {@code dayDatas} must not break
     * table constraints
     */
    public void setDayDatas(List<DayData> dayDatas) throws InvalidTableException {
        requireAllNonNull(dayDatas);
        if (!tableConstraintsAreEnforced(dayDatas)) {
            throw new InvalidTableException(
                    CustomQueue.MESSAGE_CONSTRAINTS); // table is not size FIXED_SIZE
        }

        internalList.setAll(dayDatas);
    }

    public void setDayDatas(CustomQueue replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /** Returns the backing list as an unmodifiable {@code ObservableList}. */
    public ObservableList<DayData> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public String toString() {
        String temp = "";
        for (int i = 0; i < internalList.size(); i++) {
            temp += internalList.get(i).toString();
            temp += "\n";
        }
        return temp;
    }

    @Override
    public Iterator<DayData> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CustomQueue // instanceof handles nulls
                        && internalList.equals(((CustomQueue) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /** Returns true if {@code dayDatas} table constraints are enforced */
    public static boolean tableConstraintsAreEnforced(List<DayData> dayDatas) {
        boolean res = true;

        if (dayDatas.size() != CONSTANT_SIZE) {
            res = false; // table is not size CONSTANT_SIZE
        }

        try {
            DayData dayDataCheckPointer = dayDatas.get(0); // from list
            Date dateCheckPointer = dayDataCheckPointer.getDate();
            LocalDate localDateCheckPointer = dateCheckPointer.value;

            LocalDate currentLocalDate = null; // to check

            for (DayData dayData : dayDatas) {

                currentLocalDate = dayData.getDate().value;
                if (!localDateCheckPointer.equals(currentLocalDate)) {
                    res = false; // days must be continuous
                    break;
                }
                localDateCheckPointer = localDateCheckPointer.plusDays(1);
            }

        } catch (NullPointerException | IndexOutOfBoundsException e) {
            // TODO
        }

        return res;
    }
}
