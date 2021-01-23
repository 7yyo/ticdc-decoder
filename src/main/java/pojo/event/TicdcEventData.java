package pojo.event;

import pojo.event.key.TicdcEventKey;
import pojo.event.value.TicdcEventValueBase;

/**
 * The TiCDC event structure
 * should be key & value
 */
public class TicdcEventData {

    // TiCDC Event key
    private TicdcEventKey ticdcEventKey;
    // TiCDC Event value
    private TicdcEventValueBase ticdcEventValue;

    public TicdcEventData(TicdcEventKey ticdcEventKey, TicdcEventValueBase ticdcEventValue) {
        this.ticdcEventKey = ticdcEventKey;
        this.ticdcEventValue = ticdcEventValue;
    }

    public TicdcEventKey getTicdcEventKey() {
        return ticdcEventKey;
    }

    public void setTicdcEventKey(TicdcEventKey ticdcEventKey) {
        this.ticdcEventKey = ticdcEventKey;
    }

    public TicdcEventValueBase getTicdcEventValue() {
        return ticdcEventValue;
    }

    public void setTicdcEventValue(TicdcEventValueBase ticdcEventValue) {
        this.ticdcEventValue = ticdcEventValue;
    }

}
