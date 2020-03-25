package seedu.address.testutil;

import seedu.address.model.dayData.Date;
import seedu.address.model.dayData.DayData;
import seedu.address.model.dayData.PomDurationData;
import seedu.address.model.dayData.TasksDoneData;

/** A utility class to help with building DayData objects. */
public class DayDataBuilder {

    public static final String DEFAULT_DATE = "2020-03-18";
    public static final String DEFAULT_POM_DURATION_DATA = "100";
    public static final String DEFAULT_TASKS_DONE_DATA = "10";

    private Date date;
    private PomDurationData pomDurationData;
    private TasksDoneData tasksDoneData;


    public DayDataBuilder() {
        date = new Date(DEFAULT_DATE);
        pomDurationData = new PomDurationData(DEFAULT_POM_DURATION_DATA);
        tasksDoneData = new TasksDoneData(DEFAULT_TASKS_DONE_DATA);
    }

    /** Initializes the DayDataBuilder with the data of {@code dayDataToCopy}. */
    public DayDataBuilder(DayData dayDataToCopy) {
        date = dayDataToCopy.getDate();
        pomDurationData = dayDataToCopy.getPomDurationData();
        tasksDoneData = dayDataToCopy.getTasksDoneData();
    }

    /** Sets the {@code Date} of the {@code DayData} that we are building. */
    public DayDataBuilder withDate(String date) {
        this.date = new Date(date);
        return this;
    }

    /** Sets the {@code PomDurationData} of the {@code DayData} that we are building. */
    public DayDataBuilder withPomDurationData(String pomDurationData) {
        this.pomDurationData = new PomDurationData(pomDurationData);
        return this;
    }

    /** Sets the {@code TasksDoneData} of the {@code DayData} that we are building. */
    public DayDataBuilder withTasksDoneData(String tasksDoneData) {
        this.tasksDoneData = new TasksDoneData(tasksDoneData);
        return this;
    }

    public DayData build() {
        return new DayData(date, pomDurationData, tasksDoneData);
    }
}
