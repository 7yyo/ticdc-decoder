package util;

import com.alibaba.fastjson.JSON;
import pojo.Message;
import pojo.event.EventData;
import pojo.event.EventDecoder;
import pojo.event.value.children.EventValueResolve;

import java.util.ArrayList;

public class DecodeUtil {

    /**
     * Decode Byte[] to json
     *
     * @param keyBytes   message key bytes
     * @param valueBytes message value bytes
     * @return String
     */
    public static String DecodeJson(byte[] keyBytes, byte[] valueBytes) {
        if (keyBytes == null && valueBytes == null) {
            throw new RuntimeException("Both byte[] keys and byte[] values are empty! Please check the message!");
        }
        EventDecoder eventDecoder = new EventDecoder(keyBytes, valueBytes);
        StringBuilder json = new StringBuilder();
        while (eventDecoder.hasNext()) {
            EventData eventData = eventDecoder.next();
            json.append(JSON.toJSONString(eventData, true));
        }
        return json.toString();
    }

    /**
     * Decode Byte[] to EventData list
     *
     * @param keys   kafka message key bytes
     * @param values kafka message value bytes
     * @return TiCDC event list
     */
//    public static ArrayList<EventData> DecodeEventData(byte[] keys, byte[] values) {
//        ArrayList<EventData> eds = new ArrayList<>();
//        EventDecoder edc = new EventDecoder(keys, values);
//        while (edc.hasNext()) {
//            EventData ed = edc.next();
//            eds.add(ed);
//        }
//        return eds;
//    }
}
