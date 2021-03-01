package pojo.event.value;

import enums.EventValueType;
import pojo.Message;

/**
 * Base class of RowChange/DDL/Resolve event
 */
public class EventValueBase {

    private EventValueType type;

    public EventValueBase(EventValueType type, Message message) {
        this.type = type;
    }

    public EventValueType getType() {
        return type;
    }

    public void setType(EventValueType type) {
        this.type = type;
    }

}



