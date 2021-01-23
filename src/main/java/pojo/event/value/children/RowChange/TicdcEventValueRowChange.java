package pojo.event.value.children.RowChange;

import pojo.event.value.TicdcEventValueBase;
import pojo.KafkaMessage;
import enums.TicdcEventValueType;

import java.util.List;

/**
 * The resolved event
 * see detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#resolved-event
 */
public class TicdcEventValueRowChange extends TicdcEventValueBase {

    // Event type, should be 'u' or 'd'. see detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#resolved-event
    private String changeType;
    // Because it is an update or delete statement, the data before modification is recorded here.
    private List<TicdcEventColumn> oldColumns;
    // Because it is an update or delete statement, the modified data is recorded here.
    private List<TicdcEventColumn> columns;

    public TicdcEventValueRowChange(KafkaMessage kafkaMessage) {
        super(TicdcEventValueType.rowChange, kafkaMessage);
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public List<TicdcEventColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<TicdcEventColumn> columns) {
        this.columns = columns;
    }

    public List<TicdcEventColumn> getOldColumns() {
        return oldColumns;
    }

    public void setOldColumns(List<TicdcEventColumn> oldColumns) {
        this.oldColumns = oldColumns;
    }
}
