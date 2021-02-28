package pojo.event.value.children.RowChange;

import pojo.Message;
import enums.EventValueType;
import pojo.event.value.EventValueBase;

import java.util.List;

/**
 * The row change event
 * See detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#resolved-event
 */
public class EventValueRowChange extends EventValueBase {

    // Event type, Update or delete
    private String changeType;
    private List<EventColumn> oldColumns;
    private List<EventColumn> columns;

    public EventValueRowChange(Message message) {
        super(EventValueType.rowChange, message);
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public List<EventColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<EventColumn> columns) {
        this.columns = columns;
    }

    public List<EventColumn> getOldColumns() {
        return oldColumns;
    }

    public void setOldColumns(List<EventColumn> oldColumns) {
        this.oldColumns = oldColumns;
    }
}
