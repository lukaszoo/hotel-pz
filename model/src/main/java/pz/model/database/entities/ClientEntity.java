package pz.model.database.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pz.model.database.AbstractEntity;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client")
public class ClientEntity extends AbstractEntity {
    private String email;
    private String name;
    private String surname;
    private String address;
    private String postalCode;
    private String city;

}
