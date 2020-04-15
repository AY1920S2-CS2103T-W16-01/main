package seedu.address.storage;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;
import seedu.address.model.InvalidPetException;

public class JsonAdaptedPetTest {
    public static final String INVALID_PET_NAME = "@.@";
    public static final String INVALID_LASTDONETASKTIME = "123456";
    public static final String INVALID_LEVEL_UPPER = "4";
    public static final String INVALID_LEVEL_LOWER = "0";
    public static final String INVALID_EXP_EXP_FIRST = "150";
    public static final String INVALID_EXP_LEVEL_FIRST = "1";
    public static final String INVALID_EXP_EXP_SECOND = "50";
    public static final String INVALID_EXP_LEVEL_SECOND = "2";
    public static final String INVALID_MOOD = "NOT UNHAPPY";

    public static final String VALID_PET_NAME = "Momu";
    public static final String VALID_LASTDONETASKTIME = "2020-04-11T13:59:24.013541600";
    public static final String VALID_LEVEL = "2";
    public static final String VALID_EXP_EXP = "150";
    public static final String VALID_EXP_LEVEL = "2";
    public static final String VALID_MOOD = "HANGRY";

    @Test
    public void toModelType_invalidPetName_returnsInvalidPetException() {
        JsonAdaptedPet adaptedPet =
                new JsonAdaptedPet(
                        INVALID_PET_NAME,
                        VALID_EXP_EXP,
                        VALID_EXP_LEVEL,
                        VALID_MOOD,
                        VALID_LASTDONETASKTIME);
        String expectedMessage = JsonAdaptedPet.INVALID_PETNAME_MESSAGE;
        assertThrows(InvalidPetException.class, expectedMessage, adaptedPet::toModelType);
    }

    @Test
    public void toModelType_invalidLastDoneTaskTime_returnsInvalidPetException() {
        JsonAdaptedPet adaptedPet =
                new JsonAdaptedPet(
                        VALID_PET_NAME,
                        VALID_EXP_EXP,
                        VALID_EXP_LEVEL,
                        VALID_MOOD,
                        INVALID_LASTDONETASKTIME);
        String expectedMessage = JsonAdaptedPet.INVALID_LASTDONETASKTIME_MESSAGE;
        assertThrows(InvalidPetException.class, expectedMessage, adaptedPet::toModelType);
    }

    @Test
    public void toModelType_invalidLevelLower_returnsInvalidPetException() {
        JsonAdaptedPet adaptedPet =
                new JsonAdaptedPet(
                        VALID_PET_NAME,
                        VALID_EXP_EXP,
                        INVALID_LEVEL_LOWER,
                        VALID_MOOD,
                        VALID_LASTDONETASKTIME);
        String expectedMessage = JsonAdaptedPet.INVALID_LEVEL_MESSAGE;
        assertThrows(InvalidPetException.class, expectedMessage, adaptedPet::toModelType);
    }

    @Test
    public void toModelType_invalidLevelUpper_returnsInvalidPetException() {
        JsonAdaptedPet adaptedPet =
                new JsonAdaptedPet(
                        VALID_PET_NAME,
                        VALID_EXP_EXP,
                        INVALID_LEVEL_UPPER,
                        VALID_MOOD,
                        VALID_LASTDONETASKTIME);
        String expectedMessage = JsonAdaptedPet.INVALID_LEVEL_MESSAGE;
        assertThrows(InvalidPetException.class, expectedMessage, adaptedPet::toModelType);
    }

    @Test
    public void toModelType_invalidExpFirst_returnsInvalidPetException() {
        JsonAdaptedPet adaptedPet =
                new JsonAdaptedPet(
                        VALID_PET_NAME,
                        INVALID_EXP_EXP_FIRST,
                        INVALID_EXP_LEVEL_FIRST,
                        VALID_MOOD,
                        VALID_LASTDONETASKTIME);
        String expectedMessage = JsonAdaptedPet.INVALID_EXP_MESSAGE;
        assertThrows(InvalidPetException.class, expectedMessage, adaptedPet::toModelType);
    }

    @Test
    public void toModelType_invalidExpSecond_returnsInvalidPetException() {
        JsonAdaptedPet adaptedPet =
                new JsonAdaptedPet(
                        VALID_PET_NAME,
                        INVALID_EXP_EXP_SECOND,
                        INVALID_EXP_LEVEL_SECOND,
                        VALID_MOOD,
                        VALID_LASTDONETASKTIME);
        String expectedMessage = JsonAdaptedPet.INVALID_EXP_MESSAGE;
        assertThrows(InvalidPetException.class, expectedMessage, adaptedPet::toModelType);
    }

    @Test
    public void toModelType_invalidMood_returnsInvalidPetException() {
        JsonAdaptedPet adaptedPet =
                new JsonAdaptedPet(
                        VALID_PET_NAME,
                        VALID_EXP_EXP,
                        VALID_EXP_LEVEL,
                        INVALID_MOOD,
                        VALID_LASTDONETASKTIME);
        String expectedMessage = JsonAdaptedPet.INVALID_MOOD_MESSAGE;
        assertThrows(InvalidPetException.class, expectedMessage, adaptedPet::toModelType);
    }
}
