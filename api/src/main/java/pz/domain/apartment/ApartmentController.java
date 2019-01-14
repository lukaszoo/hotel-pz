package pz.domain.apartment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pz.model.database.entities.ApartmentEntity;
import pz.model.integration.FindApartmentDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/apartment")
public class ApartmentController {
    private final ApartmentService apartmentService;

    @Autowired
    public ApartmentController(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }

    @PostMapping("/find")
    public List<ApartmentEntity> findApartment(@RequestBody FindApartmentDto dto) {
        return apartmentService.findApartments(dto);
    }

    @GetMapping("find")
    public Set<Integer> findByDate(@RequestParam(name = "date") String date) {
        return apartmentService.findOccupiedOnDate(LocalDate.parse(date));
    }
}
