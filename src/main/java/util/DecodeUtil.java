package util;

import com.alibaba.fastjson.JSON;
import pojo.event.EventData;
import pojo.event.EventDecoder;

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

}
