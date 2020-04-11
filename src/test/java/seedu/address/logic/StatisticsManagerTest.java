package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.model.Statistics.DEFAULT_DAILY_TARGET;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalDayDatas.DAY6;
import static seedu.address.testutil.TypicalDayDatas.getTypicalStatistics;

import org.junit.jupiter.api.Test;
import seedu.address.model.Statistics;

public class StatisticsManagerTest {

    StatisticsManager statisticsManager = new StatisticsManager();

    @Test
    public void setStatistics_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> statisticsManager.setStatistics(null));
    }

    @Test
    public void updateStatisticsDisplayValues_typicalDayDatas_success() {
        statisticsManager.setStatistics(new Statistics(getTypicalStatistics()));
        statisticsManager.updateStatisticsDisplayValues();

        String expectedDailyTargetText = DEFAULT_DAILY_TARGET;

        Integer dataPomDurationData = DAY6.getPomDurationData().value;
        Integer expectedProgressDaily = dataPomDurationData >= 100 ? 100 : dataPomDurationData;
        String expectedProgressDailyText = String.valueOf(expectedProgressDaily);

        Integer expectedPercentage = (expectedProgressDaily / 10) * 10;
        String expectedProgressBarDailyFilepathString =
                "/images/progress/ProgressBar" + expectedPercentage + "%.png";

        assertEquals(statisticsManager.getDailyTargetText(), expectedDailyTargetText);
        assertEquals(statisticsManager.getProgressDailyText(), expectedProgressDailyText);
        assertEquals(
                statisticsManager.getProgressBarDailyFilepathString(),
                expectedProgressBarDailyFilepathString);
    }

    @Test
    public void updateStatisticsDisplayValues_nullStatistics_throwsNullPointerException() {
        assertThrows(
                NullPointerException.class,
                () -> statisticsManager.updateStatisticsDisplayValues());
    }

    @Test
    public void setDailyTargetText_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> statisticsManager.setDailyTargetText(null));
    }
}
