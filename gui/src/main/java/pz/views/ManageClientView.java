package pz.views;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.logging.log4j.util.Strings;
import pz.model.database.entities.ApartmentEntity;
import pz.model.database.entities.ClientEntity;
import pz.services.api.client.ClientApiService;
import pz.services.events.ChangeLanguageEvent;
import pz.services.events.RefreshClientListEvent;
import pz.services.events.ViewEventBus;
import pz.services.settings.SettingsService;
import pz.services.settings.language.Bundles;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static pz.services.settings.language.Bundles.*;

public class ManageClientView implements View {
    private static final String RESOURCE = "manageClientView.fxml";
    private static Stage manageClientViewStage;
    private SettingsService settingsService;
    private ClientApiService clientApiService;

    private static ClientManagerMode viewMode;
    private ClientEntity selectedClient;

    public TextField searchTextField;
    public Button addClientButton;
    public Button editClientButton;
    public Button deleteClientButton;
    public Button clientSelectButton;
    public TableView<ClientEntity> clientTableView;
    public Button closeButton;

    private List<ClientEntity> allClientTableItems = Lists.newArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewEventBus.subscribe(this);
        clientApiService = ClientApiService.getInstance();
        settingsService = SettingsService.getInstance();

        addClientButton.setOnMouseClicked(event -> ClientFormView.show(ClientFormAction.ADD, null));
        editClientButton.setOnMouseClicked(event -> handleClientEdit());
        deleteClientButton.setOnMouseClicked(event -> handleClientDelete());
        closeButton.setOnMouseClicked(event -> manageClientViewStage.close());
        initializeClientSelectButton(clientSelectButton);
        initializeSearchTextField();
        initializeLanguage();
    }

    private void handleClientEdit() {
        if (Objects.isNull(selectedClient)) {
            showClientNotSelectedDialog();
        } else {
            ClientFormView.show(ClientFormAction.EDIT, selectedClient);
        }
    }

    public static void show(Stage stage, ClientManagerMode mode) {
        manageClientViewStage = Objects.nonNull(stage) ? stage : new Stage();
        viewMode = mode;
        try {
            Parent parent = FXMLLoader.load(ManageClientView.class.getClassLoader().getResource(RESOURCE));
            manageClientViewStage.setScene(new Scene(parent));
            manageClientViewStage.setResizable(false);
            manageClientViewStage.show();
        } catch (IOException e) {
            throw new ViewLoadingException(e);
        }
    }

    @Subscribe
    public void handleRefreshClientListEvent(RefreshClientListEvent event) {
        updateClientsList();
    }

    private void initializeClientSelectButton(Button clientSelectButton) {
        initializeClientTableView();
        if (viewMode == ClientManagerMode.BOOKING) {
            clientSelectButton.setOnMouseClicked(click -> {
                if (Objects.isNull(selectedClient)) {
                    showClientNotSelectedDialog();
                } else {
                    ApartmentSelectView.show(manageClientViewStage, selectedClient);
                }
            });
        } else {
            clientSelectButton.setVisible(false);
        }
    }

    private void initializeSearchTextField() {
        searchTextField.setPromptText(getValue(MANAGE_CLIENT_SEARCH_TEXT_FIELD_PROMPT));
        searchTextField.textProperty().addListener((ov, oldVal, newVal) -> {
            if (searchTextField.getText().isEmpty()) {
                clientTableView.setItems(FXCollections.observableArrayList(allClientTableItems));
            } else {
                clientTableView.setItems(FXCollections.observableArrayList(allClientTableItems.stream()
                        .filter(client -> this.clientContainsText(client, newVal.toLowerCase()))
                        .collect(Collectors.toList())));
            }
        });
    }

    private boolean clientContainsText(ClientEntity client, String text) {
        return client.getName().toLowerCase().contains(text)
                || client.getSurname().toLowerCase().contains(text)
                || client.getAddress().toLowerCase().contains(text)
                || client.getCity().toLowerCase().contains(text)
                || client.getPostalCode().toLowerCase().contains(text);
    }

    private void initializeClientTableView() {
        TableColumn nameColumn = new TableColumn(getValue(MANAGE_CLIENT_COLUMN_NAME));
        nameColumn.setCellValueFactory(new PropertyValueFactory<ApartmentEntity, String>("name"));
        TableColumn surnameColumn = new TableColumn(getValue(MANAGE_CLIENT_COLUMN_SURNAME));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<ApartmentEntity, String>("surname"));
        TableColumn emailColumn = new TableColumn(getValue(MANAGE_CLIENT_COLUMN_EMAIL));
        emailColumn.setCellValueFactory(new PropertyValueFactory<ApartmentEntity, String>("email"));
        TableColumn addressColumn = new TableColumn(getValue(MANAGE_CLIENT_COLUMN_ADDRESS));
        addressColumn.setCellValueFactory(new PropertyValueFactory<ApartmentEntity, String>("address"));
        TableColumn postalCodeColumn = new TableColumn(getValue(MANAGE_CLIENT_COLUMN_POSTAL_CODE));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<ApartmentEntity, String>("postalCode"));
        TableColumn cityColumn = new TableColumn(getValue(MANAGE_CLIENT_COLUMN_CITY));
        cityColumn.setCellValueFactory(new PropertyValueFactory<ApartmentEntity, String>("city"));

        clientTableView.getColumns().removeAll();
        clientTableView.getColumns().addAll(nameColumn, surnameColumn, emailColumn, addressColumn, postalCodeColumn, cityColumn);
        clientTableView.getSelectionModel().selectedItemProperty().addListener((ov, oldVal, newVal) -> selectedClient = newVal);
        updateClientsList();
    }

    private void updateClientsList() {
        List<ClientEntity> queryResult = clientApiService.getClientsList();
        ObservableList<ClientEntity> clientsList = FXCollections.observableArrayList(queryResult);
        allClientTableItems.addAll(queryResult);
        clientTableView.setItems(clientsList);
        searchTextField.setText(Strings.EMPTY);
    }

    private void showClientNotSelectedDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(getValue(UTILS_DIALOG_WARNING));
        alert.setHeaderText(getValue(MANAGE_CLIENT_DIALOG_CLIENT_NOT_SELECTED_HEADER));
        alert.setContentText(getValue(MANAGE_CLIENT_DIALOG_CLIENT_NOT_SELECTED_CONTENT));
        alert.showAndWait();
    }

    private void handleClientDelete() {
        if (Objects.isNull(selectedClient)) {
            showClientNotSelectedDialog();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, getValue(MANAGE_CLIENT_DIALOG_CLIENT_DELETE_CONTENT), ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle(getValue(MANAGE_CLIENT_DIALOG_CLIENT_DELETE_TITLE));
        alert.setHeaderText(String.format(getValue(MANAGE_CLIENT_DIALOG_CLIENT_DELETE_HEADER), selectedClient.getName() + " " + selectedClient.getSurname()));
        alert.showAndWait();
        if (alert.getResult() != ButtonType.YES) {
            return;
        }
        clientApiService.delete(selectedClient.getId());
        updateClientsList();
        showClientDeleteSuccessDialog();
    }

    private void showClientDeleteSuccessDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(getValue(UTILS_DIALOG_SUCCESS));
        alert.setHeaderText(getValue(UTILS_DIALOG_SUCCESS));
        alert.setContentText(getValue(MANAGE_CLIENT_DIALOG_CLIENT_DELETE_SUCCESS_CONTENT));
        alert.showAndWait();
    }

    @Override
    @Subscribe
    public void setLanguage(ChangeLanguageEvent event) {
        initializeLanguage();
    }

    private void initializeLanguage() {
        addClientButton.setText(getValue(MANAGE_CLIENT_ADD_CLIENT_BUTTON));
        clientSelectButton.setText(getValue(MANAGE_CLIENT_CLIENT_SELECT_BUTTON));
        closeButton.setText(getValue(MANAGE_CLIENT_CLOSE_BUTTON));
        deleteClientButton.setText(getValue(MANAGE_CLIENT_DELETE_CLIENT_BUTTON));
        editClientButton.setText(getValue(MANAGE_CLIENT_EDIT_CLIENT_BUTTON));
        searchTextField.setPromptText(getValue(MANAGE_CLIENT_SEARCH_TEXT_FIELD_PROMPT));
        initializeClientTableView();
    }

    private String getValue(Bundles bundle) {
        ResourceBundle resourceBundle = settingsService.getResourceBundle();
        return resourceBundle.getString(bundle.value);
    }
}
