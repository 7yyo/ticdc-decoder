package enums;

/**
 * The enumerated type of ticdc event.
 * See detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#event-format
 */
public enum EventValueType {
    rowChange, ddl, resolved
}
