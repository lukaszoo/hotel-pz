package pz.model.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientDto implements DataTransferObject {
    @NotNull
    private String email;
    private String name;
    private String surname;
    private String address;
    private String postalCode;
    private String city;
}
