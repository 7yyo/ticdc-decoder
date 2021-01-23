package pojo.event.key;

/**
 * The key of TiCDC event.
 * see detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#event-format
 */
public class TicdcEventKey {

    /**
     * RowChangeEvent: The timestamp of the transaction that causes the row change.
     * DDLEvent:       The timestamp of the transaction that performs the DDL change.
     * ResolvedEvent:  The Resolved timestamp. Any TS earlier than this Event has been sent.
     */
    private long ts;
    // schema
    private String scm;
    // table
    private String tbl;
    // type
    private long t;

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

    /**
     * Change ts to timestamp
     * @return timestamp
     */
    public long getTimestamp() {
        if (ts > 0) {
            return ts >> 18;
        }
        return -1L;
    }
}

