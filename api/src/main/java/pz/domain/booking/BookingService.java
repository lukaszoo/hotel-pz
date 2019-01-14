package pz.domain.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pz.domain.apartment.ApartmentService;
import pz.model.database.entities.ApartmentEntity;
import pz.model.database.entities.BookingEntity;
import pz.model.database.entities.ClientEntity;
import pz.model.database.repositories.ApartmentRepository;
import pz.model.database.repositories.BookingRepository;
import pz.model.database.repositories.ClientRepository;
import pz.model.integration.Booking;
import pz.model.integration.CalculateCostDto;
import pz.model.integration.CreateBookingDto;
import pz.utils.ServerException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ApartmentRepository apartmentRepository;
    private final ClientRepository clientRepository;
    private final ApartmentService apartmentService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ApartmentRepository apartmentRepository, ClientRepository clientRepository, ApartmentService apartmentService) {
        this.bookingRepository = bookingRepository;
        this.apartmentRepository = apartmentRepository;
        this.clientRepository = clientRepository;
        this.apartmentService = apartmentService;
    }

    public Boolean create(CreateBookingDto dto) {
        LocalDate startDate = LocalDate.parse(dto.getStartDate());
        LocalDate endDate = LocalDate.parse(dto.getEndDate());
        if (!apartmentService.isFree(dto.getApartmentId(), startDate, endDate)) {
            throw new ServerException("Apartment is occupied in time range (" + dto.getStartDate() + "," + dto.getEndDate() + ").");
        }
        if (startDate.isAfter(endDate)) {
            throw new ServerException("Invalid time interval - start date can't be after the end date.");
        }
        Double cost = dto.getCost();
        if (cost == null || cost <= 0.0) {
            cost = calculateCost(CalculateCostDto.builder()
                    .apartmentId(dto.getApartmentId())
                    .startDate(startDate)
                    .endDate(endDate)
                    .build());
        }
        BookingEntity bookingEntity = BookingEntity.builder()
                .apartmentId(dto.getApartmentId())
                .clientId(dto.getClientId())
                .cost(cost)
                .paid(false)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        bookingRepository.save(bookingEntity);
        return true;
    }

    public Double calculateCost(CalculateCostDto dto) {
        Long days = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) + 1L;
        ApartmentEntity apartmentEntity = apartmentRepository.getOne(dto.getApartmentId());
        return apartmentEntity.getCost() * days;
    }

    public Boolean setPaid(Integer id) {
        BookingEntity entity = bookingRepository.getOne(id);
        entity.setPaid(true);
        bookingRepository.save(entity);
        return true;
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll()
                .stream()
                .map(this::mapEntity)
                .collect(Collectors.toList());
    }

    private Booking mapEntity(BookingEntity bookingEntity) {
        ClientEntity client = clientRepository.getOne(bookingEntity.getClientId());
        return Booking.builder()
                .id(bookingEntity.getId())
                .apartmentNumber(bookingEntity.getApartmentId())
                .startDate(bookingEntity.getStartDate().toString())
                .endDate(bookingEntity.getEndDate().toString())
                .client(client.getName() + " " + client.getSurname())
                .clientEmail(client.getEmail())
                .totalCost(bookingEntity.getCost())
                .isPaid(bookingEntity.getPaid())
                .build();
    }

    public Boolean delete(Integer bookingId) {
        bookingRepository.deleteById(bookingId);
        return true;
    }
}
