package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.dayData.DayData;

public interface ReadOnlyStatistics {
    /**
     * Returns an unmodifiable view of the persons list. This list will not contain any duplicate
     * persons.
     *
     * @return
     */
    ObservableList<DayData> getDayDataList();
}
