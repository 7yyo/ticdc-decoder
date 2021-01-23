## TiCDC - Decoder in Java  [demo]
  
See detail [TiCDC Open Protocol](https://docs.pingcap.com/tidb/stable/ticdc-open-protocol )

## How to use for now
```java
String jsonResult = DecodeUtil.ParseBinaryToJson(keyBytes, valueBytes)
```
```java
ArrayList<TicdcEventData> dataList = DecodeUtil.ParseBinaryToEventData(keyBytes, valueBytes);
```
## API
```java
/**
 * Parse Byte[] to Json
 *
 * @param keys   kafka message key bytes
 * @param values kafka message value bytes
 * @return String
 */
 public static String ParseBinaryToJson(byte[] keys, byte[] values)
```
```java
/**
 * Parse Byte[] to TicdcEventData list
 *
 * @param keys   kafka message key bytes
 * @param values kafka message value bytes
 * @return TiCDC event list
 */
 public static ArrayList<TicdcEventData> ParseBinaryToEventData(byte[] keys, byte[] values)
```

