package seedu.address.ui;

import java.nio.file.Path;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** An UI component that displays Pet {@code Pet}. */
public class PetDisplay extends UiPart<Region> {

    private static final String FXML = "PetDisplay.fxml";

    public Path petFilepath; // mutable
    public Path accessoryFilepath; // mutable

    public Path expBarFilepath; // mutable
    public String expBarText; // mutable
    public String levelText; // mutable

    @FXML private VBox petPane;
    @FXML private ImageView petPic;
    @FXML private Label expBarView;
    @FXML private ImageView expBarPic;
    @FXML private ImageView accessoryPic;
    @FXML private Label levelView;

    public PetDisplay(
            Path petFilepath,
            String expBarText,
            Path expBarFilepath,
            Path accessoryFilepath,
            String levelText) {
        super(FXML);
        this.petFilepath = petFilepath;
        this.expBarFilepath = expBarFilepath;
        this.expBarText = expBarText;
        this.accessoryFilepath = null;
        this.levelText = levelText;

        if (accessoryFilepath != null) {
            this.accessoryFilepath = accessoryFilepath;
            Image image = new Image(String.valueOf(accessoryFilepath));
            accessoryPic.setImage(image);
        }

        expBarView.setText(expBarText);
        levelView.setText(levelText);

        // set up pet image
        Image petImage = new Image(String.valueOf(petFilepath));
        petPic.setImage(petImage);

        // set up experience bar image
        Image expBarImage = new Image(String.valueOf(expBarFilepath));
        expBarPic.setImage(expBarImage);
    }

    public void update(
            Path petFilepath,
            String expBarText,
            Path expBarFilepath,
            Path accessoryFilepath,
            String levelText) {

        this.petFilepath = petFilepath;
        this.expBarFilepath = expBarFilepath;
        this.expBarText = expBarText;
        this.accessoryFilepath = null;
        this.levelText = levelText;

        if (accessoryFilepath != null) {
            this.accessoryFilepath = accessoryFilepath;
            Image image = new Image(String.valueOf(accessoryFilepath));
            accessoryPic.setImage(image);
        }

        expBarView.setText(expBarText);
        System.out.println("PetDisplay: setBarText");
        levelView.setText(levelText);
        System.out.println("PetDisplay: levelText");

        // set up pet image
        Image petImage = new Image(String.valueOf(petFilepath));
        petPic.setImage(petImage);

        // set up experience bar image
        Image expBarImage = new Image(String.valueOf(expBarFilepath));
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
