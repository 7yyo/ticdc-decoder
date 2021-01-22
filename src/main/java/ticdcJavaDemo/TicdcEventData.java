package ticdcJavaDemo;

import ticdcJavaDemo.key.TicdcEventKey;
import ticdcJavaDemo.value.TicdcEventBase;

/**
 *  @TicdcEventData.java 定义了 event 的内容
 */
public class TicdcEventData {

    // 解析后的 Ticdc Event Key
    private TicdcEventKey ticdcEventKey;
    // 解析后的 Ticdc Event Value
    private TicdcEventBase ticdcEventValue;

    public TicdcEventData() {
    }

    public TicdcEventData(TicdcEventKey ticdcEventKey, TicdcEventBase ticdcEventValue) {
        this.ticdcEventKey = ticdcEventKey;
        this.ticdcEventValue = ticdcEventValue;
    }

    public TicdcEventKey getTicdcEventKey() {
        return ticdcEventKey;
    }

    public void setTicdcEventKey(TicdcEventKey ticdcEventKey) {
        this.ticdcEventKey = ticdcEventKey;
    }

    public TicdcEventBase getTicdcEventValue() {
        return ticdcEventValue;
    }

    public void setTicdcEventValue(TicdcEventBase ticdcEventValue) {
        this.ticdcEventValue = ticdcEventValue;
    }

}
