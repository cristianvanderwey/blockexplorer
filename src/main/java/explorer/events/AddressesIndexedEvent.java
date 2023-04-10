package explorer.events;

import org.springframework.context.ApplicationEvent;

public class AddressesIndexedEvent extends ApplicationEvent {
    public AddressesIndexedEvent(Object source) {
        super(source);
    }

}