package pz.views;

import com.google.common.collect.ImmutableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import pz.services.api.apartment.ApartmentApiService;
import pz.services.events.ChangeLanguageEvent;
import pz.services.events.ViewEventBus;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class HotelMapView implements View {
    private static final String RESOURCE = "hotelMapView.fxml";
    private static Stage hotelMapStage;
    private static Map<Integer, Rectangle> roomRectangleMapping;
    private ApartmentApiService apartmentApiService;

    public DatePicker datePicker;
    public Rectangle room1Rectangle;
    public Rectangle room2Rectangle;
    public Rectangle room3Rectangle;
    public Rectangle room4Rectangle;
    public Rectangle room5Rectangle;
    public Rectangle room6Rectangle;
    public Rectangle room7Rectangle;
    public Rectangle room8Rectangle;
    public Rectangle room9Rectangle;
    public Rectangle room10Rectangle;

    public static void show() {
        try {
            Parent parent = FXMLLoader.load(LoginView.class.getClassLoader().getResource(RESOURCE));
            hotelMapStage.setScene(new Scene(parent));
            hotelMapStage.setResizable(false);
            hotelMapStage.show();
        } catch (IOException e) {
            throw new ViewLoadingException(e);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewEventBus.subscribe(this);
        apartmentApiService = ApartmentApiService.getInstance();

        hotelMapStage = new Stage();
        roomRectangleMapping = initializeMapping();
        datePicker.valueProperty().addListener((ov, oldVal, newVal) -> this.updateRectangles(newVal));
        datePicker.setValue(LocalDate.now());
    }

    private void updateRectangles(LocalDate date) {
        Set<Integer> occupiedRooms = apartmentApiService.findOccupiedOnDate(date);
        roomRectangleMapping.forEach((k, v) -> v.setFill(occupiedRooms.contains(k) ? Color.RED : Color.GREEN));
    }

    @Override
    public void setLanguage(ChangeLanguageEvent event) {
    }

    private Map<Integer, Rectangle> initializeMapping() {
        return ImmutableMap.<Integer, Rectangle>builder()
                .put(1, room1Rectangle)
                .put(2, room2Rectangle)
                .put(3, room3Rectangle)
                .put(4, room4Rectangle)
                .put(5, room5Rectangle)
                .put(6, room6Rectangle)
                .put(7, room7Rectangle)
                .put(8, room8Rectangle)
                .put(9, room9Rectangle)
                .put(10, room10Rectangle)
                .build();
    }

}
