package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import seedu.address.model.Statistics;
import seedu.address.model.dayData.DayData;

/** A utility class containing a list of {@code Task} objects to be used in tests. */
public class TypicalDayDatas {
    public static final DayData DAY0 =
            new DayDataBuilder()
                    .withDate("2020-03-17")
                    .withPomDurationData("10")
                    .withTasksDoneData("2")
                    .build();
    public static final DayData DAY1 =
            new DayDataBuilder()
                    .withDate("2020-03-18")
                    .withPomDurationData("0")
                    .withTasksDoneData("0")
                    .build();
    public static final DayData DAY2 =
            new DayDataBuilder()
                    .withDate("2020-03-19")
                    .withPomDurationData("40")
                    .withTasksDoneData("7")
                    .build();
    public static final DayData DAY3 =
            new DayDataBuilder()
                    .withDate("2020-03-20")
                    .withPomDurationData("30")
                    .withTasksDoneData("4")
                    .build();
    public static final DayData DAY4 =
            new DayDataBuilder()
                    .withDate("2020-03-21")
                    .withPomDurationData("70")
                    .withTasksDoneData("1")
                    .build();
    public static final DayData DAY5 =
            new DayDataBuilder()
                    .withDate("2020-03-22")
                    .withPomDurationData("100")
                    .withTasksDoneData("3")
                    .build();
    public static final DayData DAY6 =
            new DayDataBuilder()
                    .withDate("2020-03-23")
                    .withPomDurationData("90")
                    .withTasksDoneData("2")
                    .build();

    // Manually added
    public static final DayData DAYNEW =
            new DayDataBuilder()
                    .withDate("2020-03-24")
                    .withPomDurationData("40")
                    .withTasksDoneData("20")
                    .build();

    public static final DayData DAYNEW2 =
            new DayDataBuilder()
                    .withDate("2020-03-25")
                    .withPomDurationData("400")
                    .withTasksDoneData("1")
                    .build();

    // public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalDayDatas() {} // prevents instantiation

    /** Returns an {@code TaskList} with all the typical tasks. */
    public static Statistics getTypicalStatistics() {
        Statistics statistics = new Statistics();
        for (DayData dayData : getTypicalDayDatas()) {
            statistics.update(dayData);
        }
        return statistics;
    }

    public static List<DayData> getTypicalDayDatas() {
        return new ArrayList<>(Arrays.asList(DAY0, DAY1, DAY2, DAY3, DAY4, DAY5, DAY6));
    }
}
