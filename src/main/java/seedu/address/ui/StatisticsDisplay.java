package seedu.address.ui;

import static seedu.address.model.dayData.CustomQueue.CONSTANT_SIZE;

import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.dayData.DayData;

/** An UI component that displays the Pomodoro {@code Pomodoro}. */
public class StatisticsDisplay extends UiPart<Region> {

    private static final String FXML = "StatisticsDisplay.fxml";
    private static final String DEFAULT_PROGRESS_BAR_DAILY_PLACEHOLDER =
            "/images/progress/ProgressBar0%.png";
    private static final String DEFAULT_PROGRESS_DAILY = "NaN";
    private static final String DEFAULT_PROGRESS_TARGET = "100";
    private final String PROGRESS_UNITS = " mins";

    public String progressBarDailyFilepathString;
    public String progressDailyText;
    public String progressTargetText;

    @FXML private VBox statisticsPane;
    @FXML private BarChart<String, Integer> barChartPomDurationData;
    @FXML private BarChart<String, Integer> barChartTasksDoneData;

    @FXML private Label progressDaily;
    @FXML private ImageView progressBarDaily;
    @FXML private Label progressTarget;

    public StatisticsDisplay() {
        super(FXML);
        this.progressBarDailyFilepathString = DEFAULT_PROGRESS_BAR_DAILY_PLACEHOLDER;
        this.progressDailyText = DEFAULT_PROGRESS_DAILY;
        this.progressTargetText = DEFAULT_PROGRESS_TARGET;

        progressDaily.setText(progressDailyText + PROGRESS_UNITS);
        Image progressBarDailyImage = new Image(DEFAULT_PROGRESS_BAR_DAILY_PLACEHOLDER);
        progressBarDaily.setImage(progressBarDailyImage);
        progressTarget.setText(progressTargetText + PROGRESS_UNITS);
    }

    public void updateGraphs(List<DayData> customQueue) {
        XYChart.Series<String, Integer> dataSeriesPomDurationData = new XYChart.Series<>();
        XYChart.Series<String, Integer> dataSeriesTasksDoneData = new XYChart.Series<>();

        for (int i = CONSTANT_SIZE - 1; i >= 0; i--) {
            String dateString = customQueue.get(i).getDate().toPrint();

            int pomDurationDataInt = customQueue.get(i).getPomDurationData().value;
            dataSeriesPomDurationData
                    .getData()
                    .add(new XYChart.Data<>(dateString, pomDurationDataInt));

            int tasksDoneDataInt = customQueue.get(i).getTasksDoneData().value;
            dataSeriesTasksDoneData.getData().add(new XYChart.Data<>(dateString, tasksDoneDataInt));
        }

        barChartPomDurationData.getData().clear();
        barChartTasksDoneData.getData().clear();

        barChartPomDurationData.getData().add(dataSeriesPomDurationData);
        barChartTasksDoneData.getData().add(dataSeriesTasksDoneData);

        dataSeriesPomDurationData.getChart().getXAxis().setAnimated(false);
        dataSeriesTasksDoneData.getChart().getXAxis().setAnimated(false);
        dataSeriesPomDurationData.getChart().setAnimated(false);
        dataSeriesTasksDoneData.getChart().setAnimated(false);
    }

    public void setProgressTargetText(String progressTargetText) {
        this.progressTargetText = progressTargetText;
        progressTarget.setText(progressTargetText + PROGRESS_UNITS);
    }

    public void setProgressDailyText(String progressDailyText) {
        this.progressDailyText = progressDailyText;
        progressDaily.setText(progressDailyText + PROGRESS_UNITS);
    }

    public void setProgressBarDailyFilepathString(String progressBarDailyFilepathString) {
        this.progressBarDailyFilepathString = progressBarDailyFilepathString;
        Image progressBarDailyImage = new Image(progressBarDailyFilepathString);
        progressBarDaily.setImage(progressBarDailyImage);
    }
}
