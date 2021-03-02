package pojo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import enums.EventValueType;
import pojo.Message;
import pojo.event.EventData;
import pojo.event.key.EventKey;
import pojo.event.value.EventValueBase;
import pojo.event.value.children.EventValueDDL;
import pojo.event.value.children.EventValueResolved;
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
    private static final String COLUMN = "u";
    private static final String OLD_COLUMN = "p";

    private final Message message;

    private DataInputStream keyStream;
    private DataInputStream valueStream;
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

            String valueData;
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

    private void readNextValueLength() {
        try {
            nextValueLength = valueStream.readLong();
        } catch (EOFException e) {
            hasNext = false;
        } catch (Exception e) {
            throw new RuntimeException("The value message format is incorrect, please check the value message! ", e);
        }
    }

    private void readProtocolVersion() {
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

    public EventKey createEventKey(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        EventKey key = new EventKey();
        key.setTs(jsonObject.getLongValue("ts"));
        key.setScm(jsonObject.getString("scm"));
        key.setTbl(jsonObject.getString("tbl"));
        key.setT(jsonObject.getLongValue("t"));
        return key;
    }

    public EventValueBase createEventValue(String json) {

        if (json == null || json.length() == 0) {
            return new EventValueResolved(message);
        }

        JSONObject jsonObject = JSON.parseObject(json);

        if (jsonObject.containsKey(DDL)) {
            EventValueDDL ddlEvent = new EventValueDDL(message);
            ddlEvent.setQ(jsonObject.getString("q"));
            ddlEvent.setT(jsonObject.getIntValue("t"));
            return ddlEvent;
        }

        String rowChangeType;
        JSONObject row;
        EventValueRowChange eventValueRowChange = new EventValueRowChange(message);

        if (jsonObject.containsKey(COLUMN)) {
            rowChangeType = COLUMN;
            row = jsonObject.getJSONObject(OLD_COLUMN);
            if (row != null) {
                eventValueRowChange.setO_col(getEventColumns(row));
            }
        } else if (jsonObject.containsKey(DELETE)) {
            rowChangeType = DELETE;
        } else {
            throw new RuntimeException("The value message is not 'update' or 'delete', json = [" + json + "]");
        }

        row = jsonObject.getJSONObject(rowChangeType);
        eventValueRowChange.setRcType(rowChangeType);

        if (eventValueRowChange.getType() == EventValueType.ROW_CHANGE) {
            List<EventColumn> columns = getEventColumns(row);
            eventValueRowChange.setCol(columns);
        }

        return eventValueRowChange;
    }

    private List<EventColumn> getEventColumns(JSONObject row) {

        List<EventColumn> eventColumnList = new ArrayList<>();
        if (row != null) {
            for (String column : row.keySet()) {
                JSONObject columnObject = row.getJSONObject(column);
                EventColumn eventColumn = new EventColumn();
                eventColumn.setT(columnObject.getIntValue("t"));
                eventColumn.setH(columnObject.getBooleanValue("h"));
                eventColumn.setV(columnObject.get("v"));
                eventColumn.setN(column);
                eventColumnList.add(eventColumn);
            }
        }

        return eventColumnList;
    }

}

