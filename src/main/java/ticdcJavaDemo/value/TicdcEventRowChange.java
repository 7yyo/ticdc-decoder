package ticdcJavaDemo.value;

import ticdcJavaDemo.KafkaMessage;

import java.util.List;

public class TicdcEventRowChange extends TicdcEventBase {

    private String updateOrDelete; // should be "u" or "d"
    private List<TicdcEventColumn> oldColumns;
    private List<TicdcEventColumn> columns;

    public TicdcEventRowChange(KafkaMessage kafkaMessage) {
        super(TicdcEventType.rowChange, kafkaMessage);
    }

    public String getUpdateOrDelete() {
        return updateOrDelete;
    }

    public void setUpdateOrDelete(String updateOrDelete) {
        this.updateOrDelete = updateOrDelete;
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
