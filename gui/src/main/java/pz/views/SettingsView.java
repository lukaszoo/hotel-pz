package pz.views;

import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pz.services.events.ChangeLanguageEvent;
import pz.services.events.ViewEventBus;
import pz.services.settings.SettingsService;
import pz.services.settings.language.Bundles;
import pz.services.settings.language.Language;
import pz.services.settings.skin.Skin;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsView implements View {
    private static final String RESOURCE = "settingsView.fxml";
    private static Stage settingsViewStage;

    private SettingsService settingsService;
    public Label header;
    public Label languageLabel;
    public ChoiceBox<String> languageChoiceBox;
    public Label skinLabel;
    public ChoiceBox<Skin> skinChoiceBox;
    public Button applyButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewEventBus.subscribe(this);
        settingsService = SettingsService.getInstance();

        languageChoiceBox.setItems(FXCollections.observableArrayList(Language.languageNames()));
        languageChoiceBox.setValue(settingsService.getCurrentLanguage().languageName);

        skinChoiceBox.setItems(FXCollections.observableArrayList(Skin.values()));
        skinChoiceBox.setValue(settingsService.getCurrentSkin());

        applyButton.setOnMouseClicked(click -> {
            settingsService.setCurrentLanguage(Language.forLanguageName(languageChoiceBox.getValue()));
            settingsService.setCurrentSkin(skinChoiceBox.getValue());
            settingsService.apply();
            settingsViewStage.close();
        });
    }

    public static void show() {
        settingsViewStage = new Stage();

        try {
            Parent parent = FXMLLoader.load(SettingsView.class.getClassLoader().getResource(RESOURCE));
            settingsViewStage.setScene(new Scene(parent));
            settingsViewStage.setResizable(false);
            settingsViewStage.show();
        } catch (Exception e) {
            throw new ViewLoadingException(e);
        }
    }

    @Override
    @Subscribe
    public void setLanguage(ChangeLanguageEvent event) {
        this.header.setText(getValue(Bundles.SETTINGS_HEADER));
        this.languageLabel.setText(getValue(Bundles.SETTINGS_LANGUAGE_LABEL));
        this.skinLabel.setText(getValue(Bundles.SETTINGS_SKIN_LABEL));
        this.applyButton.setText(getValue(Bundles.SETTINGS_APPLY_BUTTON));
    }

    private String getValue(Bundles bundle) {
        ResourceBundle resourceBundle = settingsService.getResourceBundle();
        return resourceBundle.getString(bundle.value);
    }
}
