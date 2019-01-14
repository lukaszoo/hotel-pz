package pz.model.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pz.model.database.entities.ApartmentEntity;

@Repository
public interface ApartmentRepository extends JpaRepository<ApartmentEntity, Integer> {
}
