package pojo.event;

import pojo.event.key.EventKey;
import pojo.event.value.EventValueBase;

/**
 * The ticdc event structure
 */
public class EventData {

    private EventKey k;
    private EventValueBase v;

    public EventData(EventKey k, EventValueBase v) {
        this.k = k;
        this.v = v;
    }

    public EventKey getK() {
        return k;
    }

    public void setK(EventKey k) {
        this.k = k;
    }

    public EventValueBase getV() {
        return v;
    }

    public void setV(EventValueBase v) {
        this.v = v;
    }
}
