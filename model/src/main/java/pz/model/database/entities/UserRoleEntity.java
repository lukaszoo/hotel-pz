package pz.model.database.entities;

import lombok.Data;
import pz.model.UserRole;
import pz.model.database.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "userRole")
public class UserRoleEntity extends AbstractEntity {
    @Enumerated(EnumType.STRING)
    private UserRole name;
    private String description;
}
