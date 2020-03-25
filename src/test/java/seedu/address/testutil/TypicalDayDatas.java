package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_TASK1;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_TASK2;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HELP;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_MA1521;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.Statistics;
import seedu.address.model.TaskList;
import seedu.address.model.dayData.DayData;
import seedu.address.model.task.Task;

/** A utility class containing a list of {@code Task} objects to be used in tests. */
public class TypicalDayDatas {

    public static final DayData day0 =
            new DayDataBuilder()
                    .withDate("2020-03-17")
                    .withPomDurationData("10")
                    .withTasksDoneData("2")
                    .build();
    public static final DayData day1 =
            new DayDataBuilder()
                    .withDate("2020-03-18")
                    .withPomDurationData("0")
                    .withTasksDoneData("0")
                    .build();
    public static final DayData day2 =
            new DayDataBuilder()
                    .withDate("2020-03-19")
                    .withPomDurationData("40")
                    .withTasksDoneData("7")
                    .build();
    public static final DayData day3 =
            new DayDataBuilder()
                    .withDate("2020-03-20")
                    .withPomDurationData("30")
                    .withTasksDoneData("4")
                    .build();
    public static final DayData day4 =
            new DayDataBuilder()
                    .withDate("2020-03-21")
                    .withPomDurationData("70")
                    .withTasksDoneData("1")
                    .build();
    public static final DayData day5 =
            new DayDataBuilder()
                    .withDate("2020-03-22")
                    .withPomDurationData("100")
                    .withTasksDoneData("3")
                    .build();
    public static final DayData day6 =
            new DayDataBuilder()
                    .withDate("2020-03-23")
                    .withPomDurationData("90")
                    .withTasksDoneData("2")
                    .build();

    // Manually added
    public static final DayData dayNew =
            new DayDataBuilder()
                    .withDate("2020-03-24")
                    .withPomDurationData("40")
                    .withTasksDoneData("20")
                    .build();

    public static final DayData dayNew2 =
            new DayDataBuilder()
                    .withDate("2020-03-25")
                    .withPomDurationData("400")
                    .withTasksDoneData("1")
                    .build();



    //public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalDayDatas() {} // prevents instantiation

    /** Returns an {@code TaskList} with all the typical persons. */
    public static Statistics getTypicalStatistics() {
        Statistics statistics = new Statistics();
        statistics.clearList(); // workaround to create empty list
        for (DayData dayData : getTypicalDayDatas()) {
            statistics.addDayData(dayData);
        }
        return statistics;
    }

    public static List<DayData> getTypicalDayDatas() {
        return new ArrayList<>(
                Arrays.asList(day0, day1, day2, day3, day4, day5, day6));
    }
}
