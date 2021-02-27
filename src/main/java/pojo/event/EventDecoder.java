package pojo.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import enums.EventValueType;
import pojo.Message;
import pojo.event.key.EventKey;
import pojo.event.value.EventValueBase;
import pojo.event.value.children.EventValueDDL;
import pojo.event.value.children.EventValueResolve;
import pojo.event.value.children.RowChange.EventColumn;
import pojo.event.value.children.RowChange.EventValueRowChange;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Binary message decoder
 */
public class EventDecoder implements Iterator<EventData> {

    private static final String DDL = "q";
    private static final String DELETE = "d";
    private static final String UPDATE_NEW_VALUE = "u";
    private static final String UPDATE_OLD_VALUE = "p";
    private DataInputStream keyStream;
    private DataInputStream valueStream;
    private final Message message;
    private long nextkeyLength;
    private long nextValueLength;
    private boolean hasNext = true;

    public EventDecoder(byte[] keyBytes, byte[] valueBytes) {
        this(new Message(keyBytes, valueBytes));
    }

    public EventDecoder(Message message) {
        this.message = message;
        if (message.getKey() != null) {
            keyStream = new DataInputStream(new ByteArrayInputStream(message.getKey()));
            // Get protocol version
            readProtocolVersion();
            readNextKeyLength();
        }
        if (message.getValue() != null) {
            valueStream = new DataInputStream(new ByteArrayInputStream(message.getValue()));
            readNextValueLength();
        }
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public EventData next() {
        try {
            EventKey eventKey = null;
            if (keyStream != null) {
                // Reassignment is just to eliminate ambiguity.
                long keyLength = nextkeyLength;
                byte[] keys = new byte[(int) keyLength];
                keyStream.readFully(keys);
                readNextKeyLength();
                String keyData = new String(keys, StandardCharsets.UTF_8);
                eventKey = createEventKey(keyData);
            }
            String valueData = "";
            if (valueStream != null) {
                long valueLength = nextValueLength;
                byte[] values = new byte[(int) valueLength];
                valueStream.readFully(values);
                readNextValueLength();
                valueData = new String(values, StandardCharsets.UTF_8);
                return new EventData(eventKey, createEventValue(valueData));
            } else {
                return new EventData(eventKey, null);
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void remove() {
        //TODO Just to implement the interface
    }

    /**
     * Call readLong() to get the length of the next key.
     * If it can't be read, hasNext = false.
     */
    private void readNextKeyLength() {
        try {
            nextkeyLength = keyStream.readLong();
            hasNext = true;
        } catch (EOFException e) {
            hasNext = false;
        } catch (Exception e) {
            throw new RuntimeException("The key message format is incorrect, please check the key message! ", e);
        }
    }

    /**
     * Call readLong() to get the length of the next value.
     * If it can't be read, hasNext = false.
     */
    private void readNextValueLength() {
        try {
            nextValueLength = valueStream.readLong();
        } catch (EOFException e) {
            hasNext = false;
        } catch (Exception e) {
            throw new RuntimeException("The value message format is incorrect, please check the value message! ", e);
        }
    }

    /**
     * Protocol version should be 1.
     * see detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#message-format
     */
    private void readProtocolVersion() {
        // Protocol version
        long version;
        try {
            version = keyStream.readLong();
        } catch (IOException e) {
            throw new RuntimeException("The message format is incorrect, please check the message! ", e);
        }
        if (version != 1) {
            throw new RuntimeException("Incorrect protocol version, it should be 1");
        }
    }

    /**
     * Parse json to event key
     *
     * @param json Json from key byte[]
     * @return Event key
     */
    public EventKey createEventKey(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        EventKey key = new EventKey();
        key.setTs(jsonObject.getLongValue("ts"));
        key.setScm(jsonObject.getString("scm"));
        key.setTbl(jsonObject.getString("tbl"));
        key.setT(jsonObject.getLongValue("t"));
        return key;
    }

    /**
     * Parse json to TiCDC event value
     *
     * @param json Json from value byte[]
     * @return TiCDC event value
     */
    public EventValueBase createEventValue(String json) {
        if (json == null || json.length() == 0) {
            return new EventValueResolve(message);
        }
        JSONObject jsonObject = JSON.parseObject(json);
        // DDL event
        if (jsonObject.containsKey(DDL)) {
            EventValueDDL ddlEvent = new EventValueDDL(message);
            ddlEvent.setQ(jsonObject.getString("q"));
            ddlEvent.setT(jsonObject.getIntValue("t"));
            return ddlEvent;
        }
        // Row change event type : update or delete
        String rowChangeType;
        /*
          Row change event has two type: update/delete.
         */
        if (jsonObject.containsKey(UPDATE_NEW_VALUE)) {
            rowChangeType = UPDATE_NEW_VALUE;
        } else if (jsonObject.containsKey(DELETE)) {
            rowChangeType = DELETE;
        } else {
            throw new RuntimeException("The value message is not 'update' or 'delete', json = [" + json + "]");
        }
        // Get row, should be update or delete.
        JSONObject row = jsonObject.getJSONObject(rowChangeType);
        EventValueRowChange ticdcEventValueRowChange = new EventValueRowChange(message);
        ticdcEventValueRowChange.setChangeType(rowChangeType);
        if (ticdcEventValueRowChange.getType() == EventValueType.rowChange) {
            List<EventColumn> columns = getEventColumns(row);
            ticdcEventValueRowChange.setColumns(columns);
        }
        // If has 'p', record.
        if (UPDATE_NEW_VALUE.equals(rowChangeType)) {
            row = jsonObject.getJSONObject(UPDATE_OLD_VALUE);
            if (row != null) {
                ticdcEventValueRowChange.setOldColumns(getEventColumns(row));
            }
        }
        return ticdcEventValueRowChange;
    }

    /**
     * Get event columns
     * see detail. https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#row-changed-event
     *
     * @param row Event row
     * @return Event column list
     */
    private List<EventColumn> getEventColumns(JSONObject row) {
        List<EventColumn> cs = new ArrayList<>();
        if (row != null) {
            for (String col : row.keySet()) {
                JSONObject cObj = row.getJSONObject(col);
                EventColumn c = new EventColumn();
                c.setT(cObj.getIntValue("t"));
                c.setH(cObj.getBooleanValue("h"));
//                c.setF(cObj.getIntValue("f"));
                c.setV(cObj.get("v"));
                c.setName(col);
                cs.add(c);
            }
        }
        return cs;
    }

}

