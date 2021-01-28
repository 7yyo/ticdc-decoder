## TiCDC - Decoder in Java    [Demo]

A Java decoder for [TiCDC Open Protocol](https://docs.pingcap.com/tidb/stable/ticdc-open-protocol)

## How to build
```shell
mvn clean install -Dmaven.test.skip=true
mvn package
```
The file jar can be found in `./target/`

## Usage
Demo is avaliable in [TestDecoder.java](https://github.com/7yyo/ticdc-decoder/blob/master/src/main/java/test/TestDecoder.java)
## API
```java
/**
 * Parse Byte[] to Json. 
 * Note, param keys and values can not be null in same time.
 *
 * @param keys   message key bytes, can be null.
 * @param values message value bytes, can be null.
 * @return String
 */
 public static String DecodeJson(byte[] keys, byte[] values)
```
## Parse result example

```json
{
"eventKey":{
	"scm":"a",
	"t":1,
	"tbl":"b",
	"ts":2
},
"eventValue":{
	"changeType":"u",
	"columns":[{
		"f":0,
		"h":false,
		"name":"col1",
		"t":1,
		"v":"💋💼🕶💼👛💄💋💇"
	}],
	"type":"rowChange"
    }
}
{
"eventKey":{
	"scm":"a",
	"t":1,
	"tbl":"b",
	"ts":3
},
"eventValue":{
	"changeType":"u",
	"columns":[{
		"f":0,
		"h":false,
		"name":"col1",
		"t":1,
		"v":"にほんご"
	}],
	"type":"rowChange"
	}
}
```
