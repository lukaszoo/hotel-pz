package pz.views;

import javafx.scene.control.Alert;

public class ViewLoadingException extends RuntimeException {
    public ViewLoadingException(Throwable e) {
        super(e);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("View loading error.");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
