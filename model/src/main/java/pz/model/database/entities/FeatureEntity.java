package pz.model.database.entities;

import lombok.Data;
import pz.model.database.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "feature")
public class FeatureEntity extends AbstractEntity {
    private String name;
    private String description;
}
