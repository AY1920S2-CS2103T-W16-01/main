package seedu.address.testutil;

import seedu.address.model.Statistics;
import seedu.address.model.dayData.DayData;

/**
 * A utility class to help with building Statistics objects.
 */
public class DayDataListBuilder {

    private Statistics dayDataList;

    public DayDataListBuilder() {
        dayDataList = new Statistics();
    }

    public DayDataListBuilder(Statistics dayDataList) {
        this.dayDataList = dayDataList;
    }

    /** Adds a new {@code DayData} to the {@code Statistics} that we are building. */
    public DayDataListBuilder withDayData(DayData dayData) {
        dayDataList.addDayData(dayData);
        return this;
    }

    public Statistics build() {
        return dayDataList;
    }
}
