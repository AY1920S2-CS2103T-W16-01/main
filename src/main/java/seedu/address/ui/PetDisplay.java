package seedu.address.ui;

import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** An UI component that displays Pet {@code Pet}. */
public class PetDisplay extends UiPart<Region> {

    private static final String FXML = "PetDisplay.fxml";
    private Path DEFAULT_PET_FILEPATH = Paths.get("images", "pet", "level1.png");
    private Path DEFAULT_EXPBAR_FILEPATH = Paths.get("images", "pet", "ProgressBar0%.png");
    private String DEFAULT_EXPBAR_TEXT = "0";
    private String DEFAULT_LEVEL_TEXT = "1";
    private String DEFEAULT_PET_NAME = "BB";

    private Path petFilepath; // mutable
    private Path expBarFilepath; // mutable
    private String expBarText; // mutable
    private String levelText; // mutable
    private String petName;

    @FXML private Label petNameLabel;
    @FXML private VBox petPane;
    @FXML private ImageView petPic;
    @FXML private Label expBarView;
    @FXML private ImageView expBarPic;
    @FXML private Label levelView;

    public PetDisplay() {
        super(FXML);
        this.petName = DEFEAULT_PET_NAME;
        this.petFilepath = DEFAULT_PET_FILEPATH;
        this.expBarFilepath = DEFAULT_EXPBAR_FILEPATH;
        this.expBarText = DEFAULT_EXPBAR_TEXT;
        this.levelText = DEFAULT_LEVEL_TEXT;
    }

    public void setPetImage(Path path) {
        petFilepath = path;
        Image petImage = new Image(String.valueOf(petFilepath));
        petPic.setImage(petImage);
    }

    public void setExpBarImage(Path path) {
        expBarFilepath = path;
        Image expBarImage = new Image(String.valueOf(expBarFilepath));
        expBarPic.setImage(expBarImage);
    }

    public void setExpBarText(String expBarInt) {
        this.expBarText = expBarInt;
        expBarView.setText(expBarText);
    }

    public void setLevelText(String levelText) {
        this.levelText = levelText;
        levelView.setText(levelText);
    }

    public void setPetName(String petName) {
        this.petName = petName;
        petNameLabel.setText(petName);
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
