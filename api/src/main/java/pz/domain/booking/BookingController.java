package pz.domain.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pz.model.database.entities.BookingEntity;
import pz.model.database.repositories.BookingRepository;
import pz.model.integration.Booking;
import pz.model.integration.CalculateCostDto;
import pz.model.integration.CreateBookingDto;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingRepository bookingRepository, BookingService bookingService) {
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
    }

    @GetMapping("/all")
    public List<Booking> findAll() {
        return bookingService.findAll();
    }

    @GetMapping("/find")
    public List<BookingEntity> findByClientId(@RequestParam(name = "client_id") Integer clientId) {
        return bookingRepository.findByClientId(clientId);
    }

    @PostMapping("/create")
    public Boolean create(@RequestBody @Valid CreateBookingDto dto) {
        return bookingService.create(dto);
    }

    @PostMapping("/cost")
    public Double calculateCost(@RequestBody @Valid CalculateCostDto dto) {
        return bookingService.calculateCost(dto);
    }

    @PostMapping("/pay")
    public Boolean pay(@RequestParam("id") Integer bookingId) {
        return bookingService.setPaid(bookingId);
    }

    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam("id") Integer bookingId) {
        return bookingService.delete(bookingId);
    }
}
