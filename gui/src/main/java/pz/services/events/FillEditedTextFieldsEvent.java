package pz.services.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pz.model.database.entities.ClientEntity;

@Getter
@AllArgsConstructor
public class FillEditedTextFieldsEvent implements Event {
    private ClientEntity processedEntity;
}
