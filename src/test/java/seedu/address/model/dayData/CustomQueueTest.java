package seedu.address.model.dayData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.storage.JsonAdaptedDayDataTest.VALID_POM_DURATION_DATA;
import static seedu.address.storage.JsonAdaptedDayDataTest.VALID_TASKS_DONE_DATA;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalDayDatas.DAY0;
import static seedu.address.testutil.TypicalDayDatas.DAYNEW;
import static seedu.address.testutil.TypicalDayDatas.DAYNEW2;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import seedu.address.model.dayData.exceptions.DayDataNotFoundException;
import seedu.address.model.dayData.exceptions.InvalidTableException;
import seedu.address.testutil.DayDataBuilder;

public class CustomQueueTest {

    private CustomQueue customQueue = new CustomQueue();
    private LocalDate TYPICAL_STATISTICS_LATEST_LOCAL_DATE = LocalDate.parse("2020-03-23");
    private LocalDate VALID_LOCAL_DATE = LocalDate.parse("2018-11-13");

    @Test
    public void updateDataDatesCustom_success() throws InvalidTableException {
        customQueue.init(VALID_LOCAL_DATE);
        customQueue.updateDataDatesCustom(TYPICAL_STATISTICS_LATEST_LOCAL_DATE);

        CustomQueue expectedCustomQueue = new CustomQueue();
        expectedCustomQueue.init(TYPICAL_STATISTICS_LATEST_LOCAL_DATE);
        assertEquals(expectedCustomQueue, customQueue);
    }

    @Test
    public void updateDataDatesCustom_nullLocalDatethrowsNullPointerException()
            throws InvalidTableException {
        customQueue.init(VALID_LOCAL_DATE);
        assertThrows(NullPointerException.class, () -> customQueue.updateDataDatesCustom(null));
    }

    @Test
    public void updateDayDataCustom_nullLocalDatethrowsNullPointerException()
            throws InvalidTableException {
        customQueue.init(VALID_LOCAL_DATE);
        assertThrows(NullPointerException.class, () -> customQueue.updateDayDataCustom(null));
    }

    @Test
    public void updateDayDataCustom_nonexistentDayDatathrowsDayDataNotFoundException()
            throws InvalidTableException {
        customQueue.init(VALID_LOCAL_DATE);
        assertThrows(DayDataNotFoundException.class, () -> customQueue.updateDayDataCustom(DAYNEW));
    }

    @Test
    public void getDayDataFromDateCustom_nullLocalDatethrowsNullPointerException()
            throws InvalidTableException {
        customQueue.init(VALID_LOCAL_DATE);
        assertThrows(NullPointerException.class, () -> customQueue.getDayDataFromDateCustom(null));
    }

    @Test
    public void getDayDataFromDateCustom_nonexistentDayDatathrowsDayDataNotFoundException()
            throws InvalidTableException {
        customQueue.init(VALID_LOCAL_DATE);
        assertThrows(
                DayDataNotFoundException.class,
                () -> customQueue.getDayDataFromDateCustom(DAYNEW.getDate()));
    }

    @Test
    public void getDayDataFromDateCustom_validDayData_returnsDayData()
            throws InvalidTableException {
        customQueue.init(TYPICAL_STATISTICS_LATEST_LOCAL_DATE);
        DayData day0Empty =
                new DayDataBuilder(DAY0).withPomDurationData("0").withTasksDoneData("0").build();
        assertEquals(day0Empty, customQueue.getDayDataFromDateCustom(day0Empty.getDate()));
    }

    @Test
    public void add_nullDayData_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> customQueue.add(null));
    }

    @Test
    public void setDayData_nullTargetDayData_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> customQueue.setDayData(null, DAYNEW));
    }

    @Test
    public void setDayData_nullEditedDayDatan_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> customQueue.setDayData(DAYNEW, null));
    }

    @Test
    public void setDayData_targetDayDataNotInList_throwsDayDataNotFoundException() {
        assertThrows(DayDataNotFoundException.class, () -> customQueue.setDayData(DAYNEW, DAYNEW));
    }

    @Test
    public void setDayData_editedDayDataIsSameDayData_success() throws InvalidTableException {
        customQueue.init(TYPICAL_STATISTICS_LATEST_LOCAL_DATE);
        customQueue.pop();

        customQueue.add(DAYNEW);
        customQueue.setDayData(DAYNEW, DAYNEW);

        CustomQueue expectedCustomQueue = new CustomQueue();
        expectedCustomQueue.init(TYPICAL_STATISTICS_LATEST_LOCAL_DATE);
        expectedCustomQueue.pop();

        expectedCustomQueue.add(DAYNEW);
        assertEquals(expectedCustomQueue, customQueue);
    }

    @Test
    public void setDayData_editedDayDataHasSameIdentity_success() throws InvalidTableException {
        customQueue.init(TYPICAL_STATISTICS_LATEST_LOCAL_DATE);
        customQueue.pop();

        customQueue.add(DAYNEW);
        DayData editedDayData =
                new DayDataBuilder(DAYNEW)
                        .withPomDurationData(VALID_POM_DURATION_DATA)
                        .withTasksDoneData(VALID_TASKS_DONE_DATA)
                        .build();
        customQueue.setDayData(DAYNEW, editedDayData);

        CustomQueue expectedCustomQueue = new CustomQueue();
        expectedCustomQueue.init(TYPICAL_STATISTICS_LATEST_LOCAL_DATE);
        expectedCustomQueue.pop();

        expectedCustomQueue.add(editedDayData);
        assertEquals(expectedCustomQueue, customQueue);
    }

    @Test
    public void setDayData_editedDayDataHasDifferentIdentity_success()
            throws InvalidTableException {
        customQueue.init(TYPICAL_STATISTICS_LATEST_LOCAL_DATE);
        customQueue.pop();

        customQueue.add(DAYNEW);
        customQueue.setDayData(DAYNEW, DAYNEW2);

        CustomQueue expectedCustomQueue = new CustomQueue();
        expectedCustomQueue.init(TYPICAL_STATISTICS_LATEST_LOCAL_DATE);
        expectedCustomQueue.pop();

        expectedCustomQueue.add(DAYNEW2);
        assertEquals(expectedCustomQueue, customQueue);
    }

    @Test
    public void setDayDatas_nullCustomQueue_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> customQueue.setDayDatas((CustomQueue) null));
    }

    @Test
    public void setDayDatas_customQueue_replacesOwnListWithProvidedCustomQueue()
            throws InvalidTableException {
        customQueue.init(TYPICAL_STATISTICS_LATEST_LOCAL_DATE);
        customQueue.pop();

        customQueue.add(DAYNEW);

        CustomQueue expectedCustomQueue = new CustomQueue();
        expectedCustomQueue.init(TYPICAL_STATISTICS_LATEST_LOCAL_DATE);
        expectedCustomQueue.pop();

        expectedCustomQueue.add(DAYNEW);
        customQueue.setDayDatas(expectedCustomQueue);
        assertEquals(expectedCustomQueue, customQueue);
    }

    @Test
    public void setDayDatas_nullList_throwsNullPointerException() {
        assertThrows(
                NullPointerException.class, () -> customQueue.setDayDatas((List<DayData>) null));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(
                UnsupportedOperationException.class,
                () -> customQueue.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void tableConstraintsAreEnforced_validTable_returnsTrue() throws InvalidTableException {
        customQueue.init(TYPICAL_STATISTICS_LATEST_LOCAL_DATE);
        assertTrue(
                customQueue.tableConstraintsAreEnforced(
                        customQueue.asUnmodifiableObservableList()));
    }

    @Test
    public void tableConstraintsAreEnforced_invalidTable_returnsFalse()
            throws InvalidTableException {
        customQueue.init(TYPICAL_STATISTICS_LATEST_LOCAL_DATE);
        customQueue.add(DAY0);
        assertFalse(
                customQueue.tableConstraintsAreEnforced(
                        customQueue.asUnmodifiableObservableList()));
    }
}
