package pojo.event.value.children;

import pojo.Message;
import enums.EventValueType;
import pojo.event.value.EventValueBase;

/**
 * The resolve event
 * see detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#ddl-type-code
 */
public class EventValueResolve extends EventValueBase {

    public EventValueResolve(Message message) {
        super(EventValueType.resolved, message);
    }

}
