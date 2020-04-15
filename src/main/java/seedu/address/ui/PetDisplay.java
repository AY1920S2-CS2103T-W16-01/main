package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.MainApp;

/** An UI component that displays Pet {@code Pet}. */
public class PetDisplay extends UiPart<Region> {

    private static final String FXML = "PetDisplay.fxml";
    private String DEFAULT_PET_FILEPATH = "/images/pet/level1.png";
    private String DEFAULT_EXPBAR_FILEPATH = "/images/pet/ProgressBar0%.png";
    private String DEFAULT_EXPBAR_TEXT = "0";
    private String DEFAULT_LEVEL_TEXT = "1";
    private String DEFEAULT_PET_NAME = "BB";

    private String petFilepath; // mutable
    private String expBarFilepath; // mutable
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

    public void setPetImage(String path) {
        petFilepath = path;
        Image petImage = new Image(MainApp.class.getResourceAsStream(petFilepath));
        petPic.setImage(petImage);
    }

    public void setExpBarImage(String path) {
        expBarFilepath = path;
        Image expBarImage = new Image(MainApp.class.getResourceAsStream(expBarFilepath));
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
}
