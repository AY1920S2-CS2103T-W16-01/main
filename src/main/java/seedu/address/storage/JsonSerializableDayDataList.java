package seedu.address.storage;

import static seedu.address.model.dayData.CustomQueue.tableConstraintsAreEnforced;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlyStatistics;
import seedu.address.model.Statistics;
import seedu.address.model.dayData.CustomQueue;
import seedu.address.model.dayData.DayData;
import seedu.address.model.dayData.exceptions.InvalidTableException;

/** An Immutable CustomQueue that is serializable to JSON format. */
@JsonRootName(value = "statistics")
class JsonSerializableDayDataList {
    private final List<JsonAdaptedDayData> dayDatas = new ArrayList<>();

    /**
     * Converts a given {@code ReadOnlyStatistics} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code} JsonSerializableDayDataList}.
     */
    public JsonSerializableDayDataList(ReadOnlyStatistics source) {
        dayDatas.addAll(
                source.getCustomQueue()
                        .stream()
                        .map(JsonAdaptedDayData::new)
                        .collect(Collectors.toList()));
    }

    /**
     * Converts dayDatas into the model's {@code Statistics} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     * @throws InvalidTableException if there were any table constraints violated.
     */
    public Statistics toModelType() throws IllegalValueException, InvalidTableException {
        List<DayData> dayDataList = new ArrayList<>();

        for (JsonAdaptedDayData jsonAdaptedDayData : dayDatas) {
            DayData dayData = jsonAdaptedDayData.toModelType();
            dayDataList.add(dayData);
        }

        if (!tableConstraintsAreEnforced(dayDataList)) {
            throw new InvalidTableException(CustomQueue.MESSAGE_CONSTRAINTS);
        } else {
            Statistics statistics = new Statistics();
            statistics.setDayDatas(dayDataList);
            return statistics;
        }
    }
}
