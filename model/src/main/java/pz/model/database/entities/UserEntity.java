package pz.model.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pz.model.UserRole;
import pz.model.database.AbstractEntity;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity extends AbstractEntity {
    private String username;
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "userRoleAssignment",
            joinColumns = @JoinColumn(name = "userId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "id"))
    Set<UserRoleEntity> userRoles;

    @JsonIgnore
    public boolean isAdmin() {
        return userRoles.stream().anyMatch(userRole -> userRole.getId().equals(UserRole.ADMIN.getId()));
    }
}
