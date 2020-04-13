package seedu.address.logic;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import seedu.address.model.Pet;

public class PetManager {

    public final String HANGRY_MOOD_STRING = "HANGRY";
    public final String HAPPY_MOOD_STRING = "HAPPY";

    private Pet pet;
    private String petImage;
    private String expBarImage;
    private String expBarText;
    private String levelText;

    private LocalDateTime lastDoneTaskTime;
    private LocalDateTime timeForHangry;

    public PetManager() {}

    public void setPet(Pet pet) {
        requireNonNull(pet);
        this.pet = pet;
        this.lastDoneTaskTime = LocalDateTime.parse(pet.getLastDoneTaskTime());
        // use this for ACTUAL
        // this.timeForHangry = lastDoneTaskTime.plusHours(24);
        // use this for TESTING
        this.timeForHangry = lastDoneTaskTime.plusMinutes(1);
    }

    public void incrementPomExp() {
        this.pet.incrementPomExp();
    }

    public void incrementExp() {
        this.pet.incrementExp();
    }

    public void updateLastDoneTaskWhenDone() {
        lastDoneTaskTime = LocalDateTime.now();
        pet.setLastDoneTaskTime(lastDoneTaskTime.toString());
        // For ACTUAL
        // timeForHangry = lastDoneTaskTime.plusHours(24);
        // For TESTING
        timeForHangry = lastDoneTaskTime.plusMinutes(1);
    }

    public void changeToHangry() {
        pet.changeHangry();
    }

    public void changeToHappy() {
        pet.changeHappy();
    }

    public void updateDisplayElements() {

        int exp = Integer.parseInt(pet.getExp());
        int expBarInt = exp % 100;
        expBarText = String.format("%d XP / 100 XP", expBarInt);

        levelText = this.pet.getLevel();

        String mood = pet.getMood();

        if (levelText.equals("1")) {
            String str =
                    (mood.equals(HAPPY_MOOD_STRING)
                            ? "/images/pet/level1.png"
                            : "/images/pet/level1hangry.png");
            petImage = str;
        } else if (levelText.equals("2")) {
            String str =
                    (mood.equals(HAPPY_MOOD_STRING)
                            ? "/images/pet/level2.png"
                            : "/images/pet/level2hangry.png");
            petImage = str;
        } else {
            String str =
                    (mood.equals(HAPPY_MOOD_STRING)
                            ? "/images/pet/level3.png"
                            : "/images/pet/level3hangry.png");
            petImage = str;
        }

        int expBarPerc = expBarInt / 10;

        switch (expBarPerc) {
            case 0:
                expBarImage = "/images/progress/ProgressBar0%.png";
                break;

            case 1:
                expBarImage = "/images/progress/ProgressBar10%.png";
                break;

            case 2:
                expBarImage = "/images/progress/ProgressBar20%.png";
                break;

            case 3:
                expBarImage = "/images/progress/ProgressBar30%.png";
                break;

            case 4:
                expBarImage = "/images/progress/ProgressBar40%.png";
                break;

            case 5:
                expBarImage = "/images/progress/ProgressBar50%.png";
                break;

            case 6:
                expBarImage = "/images/progress/ProgressBar60%.png";
                break;

            case 7:
                expBarImage = "/images/progress/ProgressBar70%.png";
                break;

            case 8:
                expBarImage = "/images/progress/ProgressBar80%.png";
                break;

            case 9:
                expBarImage = "/images/progress/ProgressBar90%.png";
                break;

            case 10:
                expBarImage = "/images/progress/ProgressBar100%.png";
                break;

            default:
                expBarImage = "/images/progress/ProgressBar0%.png";
                break;
        }
    }

    public String getPetName() {
        return pet.getName();
    }

    public String getLevelText() {
        return levelText;
    }

    public String getExpBarInt() {
        return expBarText;
    }

    public String getExpBarImage() {
        return expBarImage;
    }

    public String getPetImage() {
        return petImage;
    }

    public String getMood() {
        return pet.getMood();
    }

    public LocalDateTime getTimeForHangry() {
        return timeForHangry;
    }
}
