package pz.views;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pz.model.database.entities.BookingEntity;
import pz.model.integration.Booking;
import pz.services.api.booking.BookingApiService;
import pz.services.events.ChangeLanguageEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManageBookingView implements View {
    private static final String RESOURCE = "manageBookingView.fxml";
    private static Stage manageBookingStage;

    private BookingApiService bookingApiService;

    private static Booking selectedBooking;

    public TableView<Booking> bookingTableView;
    public Button payBookingButton;
    public Button cancelBookingButton;
    public Button closeButton;

    public static void show() {
        manageBookingStage = new Stage();
        try {
            Parent parent = FXMLLoader.load(ManageBookingView.class.getClassLoader().getResource(RESOURCE));
            manageBookingStage.setScene(new Scene(parent));
            manageBookingStage.setResizable(false);
            manageBookingStage.show();
        } catch (IOException e) {
            throw new ViewLoadingException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookingApiService = BookingApiService.getInstance();
        payBookingButton.setOnMouseClicked(click -> handleBookingPay());
        cancelBookingButton.setOnMouseClicked(click -> handleCancelBooking());
        closeButton.setOnMouseClicked(event -> manageBookingStage.close());
        initializeBookingTableView();
    }

    private void handleCancelBooking() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Operation is irreversible.", ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Cancel booking");
        alert.setHeaderText("Do you really want to cancel booking for client " + selectedBooking.getClient() + "?");
        alert.showAndWait();
        if (alert.getResult() != ButtonType.YES) {
            return;
        }
        bookingApiService.delete(selectedBooking.getId());
        updateBookingTableView();
        showBookingActionSuccessDialog(BookingAction.CANCEL);
    }

    private void handleBookingPay() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Total cost:" + selectedBooking.getTotalCost(), ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Pay booking");
        alert.setHeaderText("Do you really want to pay booking for client " + selectedBooking.getClient() + "?");
        alert.showAndWait();
        if (alert.getResult() != ButtonType.YES) {
            return;
        }
        bookingApiService.pay(selectedBooking.getId());
        updateBookingTableView();
        showBookingActionSuccessDialog(BookingAction.PAY);
    }

    private void initializeBookingTableView() {
        TableColumn<Booking, Integer> apartmentNumberColumn = new TableColumn<>("Apartment number");
        apartmentNumberColumn.setCellValueFactory(new PropertyValueFactory<>("apartmentNumber"));
        TableColumn<Booking, String> startDateColumn = new TableColumn<>("Start date");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        TableColumn<Booking, String> endDateColumn = new TableColumn<>("End date");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        TableColumn<Booking, String> clientColumn = new TableColumn<>("Client");
        clientColumn.setCellValueFactory(new PropertyValueFactory<>("client"));
        TableColumn<Booking, String> clientEmailColumn = new TableColumn<>("Email");
        clientEmailColumn.setCellValueFactory(new PropertyValueFactory<>("clientEmail"));
        TableColumn<Booking, Double> totalCostColumn = new TableColumn<>("totalCost");
        totalCostColumn.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        TableColumn<Booking, Boolean> isPaidColumn = new TableColumn<>("Is paid?");
        isPaidColumn.setCellValueFactory(new PropertyValueFactory<>("isPaid"));

        bookingTableView.getColumns().addAll(apartmentNumberColumn, startDateColumn, endDateColumn,
                clientColumn, clientEmailColumn, totalCostColumn, isPaidColumn);
        bookingTableView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> selectedBooking = newValue));
        updateBookingTableView();
    }

    private void updateBookingTableView() {
        bookingTableView.setItems(FXCollections.observableArrayList(bookingApiService.findAll()));
    }

    @Override
    public void setLanguage(ChangeLanguageEvent event) {

    }

    public void showBookingActionSuccessDialog(BookingAction action) {
        String performedAction = "";
        switch(action) {
            case PAY:
                performedAction = "paid";
                break;
            case CANCEL:
                performedAction = "cancelled";
                break;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Success!");
        alert.setContentText("Booking has been succesfully " + performedAction + ".");
        alert.showAndWait();
    }


    public void showBookingCancelConfirmationDialog() {

    }
}
