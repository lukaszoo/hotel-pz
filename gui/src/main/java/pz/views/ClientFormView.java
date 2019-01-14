package pz.views;

import com.google.common.collect.ImmutableSet;
import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.util.Strings;
import pz.model.database.entities.ClientEntity;
import pz.model.integration.CreateClientDto;
import pz.services.api.client.ClientApiService;
import pz.services.events.ChangeLanguageEvent;
import pz.services.events.FillEditedTextFieldsEvent;
import pz.services.events.RefreshClientListEvent;
import pz.services.events.ViewEventBus;
import pz.services.settings.SettingsService;
import pz.services.settings.language.Bundles;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class ClientFormView implements View {
    private static final String RESOURCE = "clientFormView.fxml";
    private SettingsService settingsService;

    private static Stage clientFormStage;
    private ClientApiService clientApiService;
    private Set<TextField> mandatoryTextFields;

    private static ClientEntity processedEntity;
    private static ClientFormAction formAction;

    public Label nameLabel;
    public TextField nameTextField;
    public Label surnameLabel;
    public TextField surnameTextField;
    public Label emailLabel;
    public TextField emailTextField;
    public Label addressLabel;
    public TextField addressTextField;
    public Label postalCodeLabel;
    public TextField postalCodeTextField;
    public Label cityLabel;
    public TextField cityTextField;
    public Button cancelButton;
    public Button confirmButton;
    private String dialogActionClientCreated;
    private String dialogActionClientEdited;
    private String dialogEmailTakenHeader;
    private String dialogEmailTakenContent;
    private String dialogMandatoryFieldsNotFilledHeader;
    private String dialogMandatoryFieldsNotFilledContent;
    private String dialogSuccessContent;
    private String dialogSuccessHeader;
    private String dialogWarningHeader;

    public static void show(ClientFormAction action, ClientEntity clientEntity) {
        processedEntity = clientEntity;
        formAction = action;
        try {
            Parent parent = FXMLLoader.load(ClientFormView.class.getClassLoader().getResource(RESOURCE));
            clientFormStage.setScene(new Scene(parent));
            clientFormStage.setResizable(false);
            clientFormStage.show();
        } catch (IOException e) {
            throw new ViewLoadingException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewEventBus.subscribe(this);
        settingsService = SettingsService.getInstance();

        clientFormStage = new Stage();
        clientApiService = ClientApiService.getInstance();

        if (formAction == ClientFormAction.EDIT) {
            ViewEventBus.publish(new FillEditedTextFieldsEvent(processedEntity));
        }

        mandatoryTextFields = ImmutableSet.<TextField>builder()
                .add(nameTextField)
                .add(surnameTextField)
                .add(emailTextField)
                .build();
        cancelButton.setOnMouseClicked(click -> clientFormStage.close());
        confirmButton.setOnMouseClicked(click -> handleConfirmButtonClick());
        initializeLabels();
    }

    private void handleConfirmButtonClick() {
        Boolean mandatoryLabelsFilled = mandatoryTextFields.stream()
                .allMatch(field -> Strings.isNotBlank(field.getText().trim()));
        if (!mandatoryLabelsFilled) {
            showMandatoryLabelsNotFilledDialog();
            return;
        }
        if (clientApiService.isEmailTaken(emailLabel.getText())) {
            showEmailTakenDialog();
            return;
        }
        switch (formAction) {
            case ADD:
                handleClientAdding();
                break;
            case EDIT:
                handleClientEditing();
        }
        ViewEventBus.publish(new RefreshClientListEvent());
        showSuccessDialog();
        clientFormStage.close();
    }

    private void handleClientEditing() {
        ClientEntity clientEntity = processedEntity;
        clientEntity.setName(nameTextField.getText());
        clientEntity.setSurname(surnameTextField.getText());
        clientEntity.setEmail(emailTextField.getText());
        clientEntity.setAddress(addressTextField.getText());
        clientEntity.setPostalCode(postalCodeTextField.getText());
        clientEntity.setCity(cityTextField.getText());
        clientApiService.edit(clientEntity);

    }

    private void handleClientAdding() {
        CreateClientDto dto = CreateClientDto.builder()
                .name(nameTextField.getText())
                .surname(surnameTextField.getText())
                .email(emailTextField.getText())
                .address(addressTextField.getText())
                .postalCode(postalCodeTextField.getText())
                .city(cityTextField.getText())
                .build();
        clientApiService.create(dto);
    }

    private void showMandatoryLabelsNotFilledDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(dialogWarningHeader);
        alert.setHeaderText(dialogMandatoryFieldsNotFilledHeader);
        alert.setContentText(dialogMandatoryFieldsNotFilledContent);
        alert.showAndWait();
    }

    private void showEmailTakenDialog() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(dialogWarningHeader);
        alert.setHeaderText(dialogEmailTakenHeader);
        alert.setContentText(dialogEmailTakenContent);
        alert.showAndWait();
    }

    private void showSuccessDialog() {
        String clientAction;
        if (formAction == ClientFormAction.ADD) {
            clientAction = dialogActionClientCreated;
        } else {
            clientAction = dialogActionClientEdited;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(dialogSuccessHeader);
        alert.setHeaderText(dialogSuccessHeader);
        alert.setContentText(dialogSuccessContent + clientAction + ".");
        alert.showAndWait();
    }

    @Subscribe
    public void fillEditedTextFields(FillEditedTextFieldsEvent event) {
        ClientEntity entity = event.getProcessedEntity();
        nameTextField.setText(entity.getName());
        surnameTextField.setText(entity.getSurname());
        emailTextField.setText(entity.getEmail());
        addressTextField.setText(entity.getAddress());
        postalCodeTextField.setText(entity.getPostalCode());
        cityTextField.setText(entity.getCity());
    }

    @Override
    public void setLanguage(ChangeLanguageEvent event) {
        initializeLabels();
    }

    private void initializeLabels() {
        nameLabel.setText(getValue(Bundles.CLIENT_FORM_NAME_LABEL));
        surnameLabel.setText(getValue(Bundles.CLIENT_FORM_SURNAME_LABEL));
        emailLabel.setText(getValue(Bundles.CLIENT_FORM_EMAIL_LABEL));
        addressLabel.setText(getValue(Bundles.CLIENT_FORM_ADDRESS_LABEL));
        postalCodeLabel.setText(getValue(Bundles.CLIENT_FORM_POSTAL_CODE_LABEL));
        cityLabel.setText(getValue(Bundles.CLIENT_FORM_CITY_LABEL));
        cancelButton.setText(getValue(Bundles.CLIENT_FORM_CANCEL_BUTTON));
        confirmButton.setText(getValue(Bundles.CLIENT_FORM_CONFIRM_BUTTON));
        dialogActionClientCreated = getValue(Bundles.CLIENT_FORM_DIALOG_CLIENT_ACTION_CREATED);
        dialogActionClientEdited = getValue(Bundles.CLIENT_FORM_DIALOG_CLIENT_ACTION_EDITED);
        dialogEmailTakenHeader = getValue(Bundles.CLIENT_FORM_DIALOG_EMAIL_TAKEN_HEADER);
        dialogEmailTakenContent = getValue(Bundles.CLIENT_FORM_DIALOG_EMAIL_TAKEN_CONTENT);
        dialogMandatoryFieldsNotFilledHeader = getValue(Bundles.CLIENT_FORM_DIALOG_MANDATORY_FIELDS_NOT_FILLED_HEADER);
        dialogMandatoryFieldsNotFilledContent = getValue(Bundles.CLIENT_FORM_DIALOG_MANDATORY_FIELDS_NOT_FILLED_CONTENT);
        dialogSuccessContent = getValue(Bundles.CLIENT_FORM_DIALOG_SUCCESS_CONTENT);
        dialogSuccessHeader = getValue(Bundles.UTILS_DIALOG_SUCCESS);
        dialogWarningHeader = getValue(Bundles.UTILS_DIALOG_WARNING);
    }

    private String getValue(Bundles bundle) {
        ResourceBundle resourceBundle = settingsService.getResourceBundle();
        return resourceBundle.getString(bundle.value);
    }
}
