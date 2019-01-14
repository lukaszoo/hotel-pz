package pz.model.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindApartmentDto {
    private List<Integer> features;
    private String startDate;
    private String endDate;
    private Integer capacity;
}
