package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.Pet;
import seedu.address.model.ReadOnlyPet;

public class JsonPetStorageTest {
    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonPetStorageTest");

    @TempDir public Path testFolder;

    private java.util.Optional<ReadOnlyPet> readPet(String filePath) throws Exception {
        return new JsonPetStorage(Paths.get(filePath))
                .readPet(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void readPet_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readPet(null));
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readPet("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readPet("notJsonFormatPet.json"));
    }

    @Test
    public void readAndSavePet_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempPet.json");
        Pet original = new Pet();
        JsonPetStorage jsonPetStorage = new JsonPetStorage(filePath);

        // Save in new file and read back
        jsonPetStorage.savePet(original, filePath);
        ReadOnlyPet readBack = jsonPetStorage.readPet(filePath).get();
        assertEquals(original, new Pet(readBack));

        // Modify data, overwrite exiting file, and read back
        original.setName("Momu");
        original.changeHangry();
        jsonPetStorage.savePet(original, filePath);
        readBack = jsonPetStorage.readPet(filePath).get();
        assertEquals(original, new Pet(readBack));

        // Save and read without specifying file path
        jsonPetStorage.savePet(original); // file path not specified
        readBack = jsonPetStorage.readPet().get(); // file path not specified
        assertEquals(original, new Pet(readBack));
    }

    @Test
    public void savePet_nullPet_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> savePet(null, "SomeFile.json"));
    }

    private void savePet(ReadOnlyPet pet, String filePath) {
        try {
            new JsonPetStorage(Paths.get(filePath))
                    .savePet(pet, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void savePets_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> savePet(new Pet(), null));
    }
}
