package pz.domain.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pz.model.database.entities.ClientEntity;
import pz.model.database.repositories.ClientRepository;
import pz.model.integration.CreateClientDto;
import pz.utils.ServerException;

import java.util.Objects;

@Service
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientEntity registerClient(CreateClientDto dto) {
        if (isEmailTaken(dto.getEmail())) {
            throw new ServerException("This email has already been registered: " + dto.getEmail() + ").");
        }
        ClientEntity entity = ClientEntity.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .surname(dto.getSurname())
                .address(dto.getAddress())
                .postalCode(dto.getPostalCode())
                .city(dto.getCity())
                .build();

        return clientRepository.save(entity);
    }

    public Boolean isEmailTaken(String email) {
        return Objects.nonNull(clientRepository.findByEmail(email));
    }

    public ClientEntity edit(ClientEntity clientEntity) {
        ClientEntity editedEntity = clientRepository.getOne(clientEntity.getId());
        if(Objects.isNull(editedEntity)) {
            throw new ServerException("Edited client entity does not exist!");
        }
        editedEntity.setName(clientEntity.getName());
        editedEntity.setSurname(clientEntity.getSurname());
        editedEntity.setEmail(clientEntity.getEmail());
        editedEntity.setAddress(clientEntity.getAddress());
        editedEntity.setPostalCode(clientEntity.getPostalCode());
        editedEntity.setCity(clientEntity.getCity());

        return clientRepository.save(editedEntity);
    }

    public Boolean delete(Integer id) {
        clientRepository.deleteById(id);
        return true;
    }
}
