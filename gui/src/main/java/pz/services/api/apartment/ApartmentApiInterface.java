package pz.services.api.apartment;

import pz.model.database.entities.ApartmentEntity;
import pz.model.integration.FindApartmentDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;
import java.util.Set;

public interface ApartmentApiInterface {
    @GET("/apartment/find")
    public Call<Set<Integer>> getOccupiedOnDate(@Query("date") String date);

    @POST("/apartment/find")
    public Call<List<ApartmentEntity>> find(@Body FindApartmentDto dto);
}
