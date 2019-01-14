package pz.model.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import pz.model.database.AbstractEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name = "apartment")
public class ApartmentEntity extends AbstractEntity {
    private Integer floor;
    private Integer capacity;
    private Double cost;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "equipment",
            joinColumns = @JoinColumn(name = "apartmentId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "featureId", referencedColumnName = "id"))
    Set<FeatureEntity> features;

    @JsonIgnore
    @OneToMany(mappedBy = "apartmentId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<BookingEntity> bookings;

    public Boolean isFreeInTimeRange(LocalDate startDate, LocalDate endDate) {
        return bookings.stream().noneMatch(booking -> booking.isBookingRangeColliding(startDate, endDate));
    }
}
