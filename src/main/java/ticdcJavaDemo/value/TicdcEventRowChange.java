package ticdcJavaDemo.value;

import ticdcJavaDemo.KafkaMessage;

import java.util.List;

/**
 * @TicdcEventRowChange.java 定义了 Resolve 类型的 event：
 * 代表一行的数据变化，在行发生变更时该 Event 被发出，包含变更后该行的相关信息
 */
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
