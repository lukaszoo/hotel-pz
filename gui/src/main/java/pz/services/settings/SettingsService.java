package pz.services.settings;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pz.model.database.entities.UserEntity;
import pz.services.events.*;
import pz.services.settings.language.Language;
import pz.services.settings.skin.Skin;
import pz.services.settings.skin.SkinService;

import java.util.ResourceBundle;

@Slf4j
public class SettingsService implements Subscriber {
    private static SettingsService instance;
    @Getter
    @Setter
    private Skin currentSkin;
    @Getter
    @Setter
    private Language currentLanguage;
    @Getter
    ResourceBundle resourceBundle;
    @Getter
    @Setter
    UserEntity currentlyLoggedUser;
    SkinService skinService;

    private SettingsService() {
        this.currentSkin = Skin.DEFAULT;
        this.currentLanguage = Language.ENGLISH;
        this.resourceBundle = ResourceBundle.getBundle("language", currentLanguage.locale);
        this.skinService = SkinService.getInstance();
        ViewEventBus.subscribe(this);
    }

    public static synchronized SettingsService getInstance() {
        if (instance == null) {
            instance = new SettingsService();
        }
        return instance;
    }

    public void apply() {
        resourceBundle = ResourceBundle.getBundle("language", currentLanguage.locale);
        Event event = new ChangeLanguageEvent();
        ViewEventBus.publish(event);
        Event changeSkinEvent = new ChangeSkinEvent(currentSkin);
        ViewEventBus.publish(changeSkinEvent);
    }
}
