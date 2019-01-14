package pz.services.api.booking;

import pz.model.database.entities.BookingEntity;
import pz.model.integration.Booking;
import pz.model.integration.CreateBookingDto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface BookingApiInterface {
    @GET("/booking/all")
    Call<List<Booking>> findAll();

    @POST("/booking/create")
    Call<Boolean> create(@Body CreateBookingDto dto);

    @POST("/booking/pay")
    Call<Boolean> pay(@Query("id") Integer id);

    @DELETE("/booking/delete")
    Call<Boolean> delete(@Query("id") Integer id);
}
