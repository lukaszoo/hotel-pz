package pz.model.database.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pz.model.database.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking")
public class BookingEntity extends AbstractEntity {
    private Integer apartmentId;
    private Integer clientId;
    private Double cost;
    private Boolean paid;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public boolean isBookingRangeColliding(LocalDate startDate, LocalDate endDate) {
        boolean isStartDateInRange = isDateColliding(startDate, this.startDate, this.endDate);
        boolean isEndDateInRange = isDateColliding(endDate, this.startDate, this.endDate);

        boolean isStartDateOutOfRange = isDateColliding(this.startDate, startDate, endDate);
        boolean isEndDateOutOfRange = isDateColliding(this.endDate, startDate, endDate);
        return (isStartDateInRange || isEndDateInRange)
                || (isStartDateOutOfRange || isEndDateOutOfRange);
    }

    public boolean bookingRangeContains(LocalDate date) {
        return isDateColliding(date, this.startDate, this.endDate);
    }

    private Boolean isDateColliding(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return (date.equals(startDate) || date.isAfter(startDate))
                && (date.equals(endDate) || date.isBefore(endDate));
    }
}
