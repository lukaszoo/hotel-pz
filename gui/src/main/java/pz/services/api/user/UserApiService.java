package pz.services.api.user;

import lombok.extern.slf4j.Slf4j;
import pz.model.database.entities.UserEntity;
import pz.model.integration.AuthenticateUserDto;
import pz.services.api.RetrofitService;

import java.io.IOException;

@Slf4j
public class UserApiService {
    private static UserApiService instance;
    private UserApiInterface client;
    private RetrofitService retrofitService;

    private UserApiService() {
        retrofitService = RetrofitService.getInstance();
        client = retrofitService.getRetrofit()
                .create(UserApiInterface.class);
    }

    public static synchronized UserApiService getInstance() {
        if(instance == null) {
            instance = new UserApiService();
        }
        return instance;
    }

    public UserEntity authenticateUser(AuthenticateUserDto dto) throws UserAuthenticationException {
        try {
            return client.authenticateUser(dto).execute().body();
        } catch (IOException e) {
            throw new UserAuthenticationException(e);
        }
    }

}
