package seedu.address.ui;

import java.nio.file.Path;
import java.nio.file.Paths;
import seedu.address.model.ReadOnlyPet;

public class PetDisplayHandler {
    private ReadOnlyPet pet;
    private PetDisplay petDisplay;
    private Path petFilepath;
    private Path expBarFilepath;
    private Path accessoryFilepath;
    private String levelText;
    private String expBarText;

    public PetDisplayHandler(ReadOnlyPet pet) {
        this.pet = pet;
        updatePetDisplayHandler();
        petDisplay =
                new PetDisplay(
                        this.petFilepath,
                        this.expBarText,
                        this.expBarFilepath,
                        this.accessoryFilepath,
                        this.levelText);
    }

    public void updatePetDisplayHandler() {

        int exp = Integer.parseInt(this.pet.getExp());
        int expBarInt = exp % 100;
        this.expBarText = String.format("%d XP / 100 XP", expBarInt);

        this.levelText = this.pet.getLevel();

        if (pet.getLevel().equals("1")) {
            this.petFilepath = Paths.get("images", "pet", "level1.png");
        } else {
            this.petFilepath = Paths.get("images", "pet", "level2.png");
        }

        int expBarPerc = expBarInt / 10;
        System.out.println("PetDisplayHandler: expBarPerc:" + expBarPerc);

        switch (expBarPerc) {
            case 0:
                this.expBarFilepath = Paths.get("images", "pet", "ProgressBar0%.png");
                break;

            case 1:
                this.expBarFilepath = Paths.get("images", "pet", "ProgressBar10%.png");
                break;

            case 2:
                this.expBarFilepath = Paths.get("images", "pet", "ProgressBar20%.png");
                break;

            case 3:
                this.expBarFilepath = Paths.get("images", "pet", "ProgressBar30%.png");
                break;

            case 4:
                this.expBarFilepath = Paths.get("images", "pet", "ProgressBar40%.png");
                break;

            case 5:
                this.expBarFilepath = Paths.get("images", "pet", "ProgressBar50%.png");
                break;

            case 6:
                this.expBarFilepath = Paths.get("images", "pet", "ProgressBar60%.png");
                break;

            case 7:
                this.expBarFilepath = Paths.get("images", "pet", "ProgressBar70%.png");
                break;

            case 8:
                this.expBarFilepath = Paths.get("images", "pet", "ProgressBar80%.png");
                break;

            case 9:
                this.expBarFilepath = Paths.get("images", "pet", "ProgressBar90%.png");
                break;

            case 10:
                this.expBarFilepath = Paths.get("images", "pet", "ProgressBar100%.png");
                break;
        }

        this.accessoryFilepath = null;
    }

    public void updatePetDisplay() {
        updatePetDisplayHandler();
        petDisplay.update(
                this.petFilepath,
                this.expBarText,
                this.expBarFilepath,
                this.accessoryFilepath,
                this.levelText);
    }

    // getters
    public Path getAccessoryFilepath() {
        return this.accessoryFilepath;
    }

    public String getExpBarText() {
        return this.expBarText;
    }

    public Path getPetFilepath() {
        return this.petFilepath;
    }

    public Path getExpBarFilepath() {
        return this.expBarFilepath;
    }

    public String getLevelText() {
        return this.levelText;
    }

    public PetDisplay getPetDisplay() {
        return this.petDisplay;
    }
}
