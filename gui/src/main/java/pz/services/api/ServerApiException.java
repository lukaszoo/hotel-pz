package pz.services.api;

import javafx.scene.control.Alert;

public class ServerApiException extends RuntimeException {
    public ServerApiException(String message) {
        super();
        showErrorAlert(message);
    }

    public ServerApiException(Throwable e) {
        super(e);
        showErrorAlert(e.getMessage());
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Server error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
