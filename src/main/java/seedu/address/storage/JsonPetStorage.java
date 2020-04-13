package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.InvalidPetException;
import seedu.address.model.ReadOnlyPet;

/** A class to access TaskList data stored as a json file on the hard disk. */
public class JsonPetStorage implements PetStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonPetStorage.class);

    private Path filePath;

    public JsonPetStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getPetFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyPet> readPet() throws DataConversionException, InvalidPetException {
        return readPet(filePath);
    }

    /**
     * Similreaar to {@link #readPet()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyPet> readPet(Path filePath)
            throws DataConversionException, InvalidPetException {
        requireNonNull(filePath);

        Optional<JsonAdaptedPet> jsonPet = JsonUtil.readJsonFile(filePath, JsonAdaptedPet.class);
        if (!jsonPet.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonPet.get().toModelType()); // Returns pet read from json
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void savePet(ReadOnlyPet pet) throws IOException {
        savePet(pet, filePath);
    }

    /**
     * Similar to {@link #savePet(ReadOnlyPet)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    @Override
    public void savePet(ReadOnlyPet pet, Path filePath) throws IOException {
        requireNonNull(pet);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonAdaptedPet(pet), filePath);
    }
}
