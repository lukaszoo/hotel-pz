package pz.model.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pz.model.database.entities.BookingEntity;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {
    List<BookingEntity> findByClientId(Integer clientId);
    List<BookingEntity> findByApartmentId(Integer apartmentId);
}
