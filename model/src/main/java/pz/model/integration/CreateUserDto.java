package pz.model.integration;

import com.google.common.collect.Sets;
import lombok.Data;

import java.util.Set;

@Data
public class CreateUserDto implements DataTransferObject {
    private String username;
    private String password;
    private Set<Integer> userRoleIds = Sets.newHashSet();
}
