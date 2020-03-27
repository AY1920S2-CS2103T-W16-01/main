package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.ReadOnlyPet;

/** An UI component that displays Pet {@code Pet}. */
public class PetDisplay extends UiPart<Region> {

    private static final String FXML = "PetDisplay.fxml";

    private String petFilepathString; // mutable

    //private Path accessoryFilepath; // mutable

    private String expBarFilepathString; // mutable

    private String expBarText; // mutable
    private String levelText; // mutable

    private ReadOnlyPet pet;

    @FXML private VBox petPane;
    @FXML private ImageView petPic;
    @FXML private Label expBarView;
    @FXML private ImageView expBarPic;
    @FXML private ImageView accessoryPic;
    @FXML private Label levelView;

    public PetDisplay(ReadOnlyPet pet) {
        super(FXML);
        this.pet = pet;
        update();
    }

    public void update() {

        int exp = Integer.parseInt(this.pet.getExp());
        int expBarInt = exp % 100;
        this.expBarText = String.format("%d XP / 100 XP", expBarInt);

        this.levelText = this.pet.getLevel();

        if (this.levelText.equals("1")) {
            this.petFilepathString = "/images/pet/level1.png";
        } else if (this.levelText.equals("2")) {
            this.petFilepathString = "/images/pet/level2.png";
        } else {
            this.petFilepathString = "/images/pet/level3.png";
        }

        int expBarPerc = expBarInt / 10;

        switch (expBarPerc) {
            case 0:
                this.expBarFilepathString = "/images/pet/ProgressBar0%.png";
                break;

            case 1:
                this.expBarFilepathString = "/images/pet/ProgressBar10%.png";
                break;

            case 2:
                this.expBarFilepathString = "/images/pet/ProgressBar20%.png";
                break;

            case 3:
                this.expBarFilepathString = "/images/pet/ProgressBar30%.png";
                break;

            case 4:
                this.expBarFilepathString = "/images/pet/ProgressBar40%.png";
                break;

            case 5:
                this.expBarFilepathString = "/images/pet/ProgressBar50%.png";
                break;

            case 6:
                this.expBarFilepathString = "/images/pet/ProgressBar60%.png";
                break;

            case 7:
                this.expBarFilepathString = "/images/pet/ProgressBar70%.png";
                break;

            case 8:
                this.expBarFilepathString = "/images/pet/ProgressBar80%.png";
                break;

            case 9:
                this.expBarFilepathString = "/images/pet/ProgressBar90%.png";
                break;

            case 10:
                this.expBarFilepathString = "/images/pet/ProgressBar100%.png";
                break;
        }

        // if (accessoryFilepath != null) {
        //     this.accessoryFilepath = accessoryFilepath;
        //     Image image = new Image(String.valueOf(accessoryFilepath));
        //     accessoryPic.setImage(image);
        // }

        expBarView.setText(expBarText);
        levelView.setText(levelText);

        // set up pet image
        Image petImage = new Image(petFilepathString);
        petPic.setImage(petImage);

        // set up experience bar image
        Image expBarImage = new Image(expBarFilepathString);
        expBarPic.setImage(expBarImage);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PetDisplay)) {
            return false;
        }

        // state check
        PetDisplay card = (PetDisplay) other;
        return petPic.getImage().equals(card.petPic.getImage());
    }
}
