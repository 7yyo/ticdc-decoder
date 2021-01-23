package pojo.event.value.children.RowChange;

/**
 * Column in TiCDC event
 * see detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#row-changed-event
 */
public class TicdcEventColumn {

    // column type
    private int t;
    // where handle flag
    private boolean h;
    // column name
    private String name;
    // column value
    private Object v;

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
