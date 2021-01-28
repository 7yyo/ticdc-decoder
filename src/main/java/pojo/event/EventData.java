package pojo.event;

import pojo.event.key.EventKey;
import pojo.event.value.EventValueBase;

/**
 * The TiCDC event structure
 * Should be key & value
 */
public class EventData {

    // Event key
    private EventKey eventKey;
    // Event value
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
