package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedDayDataTest.VALID_POM_DURATION_DATA;
import static seedu.address.storage.JsonAdaptedDayDataTest.VALID_TASKS_DONE_DATA;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalDayDatas.DAY0;
import static seedu.address.testutil.TypicalDayDatas.DAYNEW;
import static seedu.address.testutil.TypicalDayDatas.getTypicalDayDatas;
import static seedu.address.testutil.TypicalDayDatas.getTypicalStatistics;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import seedu.address.model.dayData.DayData;
import seedu.address.testutil.DayDataBuilder;

public class StatisticsTest {
    private final Statistics statistics = new Statistics();

    @Test
    public void setDatas_success() {
        Statistics expectedStatistics = getTypicalStatistics();
        statistics.setDayDatas(getTypicalDayDatas());
        assertEquals(expectedStatistics, statistics);
    }

    @Test
    public void setDatas_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> statistics.setDayDatas(null));
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> statistics.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyStatistics_replacesData() {
        Statistics newData = getTypicalStatistics();
        statistics.resetData(newData);
        assertEquals(newData, statistics);
    }

    @Test
    public void updateDataDates_nextDay_success() {
        Statistics statistics = getTypicalStatistics();
        statistics.updateDataDates(DAYNEW.getDate().value);

        Statistics expectedStatistics = getTypicalStatistics();
        expectedStatistics.pop();
        expectedStatistics.addDayData(
                new DayDataBuilder(DAYNEW).withPomDurationData("0").withTasksDoneData("0").build());
        assertEquals(expectedStatistics, statistics);
    }

    @Test
    public void updateDataDates_manyDaysLater_success() { // many days > CONSTANT_SIZE
        Statistics statistics = getTypicalStatistics();
        LocalDate dayManyDaysLater = DAYNEW.getDate().value.plusDays(50);
        statistics.updateDataDates(dayManyDaysLater);

        Statistics expectedStatistics = new Statistics(dayManyDaysLater);
        assertEquals(expectedStatistics, statistics);
    }

    @Test
    public void updatesDayData_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> statistics.updatesDayData(null));
    }

    @Test
    public void getDayDataFromDate_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> statistics.getDayDataFromDate(null));
    }

    @Test
    public void getDayDataFromDate_dayDataNotInStatistics_returnsNull() {
        assertEquals(null, statistics.getDayDataFromDate(DAYNEW.getDate()));
    }

    @Test
    public void getDayDataFromDate_dayDataInStatistics_returnsTrue() {
        Statistics statisticsTypical = getTypicalStatistics();
        assertEquals(DAY0, statisticsTypical.getDayDataFromDate(DAY0.getDate()));
    }

    @Test
    public void getDayDataFromDate_dayDataWithSameIdentityFieldsInStatistics_returnsTrue() {
        Statistics statisticsTypical = getTypicalStatistics();
        DayData editedDayData =
                new DayDataBuilder(DAY0)
                        .withPomDurationData(VALID_POM_DURATION_DATA)
                        .withTasksDoneData(VALID_TASKS_DONE_DATA)
                        .build();
        statisticsTypical.updatesDayData(editedDayData);
        assertEquals(DAY0, statisticsTypical.getDayDataFromDate(DAY0.getDate()));
    }

    @Test
    public void getCustomQueue_modifyList_throwsUnsupportedOperationException() {
        assertThrows(
                UnsupportedOperationException.class, () -> statistics.getCustomQueue().remove(0));
    }
}
