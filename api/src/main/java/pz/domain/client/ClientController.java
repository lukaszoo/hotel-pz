package pz.domain.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pz.model.database.entities.ClientEntity;
import pz.model.database.repositories.ClientRepository;
import pz.model.integration.CreateClientDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientRepository clientRepository;
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientRepository clientRepository, ClientService clientService) {
        this.clientRepository = clientRepository;
        this.clientService = clientService;
    }

    @GetMapping("/all")
    public List<ClientEntity> findAll() {
        return clientRepository.findAll();
    }

    @GetMapping("/find")
    public Boolean isEmailTaken(@RequestParam(name = "email") String email) {
        return clientService.isEmailTaken(email);
    }

    @PostMapping("/create")
    public ClientEntity create(@RequestBody @Valid CreateClientDto dto) {
        return clientService.registerClient(dto);
    }

    @PostMapping("/edit")
    public ClientEntity edit(@RequestBody ClientEntity clientEntity) {
        return clientService.edit(clientEntity);
    }

    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam("id") Integer id) {
        return clientService.delete(id);
    }

}
