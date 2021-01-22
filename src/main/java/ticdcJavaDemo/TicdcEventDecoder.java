package ticdcJavaDemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ticdcJavaDemo.key.TicdcEventKey;
import ticdcJavaDemo.value.*;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @TicdcEventDecoder.java 解析流程
 */
public class TicdcEventDecoder implements Iterator<TicdcEventData> {

    // update 后数据值的标示符
    private static final String UPDATE_NEW_VALUE_TOKEN = "u";
    // update 前数据值的标示符
    private static final String UPDATE_OLD_VALUE_TOKEN = "p";

    private DataInputStream keyStream;
    private DataInputStream valueStream;
    private long version;

    private boolean hasNext = true;
    private long nextKeyLength;
    private long nextValueLength;

    private KafkaMessage kafkaMessage;

    // visible for test
    public TicdcEventDecoder(byte[] keyBytes, byte[] valueBytes) {
        this(new KafkaMessage(keyBytes, valueBytes));
    }

    public TicdcEventDecoder(KafkaMessage kafkaMessage) {
        this.kafkaMessage = kafkaMessage;
        keyStream = new DataInputStream(new ByteArrayInputStream(kafkaMessage.getKey()));
        readKeyVersion();
        readKeyLength();
        valueStream = new DataInputStream(new ByteArrayInputStream(kafkaMessage.getValue()));
        readValueLength();
    }

    private void readKeyLength() {
        try {
            nextKeyLength = keyStream.readLong();
            System.out.println("###############################");
            System.out.println("下一个解析的 key length ，读取 8 个 bytes，返回 "+nextKeyLength);
            System.out.println("###############################");
            hasNext = true;
        } catch (EOFException e) {
            System.out.println("这是最后的 kv 对 ...");
            hasNext = false;
        } catch (Exception e) {
            throw new RuntimeException("Illegal format, can not read length", e);
        }
    }

    private void readValueLength() {
        try {
            nextValueLength = valueStream.readLong();
        } catch (EOFException e) {
            // ignore
        } catch (Exception e) {
            throw new RuntimeException("Illegal format, can not read length", e);
        }
    }


    private void readKeyVersion() {
        try {
            // should be 1
            // https://docs.pingcap.com/zh/tidb/stable/ticdc-open-protocol - Message format definition
            version = keyStream.readLong();
            System.out.println("协议版本号为" + version);
        } catch (IOException e) {
            throw new RuntimeException("Illegal format, can not read version", e);
        }
        if (version != 1) {
            throw new RuntimeException("Illegal version, should be 1");
        }
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }


    public TicdcEventKey createTidbEventKey(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        TicdcEventKey key = new TicdcEventKey();
        key.setTs(jsonObject.getLongValue("ts"));
        key.setT(jsonObject.getLongValue("t"));
        key.setScm(jsonObject.getString("scm"));
        key.setTbl(jsonObject.getString("tbl"));
        return key;
    }

    public TicdcEventBase createTidbEventValue(String json) {
        // resolve
        if (json == null || json.length() == 0) {
            System.out.println("json 读取完成，创建 resolve event..");
            return new TicdcEventResolve(kafkaMessage);
        }
        System.out.println("开始解析 json ...");
        JSONObject jsonObject = JSON.parseObject(json);

        // ddl
        if (jsonObject.containsKey("q")) {
            TicdcEventDDL ddl = new TicdcEventDDL(kafkaMessage);
            ddl.setQ(jsonObject.getString("q"));
            ddl.setT(jsonObject.getIntValue("t"));
            System.out.println("因为开头是 q ，所以是 ddl event ...");
            return ddl;
        }

        // row change
        String updateOrDelete;
        // type update
        if (jsonObject.containsKey(UPDATE_NEW_VALUE_TOKEN)) {
            updateOrDelete = UPDATE_NEW_VALUE_TOKEN;
            System.out.println("因为开头是 u ，所以是 update 类型的 row change event ...");
        // type delete
        } else if (jsonObject.containsKey("d")) {
            updateOrDelete = "d";
            System.out.println("因为开头是 d ，所以是 delete 类型的 row change event ...");
        } else {
            throw new RuntimeException("Can not parse Value:" + json);
        }

        JSONObject row = jsonObject.getJSONObject(updateOrDelete);
        TicdcEventRowChange v = new TicdcEventRowChange(kafkaMessage);
        v.setUpdateOrDelete(updateOrDelete);
        if (v.getType() == TicdcEventType.rowChange) {
            List<TicdcEventColumn> columns = getTicdcEventColumns(row);
            v.setColumns(columns);
        }

        if(UPDATE_NEW_VALUE_TOKEN.equals(updateOrDelete) ){
            row = jsonObject.getJSONObject(UPDATE_OLD_VALUE_TOKEN);
            if(row != null){
                v.setOldColumns(getTicdcEventColumns(row));
            }
        }
        return v;
    }
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
    @Override
    public TicdcEventData next() {
        try {
            byte[] key = new byte[(int) nextKeyLength];
            System.out.println("通过 readFully(key) 读取 长度为 "+key.length+ " 的 key 的字节流");
            keyStream.readFully(key);
//            System.out.println("nextKeyLength = " + nextKeyLength);
            readKeyLength();
            String keyData = new String(key, StandardCharsets.UTF_8);
            System.out.println("将字节流转换为 UTF8 ，所以 key = " + keyData);
            TicdcEventKey ticdcEventKey = createTidbEventKey(keyData);
            byte[] val = new byte[(int) nextValueLength];
//            System.out.println("nextValueLength = " + nextValueLength);
            System.out.println("通过 readFully(key) 读取 长度为 "+key.length+ " 的 value 的字节流");
            valueStream.readFully(val);
            readValueLength();
            String valueData = new String(val, StandardCharsets.UTF_8);
            System.out.println("将字节流转换为 UTF8，所以 value = " + valueData);
            return new TicdcEventData(ticdcEventKey, createTidbEventValue(valueData));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    // 没用，只是为了实现接口
    public void remove() {

    }
}

