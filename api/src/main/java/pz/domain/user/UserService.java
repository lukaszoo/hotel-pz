package pz.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pz.model.UserRole;
import pz.model.database.entities.UserEntity;
import pz.model.database.entities.UserRoleEntity;
import pz.model.database.repositories.UserRepository;
import pz.model.database.repositories.UserRoleRepository;
import pz.model.integration.AuthenticateUserDto;
import pz.model.integration.CreateUserDto;
import pz.utils.ServerException;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public UserEntity authenticate(AuthenticateUserDto dto) {
        UserEntity userEntity = userRepository.findByUsername(dto.getUsername());
        if (Objects.nonNull(userEntity)) {
            if (dto.getPassword().equals(userEntity.getPassword())) {
                return userEntity;
            }
        }
        return null;
    }

    public UserEntity createUser(CreateUserDto dto) {
        if (Objects.nonNull(userRepository.findByUsername(dto.getUsername()))) {
            log.error("Username {} is already taken.", dto.getUsername());
            throw new ServerException("Username \"" + dto.getUsername() + "\" is already taken.");
        }
        Set<Integer> userRoles = dto.getUserRoleIds();
        if (userRoles.isEmpty()) {
            log.warn("No user roles assigned, creating default user.");
            userRoles.add(UserRole.USER.getId());
        }
        Set<UserRoleEntity> userRoleEntities = userRoles.stream()
                .map(userRoleRepository::getOne)
                .collect(Collectors.toSet());
        return userRepository.save(UserEntity.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .userRoles(userRoleEntities)
                .build());
    }

}
