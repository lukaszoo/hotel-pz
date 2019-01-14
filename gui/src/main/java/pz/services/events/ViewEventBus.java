package pz.services.events;

import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ViewEventBus {
    private static final EventBus eventBus;
    private static ViewEventBus instance;

    static {
        eventBus = new EventBus();
    }

    public static synchronized ViewEventBus getInstance() {
        if (instance == null) {
            instance = new ViewEventBus();
        }
        return instance;
    }

    public static void subscribe(Subscriber subscriber) {
        eventBus.register(subscriber);
    }

    public static void publish(Event event) {
        eventBus.post(event);
        log.debug("Event {} published.", event);
    }
}
