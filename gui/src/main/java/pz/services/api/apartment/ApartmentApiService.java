package pz.services.api.apartment;

import lombok.extern.slf4j.Slf4j;
import pz.model.database.entities.ApartmentEntity;
import pz.model.integration.FindApartmentDto;
import pz.services.api.RetrofitService;
import pz.services.api.ServerApiException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
public class ApartmentApiService {
    private static ApartmentApiService instance;
    private RetrofitService retrofitService;
    private ApartmentApiInterface client;

    private ApartmentApiService() {
        retrofitService = RetrofitService.getInstance();
        client = retrofitService.getRetrofit()
                .create(ApartmentApiInterface.class);
    }

    public static synchronized ApartmentApiService getInstance() {
        if (instance == null) {
            instance = new ApartmentApiService();
        }
        return instance;
    }

    public Set<Integer> findOccupiedOnDate(LocalDate date) {
        try {
            return client.getOccupiedOnDate(date.toString()).execute().body();
        } catch (IOException e) {
            throw new ServerApiException(e);
        }
    }

    public List<ApartmentEntity> find(FindApartmentDto dto) {
        try {
            List<ApartmentEntity> result = client.find(dto).execute().body();
            log.debug("API call, request: {}, response {}", dto, result);
            return result;
        } catch (IOException e) {
            throw new ServerApiException(e);
        }
    }
}
