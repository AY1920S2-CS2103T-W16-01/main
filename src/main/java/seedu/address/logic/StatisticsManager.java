package seedu.address.logic;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.dayData.CustomQueue.CONSTANT_SIZE;

import javafx.collections.ObservableList;
import seedu.address.model.Statistics;
import seedu.address.model.dayData.DayData;
import seedu.address.model.settings.DailyTarget;

/** Manages logic of StatisticsDisplay */
public class StatisticsManager {
    private Statistics statistics;
    private String progressDailyText;
    private String progressBarDailyFilepathString;

    public StatisticsManager() {}

    public void setStatistics(Statistics statistics) {
        requireNonNull(statistics);
        this.statistics = statistics;
    }

    /** Update StatisticsDisplay fields for user output. */
    public void updateStatisticsDisplayValues() {
        ObservableList<DayData> dayDatas = statistics.getCustomQueue();
        requireNonNull(statistics);
        requireNonNull(dayDatas);
        assert (dayDatas.size() > 0);

        // get daily challenge target
        DailyTarget dailyTarget = statistics.getDailyTarget();
        String dailyTargetString = dailyTarget.value;
        int currTarget = Integer.valueOf(dailyTargetString);

        // get current progress
        DayData latestDayData = dayDatas.get(CONSTANT_SIZE - 1);
        int currProgress = latestDayData.getPomDurationData().value;
        if (currProgress >= currTarget) {
            currProgress = currTarget;
        }
        this.progressDailyText = String.valueOf(currProgress);

        // calculate percentage bar
        int expBarPerc = (currProgress * 10) / currTarget;
        expBarPerc *= 10; // 0, 10, 20, 30...
        if (expBarPerc >= 100) {
            expBarPerc = 100;
        }

        progressBarDailyFilepathString = "/images/progress/ProgressBar" + expBarPerc + "%.png";
    }

    public void setDailyTargetText(String dailyTargetText) {
        requireNonNull(dailyTargetText);
        statistics.setDailyTarget(dailyTargetText);
    }

    public String getDailyTargetText() {
        return statistics.getDailyTarget().value;
    }

    public ObservableList<DayData> getCustomQueue() {
        return statistics.getCustomQueue();
    }

    public String getProgressDailyText() {
        return progressDailyText;
    }

    public String getProgressBarDailyFilepathString() {
        return progressBarDailyFilepathString;
    }
}
