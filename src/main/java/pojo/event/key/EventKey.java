package pojo.event.key;

/**
 * The key of ticdc event.
 * see detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#event-format
 */
public class EventKey {

    private long ts;
    private long t;
    private String scm;
    private String tbl;

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

