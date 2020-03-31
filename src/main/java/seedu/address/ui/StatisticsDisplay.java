package seedu.address.ui;

import static seedu.address.model.dayData.CustomQueue.CONSTANT_SIZE;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.dayData.CustomQueue;
import seedu.address.model.dayData.DayData;

/** An UI component that displays the Pomodoro {@code Pomodoro}. */
public class StatisticsDisplay extends UiPart<Region> {

    private static final String FXML = "StatisticsDisplay.fxml";
    private static final String DEFAULT_PROGRESS_BAR_DAILY_PLACEHOLDER =
            "/images/pet/ProgressBar0%.png";
    private static final String DEFAULT_PROGRESS_DAILY = "NaN mins";
    private static final String DEFAULT_MEDALS = "Placeholder";
    private static final String X_AXIS = "Day";

    private final String PROGRESS_UNITS = " mins";

    public String progressBarDailyFilepathString;
    public String progressDailyText;
    public String medalsText;
    public String progressTarget; // TODO @Fyonn will set

    @FXML private VBox statisticsPane;
    @FXML private BarChart<String, Integer> barChartPomDurationData;
    @FXML private BarChart<String, Integer> barChartTasksDoneData;

    @FXML private Label progressDaily;
    @FXML private ImageView progressBarDaily;
    @FXML private Label medals;

    public StatisticsDisplay() {
        super(FXML);
        this.progressBarDailyFilepathString = DEFAULT_PROGRESS_BAR_DAILY_PLACEHOLDER;

        this.progressDailyText = DEFAULT_PROGRESS_DAILY;
        this.medalsText = DEFAULT_MEDALS;

        progressDaily.setText(progressDailyText);
        Image progressBarDailyImage = new Image(DEFAULT_PROGRESS_BAR_DAILY_PLACEHOLDER);
        progressBarDaily.setImage(progressBarDailyImage);
        medals.setText(medalsText);

        CategoryAxis xAxis1 = new CategoryAxis();
        xAxis1.setLabel(X_AXIS);
        NumberAxis yAxis1 = new NumberAxis();

        CategoryAxis xAxis2 = new CategoryAxis();
        xAxis2.setLabel(X_AXIS);
        NumberAxis yAxis2 = new NumberAxis();
    }

    public void updateGraphs(CustomQueue customQueue) {
        DayData latestDayData = customQueue.get(CONSTANT_SIZE - 1);
        int currProgress = latestDayData.getPomDurationData().value;
        progressDaily.setText(currProgress + PROGRESS_UNITS);

        int expBarPerc = currProgress / 10;
        if (expBarPerc >= 10) {
            expBarPerc = 10;
        }

        switch (expBarPerc) {
            case 0:
                this.progressBarDailyFilepathString = "/images/pet/ProgressBar0%.png";
                break;
            case 1:
                this.progressBarDailyFilepathString = "/images/pet/ProgressBar10%.png";
                break;
            case 2:
                this.progressBarDailyFilepathString = "/images/pet/ProgressBar20%.png";
                break;
            case 3:
                this.progressBarDailyFilepathString = "/images/pet/ProgressBar30%.png";
                break;
            case 4:
                this.progressBarDailyFilepathString = "/images/pet/ProgressBar40%.png";
                break;
            case 5:
                this.progressBarDailyFilepathString = "/images/pet/ProgressBar50%.png";
                break;
            case 6:
                this.progressBarDailyFilepathString = "/images/pet/ProgressBar60%.png";
                break;
            case 7:
                this.progressBarDailyFilepathString = "/images/pet/ProgressBar70%.png";
                break;
            case 8:
                this.progressBarDailyFilepathString = "/images/pet/ProgressBar80%.png";
                break;
            case 9:
                this.progressBarDailyFilepathString = "/images/pet/ProgressBar90%.png";
                break;
            case 10:
                this.progressBarDailyFilepathString = "/images/pet/ProgressBar100%.png";
                break;
        }

        Image progressBarDailyImage = new Image(progressBarDailyFilepathString);
        progressBarDaily.setImage(progressBarDailyImage);

        XYChart.Series<String, Integer> dataSeriesPomDurationData = new XYChart.Series<>();
        XYChart.Series<String, Integer> dataSeriesTasksDoneData = new XYChart.Series<>();
        // dataSeries.setName("You");

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
    }
    /*
    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StatisticsDisplay)) {
            return false;
        }

        // state check
        StatisticsDisplay card = (StatisticsDisplay) other;
        return barChartTitle.getText().equals(card.barChartTitle.getText());
    } */
}
