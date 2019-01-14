package pz.model;

import lombok.Getter;

public enum Feature {
    TV(1),
    JACCUZI(2),
    BALCONY(3),
    WIFI(4),
    BATHROOM(5);

    @Getter
    Integer id;

    Feature(Integer id) {
        this.id = id;
    }
}
