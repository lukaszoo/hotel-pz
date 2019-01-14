package pz.model.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private Integer id;
    private Integer apartmentNumber;
    private String startDate;
    private String endDate;
    private String client;
    private String clientEmail;
    private Double totalCost;
    private Boolean isPaid;
}
