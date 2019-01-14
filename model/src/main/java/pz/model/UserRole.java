package pz.model;

import lombok.Getter;

public enum UserRole {
    ADMIN(1),
    USER(2);

    @Getter
    Integer id;

    UserRole(Integer id) {
        this.id = id;
    }
}
