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
 * A list of dayDatas that enforces the following table constraints: 1. Size of CustomQueue must be
 * of CONSTANT_SIZE after each method call through Statistics. 2. DayData days in CustomQueue must
 * be continuous between its elements.
 *
 * <p>Supports a minimal set of operations.
 */
public class CustomQueue implements Iterable<DayData> {

    public static final String MESSAGE_CONSTRAINTS =
            "Size of CustomQueue must be of CONSTANT_SIZE, DayData days in CustomQueue must be continuous between its elements.";
    public static final int CONSTANT_SIZE = 7;

    private final ObservableList<DayData> internalList = FXCollections.observableArrayList();
    private final ObservableList<DayData> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /** Initialises empty DayDatas for past {@code MAX_SIZE} days from today. */
    public void init() throws InvalidTableException {
        LocalDate currDate = LocalDate.now();
        this.init(currDate);
    }

    /**
     * Initialises empty DayDatas for past {@code MAX_SIZE} days from a localDate specified.
     *
     * @param localDate latest localDate to start with.
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

    /** Reinitialises dayDataList to current day while retaining stored data. */
    public void updateDataDatesCustom() throws InvalidTableException {
        LocalDate todayLocalDate = LocalDate.now();
        this.updateDataDatesCustom(todayLocalDate);
    }

    /**
     * Reinitialises dayDataList to current day while retaining stored data.
     *
     * @param localDate latest localDate to start with.
     * @throws InvalidTableException
     */
    public void updateDataDatesCustom(LocalDate localDate) throws InvalidTableException {
        DayData currDayData = this.getLatestDayData();
        LocalDate currLocalDate = currDayData.getDate().value;

        long daysBetween = DAYS.between(localDate, currLocalDate);
        if (daysBetween > CONSTANT_SIZE) {
            this.init();
        } else {
            while (!currLocalDate.equals(localDate)) { // keep adding new date from last date
                this.pop(); // pop oldest day from head of queue

                currLocalDate = currLocalDate.plusDays(1); // create new day LocalDate

                String currLocalDateStr = currLocalDate.toString(); // construct DayData
                Date tempDate = new Date(currLocalDateStr);
                DayData tempDayData = new DayData(tempDate);

                this.add(tempDayData); // add to queue

                assert (internalList.size() <= CONSTANT_SIZE);
            }
        }

        if (!tableConstraintsAreEnforced(internalList)) {
            throw new InvalidTableException(CustomQueue.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Replaces the current DayData object from CustomQueue of the same date with a new DayData
     * object at the same Date.
     *
     * @param dayData new DayData object to replace.
     */
    public void updateDayDataCustom(DayData dayData) throws DayDataNotFoundException {
        requireNonNull(dayData);

        Date currDate = dayData.getDate();
        for (int i = internalList.size() - 1; i >= 0; i--) {
            DayData currDayData = internalList.get(i);
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
     * @param date date to identify DayData object in internalList.
     */
    public DayData getDayDataFromDateCustom(Date date) {
        requireNonNull(date);

        for (int i = internalList.size() - 1;
                i >= 0;
                i--) { // Iterate from latest data as it is accessed most often.
            DayData currDayData = internalList.get(i);
            Date currDayDataDate = currDayData.getDate();
            if (currDayDataDate.equals(date)) {
                return currDayData;
            }
        }

        throw new DayDataNotFoundException(); // dayData not found
    }

    /** Adds a dayData to the end of the queue. */
    public void add(DayData toAdd) {
        requireNonNull(toAdd);
        internalList.add(toAdd);
    }

    /** Removes oldest DayData from head of the queue. */
    public void pop() {
        assert (internalList.size() > 0);
        internalList.remove(0);
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

    /**
     * Replaces the contents of this list with {@code dayDatas}. {@code dayDatas} must not break
     * table constraints
     */
    public void setDayDatas(List<DayData> dayDatas) throws InvalidTableException {
        requireAllNonNull(dayDatas);
        if (!tableConstraintsAreEnforced(dayDatas)) {
            throw new InvalidTableException(CustomQueue.MESSAGE_CONSTRAINTS);
        }

        internalList.setAll(dayDatas);
    }

    /**
     * Replaces the contents of this list with {@code replacement}'s internalList.
     *
     * @param replacement CustomQueue's internalList to replace current internalList.
     */
    public void setDayDatas(CustomQueue replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /** Returns the internalList as an unmodifiable {@code ObservableList}. */
    public ObservableList<DayData> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    /** Get latest dayData in the internalList. */
    private DayData getLatestDayData() {
        return internalList.get(CONSTANT_SIZE - 1);
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

    /** Returns true if {@code internalList} table constraints are enforced. */
    public static boolean tableConstraintsAreEnforced(List<DayData> dayDatas) {
        if (dayDatas == null) {
            return false;
        }

        if (dayDatas.size() != CONSTANT_SIZE) {
            return false; // table is not size {@code CONSTANT_SIZE}.
        }

        DayData dayDataCheckPointer = dayDatas.get(0); // from list
        Date dateCheckPointer = dayDataCheckPointer.getDate();
        LocalDate localDateCheckPointer = dateCheckPointer.value;

        for (DayData dayData : dayDatas) {
            Date currentDate = dayData.getDate();
            LocalDate currentLocalDate = currentDate.value;

            if (!localDateCheckPointer.equals(currentLocalDate)) {
                return false; // days in dayDatas are not continuous
            }

            localDateCheckPointer = localDateCheckPointer.plusDays(1);
        }

        return true;
    }
}
