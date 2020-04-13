package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.scene.Parent;

public class CssManipulator {
    public static final String ERROR_STYLE_CLASS = "error";
    public static final String WARNING_STYLE_CLASS = "warning";
    public static final String SUCCESS_STYLE_CLASS = "success";

    /** Remvoes all possible styles applied */
    public static void setStyleToDefault(Parent node) {
        node.getStyleClass().remove(ERROR_STYLE_CLASS);
        node.getStyleClass().remove(WARNING_STYLE_CLASS);
        node.getStyleClass().remove(SUCCESS_STYLE_CLASS);
    }

    /** Sets the command box style to indicate a failed command. */
    public static void setStyleToIndicateFailure(Parent node) {
        ObservableList<String> styleClass = node.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /** Sets the command box style to indicate a failed auto complete. */
    public static void setStyleToIndicateWarning(Parent node) {
        ObservableList<String> styleClass = node.getStyleClass();

        if (styleClass.contains(WARNING_STYLE_CLASS)) {
            return;
        }

        styleClass.add(WARNING_STYLE_CLASS);
    }

    /** Sets the command box style to indicate a successful auto complete. */
    public static void setStyleToIndicateSuccess(Parent node) {
        ObservableList<String> styleClass = node.getStyleClass();

        if (styleClass.contains(SUCCESS_STYLE_CLASS)) {
            return;
        }

        styleClass.add(SUCCESS_STYLE_CLASS);
    }
}
