package pz.services.api.client;

import pz.model.database.entities.ClientEntity;
import pz.model.integration.CreateClientDto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ClientApiInterface {
    @GET("/client/all")
    Call<List<ClientEntity>> findAll();

    @GET("/client/find")
    Call<Boolean> isEmailTaken(@Query("email") String email);

    @POST("/client/create")
    Call<ClientEntity> create(@Body CreateClientDto dto);

    @POST("/client/edit")
    Call<ClientEntity> edit(@Body ClientEntity entity);

    @DELETE("/client/delete")
    Call<Boolean> delete(@Query("id") Integer id);
}
