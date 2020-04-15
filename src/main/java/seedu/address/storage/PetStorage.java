package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.InvalidPetException;
import seedu.address.model.Pet;
import seedu.address.model.ReadOnlyPet;

/** Represents a storage for {@link seedu.address.model.TaskList}. */
public interface PetStorage {

    /** Returns the file path of the data file. */
    Path getPetFilePath();

    /**
     * Returns TaskList data as a {@link ReadOnlyPet}. Returns {@code Optional.empty()} if storage
     * file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyPet> readPet()
            throws DataConversionException, IOException, InvalidPetException;

    /** @see #getPetFilePath() */
    Optional<ReadOnlyPet> readPet(Path filePath)
            throws DataConversionException, IOException, InvalidPetException;

    /**
     * Saves the given {@link ReadOnlyPet} to the storage.
     *
     * @param taskList cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void savePet(ReadOnlyPet pet) throws IOException;

    /** @see #savePet(Pet) */
    void savePet(ReadOnlyPet pet, Path filePath) throws IOException;
}
