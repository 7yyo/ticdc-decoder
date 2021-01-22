package ticdcJavaDemo.key;

/**
 * @TicdcEventKey.java 是 event 的 key 的实体类
 * 关于 event，请参考 https://docs.pingcap.com/zh/tidb/stable/ticdc-open-protocol 的概述部分
 */
public class TicdcEventKey {

    /**
     * Ticdc event key 包含如下属性
     */
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

