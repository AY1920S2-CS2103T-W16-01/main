package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlyStatistics;
import seedu.address.model.Statistics;
import seedu.address.model.dayData.CustomQueue;
import seedu.address.model.dayData.DayData;
import seedu.address.model.dayData.exceptions.InvalidTableException;

import static seedu.address.model.dayData.CustomQueue.CONSTANT_SIZE;

/** An Immutable TaskList that is serializable to JSON format. */
@JsonRootName(value = "statistics")
class JsonSerializableDayDataList {
    private final List<JsonAdaptedDayData> dayDatas = new ArrayList<>();

    /** Constructs a {@code JsonSerializableTaskList} with the given tasks. */
    @JsonCreator
    public JsonSerializableDayDataList(
            @JsonProperty("medals") String medals,
            @JsonProperty("dayDatas") List<JsonAdaptedDayData> dayDatas) {
        this.dayDatas.addAll(dayDatas);
    }

    /**
     * Converts a given {@code ReadOnlyTaskList} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code
     *     JsonSerializableTaskList}.
     */
    public JsonSerializableDayDataList(ReadOnlyStatistics source) {
        dayDatas.addAll(
                source.getDayDataList()
                        .stream()
                        .map(JsonAdaptedDayData::new)
                        .collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code TaskList} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public Statistics toModelType() throws IllegalValueException, InvalidTableException, DateTimeParseException {
        Statistics dayDataList = new Statistics();
        dayDataList.clearList(); // workaround to create empty list

        int count = 0;

        try {
            String dateCheckPointer = dayDatas.get(0).getDateJson();
            LocalDate localDateCheckPointer = LocalDate.parse(dateCheckPointer);
            String currentDate = null;

            for (JsonAdaptedDayData jsonAdaptedDayData : dayDatas) {
                DayData dayData = jsonAdaptedDayData.toModelType();
                dayDataList.addDayData(dayData);
                count++;

                currentDate = jsonAdaptedDayData.getDateJson();
                LocalDate currentLocalDate = LocalDate.parse(currentDate);
                if (!currentLocalDate.equals(localDateCheckPointer)) {
                    throw new InvalidTableException(CustomQueue.MESSAGE_CONSTRAINTS); // days must be continuous
                }

                localDateCheckPointer = localDateCheckPointer.plusDays(1);
            }
        } catch (DateTimeParseException | NullPointerException | IndexOutOfBoundsException e) {

        }

        if (count != CONSTANT_SIZE) {
            throw new InvalidTableException(CustomQueue.MESSAGE_CONSTRAINTS); // table is not size FIXED_SIZE
        }

        return dayDataList;
    }
}
