package pz.domain.booking

import pz.model.database.entities.BookingEntity
import pz.model.database.repositories.ApartmentRepository
import pz.model.database.repositories.BookingRepository
import pz.model.integration.VerifyOccupiedDto
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

class BookingServiceSpec extends Specification {
    private static final Integer APARTMENT_ID = 1
    def bookingRepository = Mock(BookingRepository)
    def apartmentRepository = Mock(ApartmentRepository)
    @Subject
    def service = new BookingService(bookingRepository, apartmentRepository, clientRepository, apartmentService)

    def "should return true when apartment is free"() {
        given:
        def givenDto = createDto('1999-12-27', '1999-12-29')
        apartmentIsOccupiedOnNewYearsEve()
        when:
        def result = service.isFree(givenDto)
        then:
        result == true
    }

    def "should return false when apartment dates collide on left"() {
        given:
        def givenDto = createDto('1999-12-01', '1999-12-31')
        apartmentIsOccupiedOnNewYearsEve()
        when:
        def result = service.isFree(givenDto)
        then:
        result == false
    }

    def "should return false when apartment dates collide on right"() {
        given:
        def givenDto = createDto('2000-01-01', '2000-01-31')
        apartmentIsOccupiedOnNewYearsEve()
        when:
        def result = service.isFree(givenDto)
        then:
        result == false
    }

    def "should return false when booking out of range"() {
        given:
        def givenDto = createDto('1999-12-31', '2000-01-01')
        apartmentIsOccupiedOnNewYearsEve()
        when:
        def result = service.isFree(givenDto)
        then:
        result == false
    }

    def "should return false when booking in range"() {
        given:
        def givenDto = createDto('1999-12-29', '2000-01-03')
        apartmentIsOccupiedOnNewYearsEve()
        when:
        def result = service.isFree(givenDto)
        then:
        result == false
    }

    private void apartmentIsOccupiedOnNewYearsEve() {
        bookingRepository.findByApartmentId(APARTMENT_ID) >> [
                createBooking("1999-12-30", "2000-01-02")
        ]
    }

    private static VerifyOccupiedDto createDto(String startDate, String endDate) {
        def dto = new VerifyOccupiedDto()
        dto.setApartmentId(APARTMENT_ID)
        dto.setStartDate(LocalDate.parse(startDate))
        dto.setEndDate(LocalDate.parse(endDate))
        return dto
    }

    private static BookingEntity createBooking(String startDate, String endDate) {
        return BookingEntity.builder()
                .apartmentId(APARTMENT_ID)
                .startDate(LocalDate.parse(startDate))
                .endDate(LocalDate.parse(endDate))
                .build()
    }
}
