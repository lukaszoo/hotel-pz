package pz.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pz.model.database.entities.UserEntity;
import pz.model.database.repositories.UserRepository;
import pz.model.integration.AuthenticateUserDto;
import pz.model.integration.CreateUserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @PostMapping("/create")
    public UserEntity createUser(@RequestBody @Valid CreateUserDto dto) {
        return userService.createUser(dto);
    }

    @PostMapping("/auth")
    public UserEntity authenticate(@RequestBody @Valid AuthenticateUserDto dto) {
        return userService.authenticate(dto);
    }
}
