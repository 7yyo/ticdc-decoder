package ticdcJavaDemo.key;

/*
    TicdcEventKey.java is the entity class of ticdc event key.
 */
public class TicdcEventKey {

    private long ts;
    private String scm; // Schema
    private String tbl; // Table
    private long t; // Type

    public long getTimestamp() {
        if (ts > 0) {
            return ts >> 18;
        }
        return -1L;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getScm() {
        return scm;
    }

    public void setScm(String scm) {
        this.scm = scm;
    }

    public String getTbl() {
        return tbl;
    }

    public void setTbl(String tbl) {
        this.tbl = tbl;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }
}

