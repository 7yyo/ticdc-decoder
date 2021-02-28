package pojo.event;

import pojo.event.key.EventKey;
import pojo.event.value.EventValueBase;

/**
 * The ticdc event structure
 */
public class EventData {

    private EventKey eventKey;
    private EventValueBase eventValue;

    public EventData(EventKey eventKey, EventValueBase ticdcEventValue) {
        this.eventKey = eventKey;
        this.eventValue = ticdcEventValue;
    }

    public EventKey getEventKey() {
        return eventKey;
    }

    public void setEventKey(EventKey eventKey) {
        this.eventKey = eventKey;
    }

    public EventValueBase getEventValue() {
        return eventValue;
    }

    public void setEventValue(EventValueBase eventValue) {
        this.eventValue = eventValue;
    }
}
