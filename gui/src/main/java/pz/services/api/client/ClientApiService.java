package pz.services.api.client;

import pz.model.database.entities.ClientEntity;
import pz.model.integration.CreateClientDto;
import pz.services.api.RetrofitService;
import pz.services.api.ServerApiException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ClientApiService {
    private final RetrofitService retrofitService;
    private final ClientApiInterface client;

    private static ClientApiService instance;

    private ClientApiService() {
        retrofitService = RetrofitService.getInstance();
        client = retrofitService.getRetrofit()
                .create(ClientApiInterface.class);
    }

    public static synchronized ClientApiService getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ClientApiService();
        }
        return instance;
    }

    public List<ClientEntity> getClientsList() {
        try {
            return client.findAll().execute().body();
        } catch (IOException e) {
            throw new ServerApiException(e);
        }
    }

    public Boolean isEmailTaken(String email) {
        try {
            return client.isEmailTaken(email).execute().body();
        } catch (IOException e) {
            throw new ServerApiException(e);
        }
    }

    public ClientEntity create(CreateClientDto dto) {
        try {
            return client.create(dto).execute().body();
        } catch (IOException e) {
            throw new ServerApiException(e);
        }
    }

    public ClientEntity edit(ClientEntity entity) {
        try {
            return client.edit(entity).execute().body();
        } catch (IOException e) {
            throw new ServerApiException(e);
        }
    }

    public Boolean delete(Integer id) {
        try {
            return client.delete(id).execute().body();
        } catch (IOException e) {
            throw new ServerApiException(e);
        }
    }
}
