package pz.model.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pz.model.database.entities.FeatureEntity;

@Repository
public interface FeatureRepository extends JpaRepository<FeatureEntity, Integer> {
}
