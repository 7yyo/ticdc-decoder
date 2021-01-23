package pojo.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import enums.TicdcEventValueType;
import pojo.event.key.TicdcEventKey;
import pojo.event.value.TicdcEventValueBase;
import pojo.event.value.children.RowChange.TicdcEventColumn;
import pojo.event.value.children.TicdcEventValueDDL;
import pojo.event.value.children.TicdcEventValueResolve;
import pojo.event.value.children.RowChange.TicdcEventValueRowChange;
import pojo.KafkaMessage;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Binary kafka message decoder
 */
public class TicdcEventDecoder implements Iterator<TicdcEventData> {

    // Row change event - after update flag
    private static final String UPDATE_NEW_VALUE = "u";
    // Row change event - before update flag
    private static final String UPDATE_OLD_VALUE = "p";
    // Row change event - delete event flag
    private static final String DELETE = "d";
    // DDL event flag
    private static final String DDL = "q";

    // Key byte stream
    private DataInputStream keyStream;
    // Value byte stream
    private DataInputStream valueStream;
    // Protocol version
    private long version;

    private boolean hasNext = true;
    private long keyLength;
    private long valueLength;
    // Get from keyStream.readLong()
    private long nextkeyLength;
    // Get from valueStream.readLong()
    private long nextValueLength;

    private KafkaMessage kafkaMessage;

    public TicdcEventDecoder(byte[] keyBytes, byte[] valueBytes) {
        this(new KafkaMessage(keyBytes, valueBytes));
    }

    // Event decoder for byte[]
    public TicdcEventDecoder(KafkaMessage kafkaMessage) {
        this.kafkaMessage = kafkaMessage;
        keyStream = new DataInputStream(new ByteArrayInputStream(kafkaMessage.getKey()));
        // Get protocol version
        readKeyVersion();
        // Read 8 bytes from keyStream
        readNextKeyLength();
        valueStream = new DataInputStream(new ByteArrayInputStream(kafkaMessage.getValue()));
        // Read 8 bytes from valueStream
        readNextValueLength();
    }


    /**
     * Can iterate
     *
     * @return boolean
     */
    @Override
    public boolean hasNext() {
        return hasNext;
    }

    /**
     * Iteration
     *
     * @return TiCDCEventData
     */
    @Override
    public TicdcEventData next() {
        try {
            // Reassignment is just to eliminate ambiguity.
            keyLength = nextkeyLength;
            byte[] key = new byte[(int) keyLength];
            // Read bytes from keyStream, length is keyLength.
            keyStream.readFully(key);
            // Read next 8 bytes from keyStream.
            readNextKeyLength();
            String keyData = new String(key, StandardCharsets.UTF_8);
            TicdcEventKey ticdcEventKey = createTicdcEventKey(keyData);
            valueLength = nextValueLength;
            byte[] val = new byte[(int) valueLength];
            // Read bytes from valueStream, length is valueLength.
            valueStream.readFully(val);
            // Read next 8 bytes from valueStream
            readNextValueLength();
            String valueData = new String(val, StandardCharsets.UTF_8);
            return new TicdcEventData(ticdcEventKey, createTicdcEventValue(valueData));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Just to implement the interface
     */
    @Override
    public void remove() {

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
            throw new RuntimeException("Illegal format, can not read length", e);
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
            // ignore
        } catch (Exception e) {
            throw new RuntimeException("Illegal format, can not read length", e);
        }
    }

    /**
     * Protocol version should be 1.
     * see detail https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#message-format
     */
    private void readKeyVersion() {
        try {
            version = keyStream.readLong();
        } catch (IOException e) {
            throw new RuntimeException("Illegal format, can not read version", e);
        }
        if (version != 1) {
            throw new RuntimeException("Illegal version, should be 1");
        }
    }


    /**
     * Parse json to TiCDC event key
     *
     * @param json Json from key byte[]
     * @return TiCDC event key
     */
    public TicdcEventKey createTicdcEventKey(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        TicdcEventKey key = new TicdcEventKey();
        key.setTs(jsonObject.getLongValue("ts"));
        key.setT(jsonObject.getLongValue("t"));
        key.setScm(jsonObject.getString("scm"));
        key.setTbl(jsonObject.getString("tbl"));
        return key;
    }

    /**
     * Parse json to TiCDC event value
     *
     * @param json Json from value byte[]
     * @return TiCDC event value
     */
    public TicdcEventValueBase createTicdcEventValue(String json) {
        // If json is null, represents the end of reading, return resolve event.
        // Because regardless of row change or ddl event, there must be value data.
        if (json == null || json.length() == 0) {
            return new TicdcEventValueResolve(kafkaMessage);
        }

        JSONObject jsonObject = JSON.parseObject(json);

        // DDL event
        if (jsonObject.containsKey(DDL)) {
            TicdcEventValueDDL ddlEvent = new TicdcEventValueDDL(kafkaMessage);
            ddlEvent.setQ(jsonObject.getString("q"));
            ddlEvent.setT(jsonObject.getIntValue("t"));
            return ddlEvent;
        }

        // Row change event
        String rowChangeType;
        /*
          Row change event has two type: update/delete.
         */
        if (jsonObject.containsKey(UPDATE_NEW_VALUE)) {
            rowChangeType = UPDATE_NEW_VALUE;
        } else if (jsonObject.containsKey(DELETE)) {
            rowChangeType = DELETE;
        } else {
            throw new RuntimeException("Can not parse the event data value, json : " + json);
        }

        // Get row, should be update or delete.
        JSONObject row = jsonObject.getJSONObject(rowChangeType);
        TicdcEventValueRowChange ticdcEventValueRowChange = new TicdcEventValueRowChange(kafkaMessage);
        ticdcEventValueRowChange.setChangeType(rowChangeType);
        if (ticdcEventValueRowChange.getType() == TicdcEventValueType.rowChange) {
            List<TicdcEventColumn> columns = getTicdcEventColumns(row);
            ticdcEventValueRowChange.setColumns(columns);
        }

        // If has 'p', record.
        if (UPDATE_NEW_VALUE.equals(rowChangeType)) {
            row = jsonObject.getJSONObject(UPDATE_OLD_VALUE);
            if (row != null) {
                ticdcEventValueRowChange.setOldColumns(getTicdcEventColumns(row));
            }
        }
        return ticdcEventValueRowChange;
    }

    /**
     * get TiCDC event columns
     * see detail. https://docs.pingcap.com/tidb/stable/ticdc-open-protocol#row-changed-event
     *
     * @param row event row
     * @return Ticdc event column list
     */
    private List<TicdcEventColumn> getTicdcEventColumns(JSONObject row) {
        List<TicdcEventColumn> columns = new ArrayList<>();
        if (row != null) {
            for (String col : row.keySet()) {
                JSONObject columnObj = row.getJSONObject(col);
                TicdcEventColumn column = new TicdcEventColumn();
                column.setH(columnObj.getBooleanValue("h"));
                column.setT(columnObj.getIntValue("t"));
                column.setV(columnObj.get("v"));
                column.setName(col);
                columns.add(column);
            }
        }
        return columns;
    }

}

