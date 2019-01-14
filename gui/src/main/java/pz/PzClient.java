package pz;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import pz.views.LoginView;

import java.io.IOException;

@Slf4j
public class PzClient extends Application {
    public static void main(String[] args) {
        log.info("Starting client application...");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            LoginView.show();
        } catch (IOException e) {
            log.error("IOException.", e);
        }
    }
}
