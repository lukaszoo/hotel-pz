package pz.views;

import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import pz.services.events.ChangeLanguageEvent;
import pz.services.events.ViewEventBus;
import pz.services.settings.SettingsService;
import pz.services.settings.language.Bundles;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuView implements View {
    private static final String RESOURCE = "menuView.fxml";
    private SettingsService settingsService;

    public Button bookApartmentButton;
    public Button manageClientsButton;
    public Button showHotelMapButton;
    public Button settingsButton;
    public Button manageBookingsButton;
    public Button quitButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewEventBus.subscribe(this);
        settingsService = SettingsService.getInstance();

        showHotelMapButton.setOnMouseClicked(click -> HotelMapView.show());
        settingsButton.setOnMouseClicked(click -> SettingsView.show());
        initializeBookApartmentButton();
        initializeManageBookingButton();
        initializeManageClientsButton();
        quitButton.setOnMouseClicked(click -> System.exit(0));
    }

    private void initializeManageClientsButton() {
        enableForAdminOnly(manageClientsButton);
        manageClientsButton.setOnMouseClicked(click -> ManageClientView.show(new Stage(), ClientManagerMode.MANAGE));
    }

    private void initializeManageBookingButton() {
        enableForAdminOnly(manageBookingsButton);
        manageBookingsButton.setOnMouseClicked(click -> ManageBookingView.show());
    }

    private void initializeBookApartmentButton() {
        enableForAdminOnly(bookApartmentButton);
        bookApartmentButton.setOnMouseClicked(click -> ManageClientView.show(new Stage(), ClientManagerMode.BOOKING));
    }

    public static void show(Stage stage) {
        if (Objects.isNull(stage)) {
            stage = new Stage();
        }
        try {
            Parent parent = FXMLLoader.load(MenuView.class.getClassLoader().getResource(RESOURCE));
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            throw new ViewLoadingException(e);
        }
        ViewEventBus.publish(new ChangeLanguageEvent());
    }

    @Override
    @Subscribe
    public void setLanguage(ChangeLanguageEvent event) {
        showHotelMapButton.setText(getValue(Bundles.MENU_SHOW_HOTEL_MAP_BUTTON));
        settingsButton.setText(getValue(Bundles.MENU_SETTINGS_BUTTON));
        bookApartmentButton.setText(getValue(Bundles.MENU_BOOK_APARTMENT_BUTTON));
        manageBookingsButton.setText(getValue(Bundles.MENU_MANAGE_BOOKINGS_BUTTON));
        manageClientsButton.setText(getValue(Bundles.MENU_MANAGE_CLIENTS_BUTTON));
        quitButton.setText(getValue(Bundles.MENU_QUIT_BUTTON));
    }

    private String getValue(Bundles bundle) {
        ResourceBundle resourceBundle = settingsService.getResourceBundle();
        return resourceBundle.getString(bundle.value);
    }

    private void enableForAdminOnly(Button button) {
        if (!settingsService.getCurrentlyLoggedUser().isAdmin()) {
            button.setDisable(true);
        }
    }
}
