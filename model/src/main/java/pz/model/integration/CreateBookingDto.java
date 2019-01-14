package pz.model.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingDto implements DataTransferObject {
    @NotNull
    private Integer apartmentId;
    @NotNull
    private Integer clientId;
    @NotNull
    private String startDate;
    @NotNull
    private String endDate;
    private Double cost;
}
