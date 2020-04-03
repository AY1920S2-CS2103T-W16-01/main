package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Pet;
import seedu.address.model.ReadOnlyPet;

@JsonRootName(value = "pet")
class JsonAdaptedPet {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Pet's %s field is missing!";

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
    public ReadOnlyPet toModelType() throws IllegalValueException {
        // TODO set up proper model for all attributes of the Pet
        // if (name == null) {
        // throw new IllegalValueException(
        // String.format(MISSING_FIELD_MESSAGE_FORMAT, "name"));
        // }
        // if (!Name.isValidName(name)) {
        // throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        // }
        // final Name modelName = new Name(name);

        // if (exp == null) {
        // throw new IllegalValueException(
        // String.format(MISSING_FIELD_MESSAGE_FORMAT, "exp"));
        // }
        // if (!exp.isValidexp(exp)) {
        // throw new IllegalValueException(exp.MESSAGE_CONSTRAINTS);
        // }
        // final exp modelexp = new exp(priority);

        // if (level == null) {
        // throw new IllegalValueException(
        // String.format(MISSING_FIELD_MESSAGE_FORMAT, level.class.getSimpleName()));
        // }
        // if (!level.isValidlevel(level)) {
        // throw new IllegalValueException(level.MESSAGE_CONSTRAINTS);
        // }
        // final level modellevel = new level(level);

        return new Pet(name, exp, level, mood, lastDoneTaskTime);
    }
}
