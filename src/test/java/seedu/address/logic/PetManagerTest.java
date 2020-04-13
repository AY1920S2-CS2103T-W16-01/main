package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import seedu.address.model.Pet;

public class PetManagerTest {
    private final PetManager petManager = new PetManager();

    @Test
    public void incrementEXP_bydone() {
        Pet pet = new Pet();
        petManager.setPet(pet);
        petManager.incrementExp();
        assertTrue(pet.getExp().equals("5"));
    }

    @Test
    public void incrementEXP_byPom() {
        Pet pet = new Pet();
        petManager.setPet(pet);
        petManager.incrementPomExp();
        assertTrue(pet.getExp().equals("25"));
    }

    @Test
    public void updateLastDoneTime_success() {
        Pet pet =
                new Pet(
                        "BB",
                        "50",
                        "1",
                        "HAPPY",
                        LocalDateTime.now().minus(Duration.ofMinutes(5)).toString());
        petManager.setPet(pet);
        petManager.updateLastDoneTaskWhenDone();
        String lastDoneTaskTime = pet.getLastDoneTaskTime();
        LocalDateTime lastDoneTaskTimeWithoutMili = LocalDateTime.parse(lastDoneTaskTime).withNano(0);
        assertTrue(LocalDateTime.now().withNano(0).toString().equals(lastDoneTaskTimeWithoutMili.toString()));
        assertTrue(petManager.getTimeForHangry().withNano(0).equals(LocalDateTime.now().withNano(0).plusMinutes(1)));
    }

    @Test
    public void changeToHangry_success() {
        Pet pet = new Pet("BB", "50", "1", "HAPPY", LocalDateTime.now().toString());
        petManager.setPet(pet);
        petManager.changeToHangry();
        assertTrue(pet.getMood().equals("HANGRY"));
    }

    @Test
    public void changeToHappy_success() {
        Pet pet = new Pet("BB", "50", "1", "HANGRY", LocalDateTime.now().toString());
        petManager.setPet(pet);
        petManager.changeToHappy();
        assertTrue(pet.getMood().equals("HAPPY"));
    }

    @Test
    public void updateDisplayElementsHappy_success() {
        Pet pet = new Pet("BB", "50", "1", "HAPPY", LocalDateTime.now().toString());
        petManager.setPet(pet);
        petManager.updateDisplayElements();
        assertTrue(petManager.getExpBarInt().equals("50 XP / 100 XP"));
        assertTrue(petManager.getLevelText().equals("1"));
        assertTrue(petManager.getPetImage().equals("/images/pet/level1.png"));
        assertTrue(petManager.getExpBarImage().equals("/images/progress/ProgressBar50%.png"));
    }

    @Test
    public void updateDisplayElementsHangry_success() {
        Pet pet = new Pet("BB", "150", "2", "HANGRY", LocalDateTime.now().toString());
        petManager.setPet(pet);
        petManager.updateDisplayElements();
        assertTrue(petManager.getExpBarInt().equals("50 XP / 100 XP"));
        assertTrue(petManager.getLevelText().equals("2"));
        assertTrue(petManager.getPetImage().equals("/images/pet/level2hangry.png"));
        assertTrue(petManager.getExpBarImage().equals("/images/progress/ProgressBar50%.png"));
    }
}
