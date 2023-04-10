package explorer.events;

import org.springframework.context.ApplicationEvent;

public class BlockHashReceivedEvent extends ApplicationEvent {
    private String hash;

    public BlockHashReceivedEvent(Object source, String hash) {
        super(source);
        this.hash = hash;
    }
    public String getHash() {
        return hash;
    }
}