package pz.services.api.user;

import pz.model.database.entities.UserEntity;
import pz.model.integration.AuthenticateUserDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApiInterface {
    @POST("/user/auth")
    public Call<UserEntity> authenticateUser(@Body AuthenticateUserDto dto);
}
