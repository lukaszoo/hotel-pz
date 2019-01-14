package pz.domain.apartment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pz.model.database.entities.ApartmentEntity;
import pz.model.database.entities.BookingEntity;
import pz.model.database.entities.FeatureEntity;
import pz.model.database.repositories.ApartmentRepository;
import pz.model.database.repositories.BookingRepository;
import pz.model.database.repositories.FeatureRepository;
import pz.model.integration.FindApartmentDto;
import pz.model.integration.VerifyOccupiedDto;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;
    private final FeatureRepository featureRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public ApartmentService(ApartmentRepository apartmentRepository, FeatureRepository featureRepository, BookingRepository bookingRepository) {
        this.apartmentRepository = apartmentRepository;
        this.featureRepository = featureRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<ApartmentEntity> findApartments(FindApartmentDto dto) {
        LocalDate startDate = LocalDate.parse(dto.getStartDate());
        LocalDate endDate = LocalDate.parse(dto.getStartDate());
        List<FeatureEntity> features = Optional.ofNullable(dto.getFeatures())
                .orElse(Collections.emptyList())
                .stream()
                .map(featureRepository::getOne)
                .collect(Collectors.toList());

        return apartmentRepository.findAll().stream()
                .filter(apartment -> apartment.getCapacity() >= dto.getCapacity())
                .filter(apartment -> apartment.getFeatures().containsAll(features))
                .filter(apartment -> isFree(apartment.getId(), startDate, endDate))
                .collect(Collectors.toList());
    }

    public Set<Integer> findOccupiedOnDate(LocalDate localDate) {
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.bookingRangeContains(localDate))
                .map(BookingEntity::getApartmentId)
                .collect(Collectors.toSet());
    }

    public Boolean isFree(VerifyOccupiedDto dto) {
        return isFree(dto.getApartmentId(), dto.getStartDate(), dto.getEndDate());
    }

    public Boolean isFree(Integer apartmentId, LocalDate startDate, LocalDate endDate) {
        List<BookingEntity> apartmentBookings = bookingRepository.findByApartmentId(apartmentId);
        return apartmentBookings.stream().noneMatch(booking -> booking.isBookingRangeColliding(startDate, endDate));
    }
}
