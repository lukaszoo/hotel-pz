package pz.views;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.Subscribe;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pz.model.Feature;
import pz.model.database.entities.ApartmentEntity;
import pz.model.database.entities.ClientEntity;
import pz.model.integration.CreateBookingDto;
import pz.model.integration.FindApartmentDto;
import pz.services.api.apartment.ApartmentApiService;
import pz.services.api.booking.BookingApiService;
import pz.services.events.ChangeLanguageEvent;
import pz.services.events.ViewEventBus;
import pz.services.settings.SettingsService;
import pz.services.settings.language.Bundles;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static pz.services.settings.language.Bundles.*;

public class ApartmentSelectView implements View {
    private static final String RESOURCE = "apartmentSelectView.fxml";
    private ApartmentApiService apartmentApiService;
    private BookingApiService bookingApiService;
    private SettingsService settingsService;

    private static Stage apartmentSelectStage;

    private Map<CheckBox, Feature> featureCheckboxesMapping;
    public CheckBox tvCheckbox;
    public CheckBox balconyCheckbox;
    public CheckBox jaccuziCheckbox;
    public CheckBox wifiCheckbox;
    public CheckBox bathroomCheckbox;

    public Button showHotelMapButton;
    public Label startDateLabel;
    public DatePicker startDatePicker;
    public Label endDateLabel;
    public DatePicker endDatePicker;
    public Label capacityLabel;
    public Spinner<Integer> capacitySpinner;
    public Label totalCostLabel;
    public Label totalCostValueLabel;
    public TableView<ApartmentEntity> apartmentsTableView;
    public Button apartmentSelectButton;
    public Button goBackButton;

    private ApartmentEntity selectedApartment;
    private static ClientEntity selectedClient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewEventBus.subscribe(this);
        apartmentApiService = ApartmentApiService.getInstance();
        bookingApiService = BookingApiService.getInstance();
        settingsService = SettingsService.getInstance();

        featureCheckboxesMapping = initializeFeatureCheckboxesMapping();
        initializeApartmentsTableView();
        initializeCapacitySpinner(capacitySpinner);
        initializeDatePicker(startDatePicker);
        initializeDatePicker(endDatePicker);
        initializeApartmentSelectButton();
        showHotelMapButton.setOnMouseClicked(click -> HotelMapView.show());
        goBackButton.setOnMouseClicked(click -> ManageClientView.show(apartmentSelectStage, ClientManagerMode.BOOKING));
        setTotalCost();
        updateApartmentList();
        initializeLanguage();
    }

    private void initializeApartmentSelectButton() {
        apartmentSelectButton.setOnMouseClicked(click -> {
            if (Objects.nonNull(selectedApartment) && datePickerSelectionsValid()) {
                bookApartment();
            } else {
                showInvalidFormDialog();
            }
        });
    }

    public static void show(Stage stage, ClientEntity client) {
        selectedClient = client;
        if (Objects.nonNull(stage)) {
            apartmentSelectStage = stage;
        }
        try {
            Parent parent = FXMLLoader.load(ApartmentSelectView.class.getClassLoader().getResource(RESOURCE));
            apartmentSelectStage.setScene(new Scene(parent));
            apartmentSelectStage.setResizable(false);
            apartmentSelectStage.show();
        } catch (IOException e) {
            throw new ViewLoadingException(e);
        }
    }

    private void updateApartmentList() {
        if (startDatePicker.getValue().isAfter(endDatePicker.getValue())) {
            return;
        }
        apartmentsTableView.setItems(FXCollections.observableArrayList(findApartments()));
    }

    private List<ApartmentEntity> findApartments() {
        List<Integer> features = featureCheckboxesMapping.entrySet().stream()
                .filter(k -> k.getKey().isSelected())
                .map(Map.Entry::getValue)
                .map(Feature::getId)
                .collect(Collectors.toList());

        FindApartmentDto dto = FindApartmentDto.builder()
                .startDate(startDatePicker.getValue().toString())
                .endDate(endDatePicker.getValue().toString())
                .capacity(capacitySpinner.getValue())
                .features(features)
                .build();

        List<ApartmentEntity> searchResult = apartmentApiService.find(dto);
        return Objects.nonNull(searchResult) ? searchResult : Collections.emptyList();
    }

    private void initializeCapacitySpinner(Spinner<Integer> capacitySpinner) {
        capacitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3, 1));
        capacitySpinner.valueProperty().addListener((ov, oldVal, newVal) -> updateApartmentList());
    }

    private Map<CheckBox, Feature> initializeFeatureCheckboxesMapping() {
        Map<CheckBox, Feature> mapping = ImmutableMap.<CheckBox, Feature>builder()
                .put(tvCheckbox, Feature.TV)
                .put(jaccuziCheckbox, Feature.JACCUZI)
                .put(balconyCheckbox, Feature.BALCONY)
                .put(wifiCheckbox, Feature.WIFI)
                .put(bathroomCheckbox, Feature.BATHROOM)
                .build();
        mapping.keySet().forEach(checkbox -> checkbox.selectedProperty().addListener((ov, oldVal, newVal) -> updateApartmentList()));
        return mapping;
    }

    private void initializeDatePicker(DatePicker datePicker) {
        datePicker.setValue(LocalDate.now());
        datePicker.valueProperty().addListener((ov, oldVal, newVal) -> {
            if (datePickerSelectionsValid()) {
                apartmentSelectButton.setDisable(false);
                updateApartmentList();
            } else {
                apartmentSelectButton.setDisable(true);
                showInvalidFormDialog();
            }
        });
    }

    private boolean datePickerSelectionsValid() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        return startDate.equals(endDate) || startDate.isBefore(endDate);
    }

    private void showInvalidFormDialog() {
        String selectedApartmentNumber = Optional.ofNullable(selectedApartment)
                .map(ApartmentEntity::getId)
                .map(Objects::toString)
                .orElse(getValue(APARTMENT_SELECT_DIALOG_INVALID_FORM_DEFAULT_APARTMENT_NUMBER));

        String message = String.format(getValue(APARTMENT_SELECT_DIALOG_INVALID_FORM_CONTENT),
                selectedApartmentNumber.toString(),
                startDatePicker.getValue().toString(),
                endDatePicker.getValue().toString());
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(getValue(UTILS_DIALOG_WARNING));
        alert.setHeaderText(getValue(APARTMENT_SELECT_DIALOG_INVALID_FORM_HEADER));
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showBookingSuccessDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(getValue(UTILS_DIALOG_SUCCESS));
        alert.setHeaderText(getValue(APARTMENT_SELECT_DIALOG_BOOKING_SUCCESS_HEADER));
        alert.setContentText(getValue(APARTMENT_SELECT_DIALOG_BOOKING_SUCCESS_CONTENT));
        alert.showAndWait();
    }

    private void initializeApartmentsTableView() {
        TableColumn numberColumn = new TableColumn(getValue(APARTMENT_SELECT_COLUMN_NUMBER));
        numberColumn.setCellValueFactory(new PropertyValueFactory<ApartmentEntity, String>("id"));
        TableColumn floorColumn = new TableColumn(getValue(APARTMENT_SELECT_COLUMN_FLOOR));
        floorColumn.setCellValueFactory(new PropertyValueFactory<ApartmentEntity, String>("floor"));
        TableColumn capacityColumn = new TableColumn(getValue(APARTMENT_SELECT_COLUMN_CAPACITY));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<ApartmentEntity, String>("capacity"));
        TableColumn costColumn = new TableColumn(getValue(APARTMENT_SELECT_COLUMN_COST));
        costColumn.setCellValueFactory(new PropertyValueFactory<ApartmentEntity, String>("cost"));

        apartmentsTableView.getColumns().removeAll();
        apartmentsTableView.getColumns().addAll(numberColumn, floorColumn, capacityColumn, costColumn);
        apartmentsTableView.getSelectionModel().selectedItemProperty().addListener((ov, oldVal, newVal) -> {
            selectedApartment = newVal;
            setTotalCost();
        });
    }

    private void bookApartment() {
        CreateBookingDto dto = CreateBookingDto.builder()
                .apartmentId(selectedApartment.getId())
                .clientId(selectedClient.getId())
                .cost(Double.parseDouble(totalCostValueLabel.getText()))
                .startDate(startDatePicker.getValue().toString())
                .endDate(endDatePicker.getValue().toString())
                .build();

        bookingApiService.createBooking(dto);
        showBookingSuccessDialog();
        apartmentSelectStage.close();
    }

    private void setTotalCost() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Double totalCost;
        if (Objects.nonNull(startDate) && Objects.nonNull(endDate) && Objects.nonNull(selectedApartment)) {
            Double costPerDay = selectedApartment.getCost();
            Long days = ChronoUnit.DAYS.between(startDate, endDate) + 1L;
            totalCost = costPerDay * days;
        } else {
            totalCost = 0.0;
        }
        totalCostValueLabel.setText(totalCost.toString());
    }


    @Override
    @Subscribe
    public void setLanguage(ChangeLanguageEvent event) {
        initializeLanguage();
    }

    private void initializeLanguage() {
        apartmentSelectButton.setText(getValue(APARTMENT_SELECT_APARTMENT_SELECT_BUTTON));
        capacityLabel.setText(getValue(APARTMENT_SELECT_CAPACITY_LABEL));
        endDateLabel.setText(getValue(APARTMENT_SELECT_END_DATE_LABEL));
        startDateLabel.setText(getValue(APARTMENT_SELECT_START_DATE_LABEL));
        goBackButton.setText(getValue(APARTMENT_SELECT_GO_BACK_BUTTON));
        showHotelMapButton.setText(getValue(APARTMENT_SELECT_SHOW_HOTEL_MAP_BUTTON));
        totalCostLabel.setText(getValue(APARTMENT_SELECT_TOTAL_COST_LABEL));
        initializeApartmentsTableView();
    }

    private String getValue(Bundles bundle) {
        ResourceBundle resourceBundle = settingsService.getResourceBundle();
        return resourceBundle.getString(bundle.value);
    }
}
