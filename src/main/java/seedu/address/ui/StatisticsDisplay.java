package seedu.address.ui;

import java.nio.file.Path;
import java.nio.file.Paths;
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

/** An UI component that displays the Pomodoro {@code Pomodoro}. */
public class StatisticsDisplay extends UiPart<Region> {

    private static final String FXML = "StatisticsDisplay.fxml";
    private static final String DEFAULT_BAR_CHART_TITLE = "Sample graph title";
    private static final Path DEFAULT_PROGRESS_BAR_DAILY__PLACEHOLDER =
            Paths.get("images", "statistics", "progressBarDaily0%.png");
    private static final String DEFAULT_PROGRESS_DAILY = "0 mins / 100 mins (DEFAULT)";
    private static final String DEFAULT_MEDALS = "Placeholder";

    private static final String POMODORO_RUNTIME_Y_AXIS = "Total Pomodoro Runtime";
    private static final String X_AXIS = "Day";

    public String barChartTitleText; // mutable
    public int[][] data; //
    public Path progressBarDailyFilepath;
    public String progressDailyText;
    public String medalsText;

    @FXML private VBox statisticsPane;
    @FXML private Label barChartTitle;
    @FXML private BarChart<String, Integer> barChart;

    @FXML private Label progressDaily;
    @FXML private ImageView progressBarDaily;
    @FXML private Label medals;

    public StatisticsDisplay() {
        super(FXML);
        this.barChartTitleText = DEFAULT_BAR_CHART_TITLE;
        this.data = null; // TODO *******************
        this.progressBarDailyFilepath = DEFAULT_PROGRESS_BAR_DAILY__PLACEHOLDER;
        this.progressDailyText = DEFAULT_PROGRESS_DAILY;
        this.medalsText = DEFAULT_MEDALS;

        progressDaily.setText(progressDailyText);
        Image progressBarDailyImage = new Image(String.valueOf(progressBarDailyFilepath));
        progressBarDaily.setImage(progressBarDailyImage);
        medals.setText(medalsText);

        barChartTitle.setText(barChartTitleText);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel(X_AXIS);
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(POMODORO_RUNTIME_Y_AXIS);

        XYChart.Series<String, Integer> dataSeries1 = new XYChart.Series<>();
        dataSeries1.setName("You");

        // TODO remove ********************
        dataSeries1.getData().add(new XYChart.Data<>("Day 1", 20));
        dataSeries1.getData().add(new XYChart.Data<>("Day 2", 40));
        dataSeries1.getData().add(new XYChart.Data<>("Day 3", 60));
        dataSeries1.getData().add(new XYChart.Data<>("Day 4", 40));
        dataSeries1.getData().add(new XYChart.Data<>("Day 5", 20));
        dataSeries1.getData().add(new XYChart.Data<>("Day 6", 40));
        dataSeries1.getData().add(new XYChart.Data<>("Day 7", 80));

        barChart.getData().add(dataSeries1);
    }

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
    }
}
