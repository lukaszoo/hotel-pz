package pz.views;

import com.google.common.eventbus.Subscribe;
import com.google.common.hash.Hashing;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pz.model.database.entities.UserEntity;
import pz.model.integration.AuthenticateUserDto;
import pz.services.api.user.UserApiService;
import pz.services.api.user.UserAuthenticationException;
import pz.services.events.ChangeLanguageEvent;
import pz.services.events.ViewEventBus;
import pz.services.settings.properties.PropertiesService;
import pz.services.settings.properties.Property;
import pz.services.settings.SettingsService;
import pz.services.settings.language.Bundles;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

import static pz.services.settings.language.Bundles.LOGIN_DIALOG_AUTHENTICATION_ERROR_HEADER;
import static pz.services.settings.language.Bundles.LOGIN_DIALOG_AUTHENTICATION_ERROR_TITLE;
import static pz.services.settings.language.Bundles.LOGIN_ERROR_INVALID_USER_NAME_OR_PASSWORD;

@Slf4j
@Getter
public class LoginView implements View {
    private static final String RESOURCE = "loginView.fxml";
    private UserApiService userApiService;
    private SettingsService settingsService;
    private PropertiesService propertiesService;
    private static Stage loginStage;
    public Label header;
    public Label loginLabel;
    public Label passwordLabel;
    public TextField loginField;
    public PasswordField passwordField;
    public Button performLoginButton;
    public Button settingsButton;

    public static void show() throws IOException {
        Parent parent = FXMLLoader.load(LoginView.class.getClassLoader().getResource(RESOURCE));
        Scene scene = new Scene(parent);
        loginStage.setScene(scene);
        loginStage.setResizable(false);
        loginStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewEventBus.subscribe(this);
        userApiService = UserApiService.getInstance();
        settingsService = SettingsService.getInstance();
        propertiesService = PropertiesService.getInstance();

        loginStage = new Stage();
        loginStage.setTitle(propertiesService.get(Property.APPLICATION_NAME));
        settingsButton.setOnMouseClicked(click -> SettingsView.show());
        performLoginButton.setOnMouseClicked(click -> this.authenticate());
        setLanguage(new ChangeLanguageEvent());
    }

    private void authenticate() {
        String passwordHash = Hashing.sha256()
                .hashString(passwordField.getText(), StandardCharsets.UTF_8)
                .toString();
        AuthenticateUserDto dto = new AuthenticateUserDto();
        dto.setUsername(loginField.getText());
        dto.setPassword(passwordHash);

        UserEntity userEntity;
        try {
            userEntity = userApiService.authenticateUser(dto);
            if (Objects.isNull(userEntity)) {
                throw new UserAuthenticationException(getValue(LOGIN_ERROR_INVALID_USER_NAME_OR_PASSWORD));
            }
            settingsService.setCurrentlyLoggedUser(userEntity);
            MenuView.show(new Stage());
            loginStage.close();

        } catch (UserAuthenticationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(getValue(LOGIN_DIALOG_AUTHENTICATION_ERROR_TITLE));
            alert.setHeaderText(getValue(LOGIN_DIALOG_AUTHENTICATION_ERROR_HEADER));
            alert.setContentText(e.reason);
            alert.showAndWait();
        }
    }

    @Override
    @Subscribe
    public void setLanguage(ChangeLanguageEvent event) {
        this.header.setText(getValue(Bundles.LOGIN_HEADER));
        this.loginLabel.setText(getValue(Bundles.LOGIN_LOGIN_LABEL));
        this.passwordLabel.setText(getValue(Bundles.LOGIN_PASSWORD_LABEL));
        this.performLoginButton.setText(getValue(Bundles.LOGIN_PERFORM_LOGIN_BUTTON));
        this.settingsButton.setText(getValue(Bundles.LOGIN_SETTINGS_BUTTON));
    }

    private String getValue(Bundles bundle) {
        ResourceBundle resourceBundle = settingsService.getResourceBundle();
        return resourceBundle.getString(bundle.value);
    }
}
