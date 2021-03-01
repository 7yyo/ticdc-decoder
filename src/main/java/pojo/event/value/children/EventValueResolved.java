package pojo.event.value.children;

import pojo.Message;
import enums.EventValueType;
import pojo.event.value.EventValueBase;

/**
 * The resolve event
 * See detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#ddl-type-code
 */
public class EventValueResolved extends EventValueBase {

    public EventValueResolved(Message message) {
        super(EventValueType.RESOLVED, message);
    }

}
