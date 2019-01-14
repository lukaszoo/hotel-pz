package pz.model.integration;

import lombok.Data;

@Data
public class AuthenticateUserDto implements DataTransferObject {
    private String username;
    private String password;
}
