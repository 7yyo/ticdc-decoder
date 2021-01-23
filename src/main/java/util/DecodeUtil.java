package util;

import com.alibaba.fastjson.JSON;
import pojo.event.TicdcEventData;
import pojo.event.TicdcEventDecoder;

import java.util.ArrayList;

public class DecodeUtil {

    /**
     * Parse Byte[] to Json
     *
     * @param keys   kafka message key bytes
     * @param values kafka message value bytes
     * @return String
     */
    public static String ParseBinaryToJson(byte[] keys, byte[] values) {
        TicdcEventDecoder ticdcEventDecoder = new TicdcEventDecoder(keys, values);
        StringBuilder result = new StringBuilder();
        while (ticdcEventDecoder.hasNext()) {
            TicdcEventData ticdcEventData = ticdcEventDecoder.next();
            result.append(JSON.toJSONString(ticdcEventData, true)).append("\n");
        }
        return result.toString();
    }

    /**
     * Parse Byte[] to TicdcEventData list
     *
     * @param keys   kafka message key bytes
     * @param values kafka message value bytes
     * @return TiCDC event list
     */
    public static ArrayList<TicdcEventData> ParseBinaryToEventData(byte[] keys, byte[] values) {
        ArrayList<TicdcEventData> ticdcEventDataList = new ArrayList<>();
        TicdcEventDecoder ticdcEventDecoder = new TicdcEventDecoder(keys, values);
        while (ticdcEventDecoder.hasNext()) {
            TicdcEventData ticdcEventData = ticdcEventDecoder.next();
            ticdcEventDataList.add(ticdcEventData);
        }
        return ticdcEventDataList;
    }

}
