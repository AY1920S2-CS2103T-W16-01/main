package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.InvalidPetException;
import seedu.address.model.Pet;
import seedu.address.model.ReadOnlyPet;
import seedu.address.model.settings.PetName;

@JsonRootName(value = "pet")
class JsonAdaptedPet {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Pet's %s field is missing!";

    public static final String INVALID_PETNAME_MESSAGE = "invalid Pet name input";
    public static final String INVALID_EXP_MESSAGE = "Invalid experience input";
    public static final String INVALID_LEVEL_MESSAGE = "Invalid level input";
    public static final String INVALID_MOOD_MESSAGE = "Invalid mood input";
    public static final String INVALID_LASTDONETASKTIME_MESSAGE = "Invalid lastdonetasktime input";

    private final String name;
    private final String exp;
    private final String level;
    private final String mood;
    private final String lastDoneTaskTime;

    /** Constructs a {@code JsonAdaptedTask} with the given task details. */
    @JsonCreator
    public JsonAdaptedPet(
            @JsonProperty("name") String name,
            @JsonProperty("exp") String exp,
            @JsonProperty("level") String level,
            @JsonProperty("mood") String mood,
            @JsonProperty("lastDoneTaskTime") String lastDoneTaskTime) {
        this.name = name;
        this.exp = exp;
        this.level = level;
        this.mood = mood;
        this.lastDoneTaskTime = lastDoneTaskTime;
    }

    /** Converts a given {@code Task} into this class for Jackson use. */
    public JsonAdaptedPet(ReadOnlyPet source) {
        name = source.getName();
        exp = source.getExp();
        level = source.getLevel();
        mood = source.getMood();
        lastDoneTaskTime = source.getLastDoneTaskTime();
    }

    /**
     * Converts this Jackson-friendly adapted task object into the model's {@code Task} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted
     *     task.
     */
    public ReadOnlyPet toModelType() throws IllegalValueException, InvalidPetException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "name"));
        }
        if (!PetName.isValidPetName(name)) {
            throw new InvalidPetException(INVALID_PETNAME_MESSAGE);
        }
        final String modelName = name;

        if (level == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "level"));
        }

        if (!(level.equals("1") || level.equals("2") || level.equals("3"))) {
            throw new InvalidPetException(INVALID_LEVEL_MESSAGE);
        }

        final String modelLevel = level;

        if (exp == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "exp"));
        }

        int expInt = Integer.parseInt(exp);

        if (level.equals("1")) {
            if (!(expInt >= 0 && expInt < 100)) {
                throw new InvalidPetException(INVALID_EXP_MESSAGE);
            }
        } else if (level.equals("2")) {
            if (!(expInt >= 100 && expInt < 200)) {
                throw new InvalidPetException(INVALID_EXP_MESSAGE);
            }
        } else {
            if (!(expInt >= 200)) {
                throw new InvalidPetException(INVALID_EXP_MESSAGE);
            }
        }

        final String modelExp = exp;

        if (mood == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "mood"));
        }

        if (!(mood.equals("HAPPY") || mood.equals("HANGRY"))) {
            throw new InvalidPetException(INVALID_MOOD_MESSAGE);
        }

        final String modelMood = mood;

        try {
            LocalDateTime.parse(lastDoneTaskTime);
        } catch (DateTimeParseException e) {
            throw new InvalidPetException(INVALID_LASTDONETASKTIME_MESSAGE);
        }

        final String modelLastDoneTaskTime = lastDoneTaskTime;

        return new Pet(modelName, modelExp, modelLevel, modelMood, modelLastDoneTaskTime);
    }
}
