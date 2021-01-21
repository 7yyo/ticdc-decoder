package ticdcJavaDemo.value;

public class TicdcEventColumn {

    private int t;// Type, see https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#column-type-code
    private boolean h; // Where Handle
    private String name; // Column name
    private Object v; // Value

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
