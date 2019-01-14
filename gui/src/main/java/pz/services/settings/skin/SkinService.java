package pz.services.settings.skin;

import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import pz.services.events.ChangeSkinEvent;
import pz.services.events.Subscriber;
import pz.services.events.ViewEventBus;

import java.util.Objects;

public class SkinService implements Subscriber {
    private static SkinService instance;

    private SkinService() {
        ViewEventBus.subscribe(this);
    }

    public static synchronized SkinService getInstance() {
        if (Objects.isNull(instance)) {
            instance = new SkinService();
        }
        return instance;
    }

    @Subscribe
    public void changeSkin(ChangeSkinEvent event) {
        switch (event.getSkin()) {
            case DEFAULT:
                Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
                break;
            case HIGH_CONTRAST:
                Application.setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
                break;
        }

    }
}
