package pz.views;

import javafx.fxml.Initializable;
import pz.services.events.Subscriber;
import pz.services.events.ChangeLanguageEvent;
import pz.services.events.ViewEventBus;

import java.net.URL;
import java.util.ResourceBundle;

public interface View extends Initializable, Subscriber {
    @Override
    default void initialize(URL location, ResourceBundle resources) {
        ViewEventBus.subscribe(this);
    }
    void setLanguage(ChangeLanguageEvent event);
}
