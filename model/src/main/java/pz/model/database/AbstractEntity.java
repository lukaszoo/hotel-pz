package pz.model.database;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
    protected static final String ID_SEQUENCE_NAME = "ID_SEQUENCE";
    protected static final String ID_SEQUENCE_GENERATOR_NAME = "ID_SEQUENCE_GENERATOR";
    @Id
    @SequenceGenerator(name = ID_SEQUENCE_GENERATOR_NAME, sequenceName = ID_SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_SEQUENCE_GENERATOR_NAME)
    protected Integer id;
}
