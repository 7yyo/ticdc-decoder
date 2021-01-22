package ticdcJavaDemo.value;

/**
 * @TicdcEventColumn.java 定义了 event 中 column 的相关信息
 */
public class TicdcEventColumn {

    private int t;// 列类型,例如 int float 等， 参考 https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#column-type-code
    private boolean h; // 是否有 where 句柄
    private String name; // 列名
    private Object v; // 列值

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public boolean isH() {
        return h;
    }

    public void setH(boolean h) {
        this.h = h;
    }

    public Object getV() {
        return v;
    }

    public void setV(Object v) {
        this.v = v;
    }
}
