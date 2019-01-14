package pz.model.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculateCostDto implements DataTransferObject {
    private Integer apartmentId;
    private LocalDate startDate;
    private LocalDate endDate;
}
