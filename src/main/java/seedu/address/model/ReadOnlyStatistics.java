package seedu.address.model;

import java.util.List;
import seedu.address.model.dayData.DayData;

public interface ReadOnlyStatistics {
    /**
     * Returns an unmodifiable view of the persons list. This list will not contain any duplicate
     * persons.
     *
     * @return
     */
    List<DayData> getDayDataList();
}
