package pz.services.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pz.services.settings.skin.Skin;

@Getter
@AllArgsConstructor
public class ChangeSkinEvent implements Event {
    private Skin skin;
}
