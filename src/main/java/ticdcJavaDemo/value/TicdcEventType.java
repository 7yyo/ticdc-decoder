package ticdcJavaDemo.value;

/**
 * There are three types of ticdc event.
 * @Row_Changed Event
 * @DDl Event
 * @Resolved Event
 *
 * See detail https://docs.pingcap.com/zh/tidb/stable/ticdc-open-protocol
 */
public enum TicdcEventType {

    rowChange, ddl, resolved

}
