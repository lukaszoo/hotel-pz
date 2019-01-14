package pz.services.api.booking;

import pz.model.integration.Booking;
import pz.model.integration.CreateBookingDto;
import pz.services.api.RetrofitService;
import pz.services.api.ServerApiException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class BookingApiService {
    private final RetrofitService retrofitService;
    private final BookingApiInterface client;

    private static BookingApiService instance;

    private BookingApiService() {
        retrofitService = RetrofitService.getInstance();
        client = retrofitService.getRetrofit()
                .create(BookingApiInterface.class);
    }

    public static synchronized BookingApiService getInstance() {
        if (Objects.isNull(instance)) {
            instance = new BookingApiService();
        }
        return instance;
    }

    public Boolean createBooking(CreateBookingDto dto) {
        try {
            return client.create(dto).execute().body();
        } catch (IOException e) {
            throw new ServerApiException(e);
        }
    }

    public List<Booking> findAll() {
        try {
            return client.findAll().execute().body();
        } catch (IOException e) {
            throw new ServerApiException(e);
        }
    }

    public Boolean pay(Integer bookingId) {
        try {
            return client.pay(bookingId).execute().body();
        } catch (IOException e) {
            throw new ServerApiException(e);
        }
    }

    public Boolean delete(Integer bookingId) {
        try {
            return client.delete(bookingId).execute().body();
        } catch (IOException e) {
            throw new ServerApiException(e);
        }
    }
}
