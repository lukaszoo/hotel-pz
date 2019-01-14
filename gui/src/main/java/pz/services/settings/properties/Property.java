package pz.services.settings.properties;

import lombok.Getter;

public enum Property {
    SERVER_ADDRESS("server.address"),
    APPLICATION_NAME("application.name");

    @Getter
    String value;

    Property(String value) {
        this.value = value;
    }
}
