package seedu.address.logic;

import static seedu.address.model.dayData.CustomQueue.CONSTANT_SIZE;

import javafx.collections.ObservableList;
import seedu.address.model.Statistics;
import seedu.address.model.dayData.DayData;

/** Manages logic of StatisticsDisplay */
public class StatisticsManager {
    private Statistics statistics;
    private String dailyTargetText;
    private String progressDailyText;
    private String progressBarDailyFilepathString;
    private ObservableList<DayData> dayDatas;

    public StatisticsManager() {}

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    /**
     * Update StatisticsDisplay fields for user output.
     *
     * @param dayDatas current dayDatas information.
     */
    public void updateStatisticsDisplayValues(ObservableList<DayData> dayDatas) {
        assert (statistics != null);

        this.dayDatas = dayDatas;
        DayData latestDayData = dayDatas.get(CONSTANT_SIZE - 1);
        int currProgress = latestDayData.getPomDurationData().value;

        String currTargetText = statistics.getDailyTarget();
        int currTarget = Integer.valueOf(currTargetText);
        if (currProgress >= currTarget) {
            currProgress = currTarget;
        }

        this.progressDailyText = String.valueOf(currProgress);
        this.dailyTargetText = String.valueOf(currTarget);

        int expBarPerc = (currProgress * 10) / currTarget;
        if (expBarPerc >= 10) {
            expBarPerc = 10;
        }

        switch (expBarPerc) {
            case 0:
                this.progressBarDailyFilepathString = "/images/progress/ProgressBar0%.png";
                break;
            case 1:
                this.progressBarDailyFilepathString = "/images/progress/ProgressBar10%.png";
                break;
            case 2:
                this.progressBarDailyFilepathString = "/images/progress/ProgressBar20%.png";
                break;
            case 3:
                this.progressBarDailyFilepathString = "/images/progress/ProgressBar30%.png";
                break;
            case 4:
                this.progressBarDailyFilepathString = "/images/progress/ProgressBar40%.png";
                break;
            case 5:
                this.progressBarDailyFilepathString = "/images/progress/ProgressBar50%.png";
                break;
            case 6:
                this.progressBarDailyFilepathString = "/images/progress/ProgressBar60%.png";
                break;
            case 7:
                this.progressBarDailyFilepathString = "/images/progress/ProgressBar70%.png";
                break;
            case 8:
                this.progressBarDailyFilepathString = "/images/progress/ProgressBar80%.png";
                break;
            case 9:
                this.progressBarDailyFilepathString = "/images/progress/ProgressBar90%.png";
                break;
            case 10:
                this.progressBarDailyFilepathString = "/images/progress/ProgressBar100%.png";
                break;
        }
    }

    public void setDailyTargetText(String dailyTargetText) {
        statistics.setDailyTarget(dailyTargetText);
    }

    public String getDailyTargetText() {
        return statistics.getDailyTarget();
    }

    public String getProgressDailyText() {
        return progressDailyText;
    }

    public String getProgressBarDailyFilepathString() {
        return progressBarDailyFilepathString;
    }

    public ObservableList<DayData> getCustomQueue() {
        return dayDatas;
    }
}
