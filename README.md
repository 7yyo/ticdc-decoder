## TiCDC - Decoder in Java    [Demo]

A Java decoder for [TiCDC Open Protocol](https://docs.pingcap.com/tidb/stable/ticdc-open-protocol)

## How to build
```shell
mvn clean install -Dmaven.test.skip=true
mvn package
```
The file jar can be found in `./target/`

## Usage
Ticdc decoder demo is avaliable in [TestDecoder.java](https://github.com/7yyo/ticdc-decoder/blob/master/src/main/java/test/TestDecoder.java)  
Ticdc decoder demo for kafka is avaliable in [Consumer.java](https://github.com/7yyo/ticdc-decoder/blob/master/src/main/java/test/kafka/Consumer.java)
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

### Row change event
```mysql
update t1 set c1 = 2 where id = 1;
```
```json
{
 "eventKey":{
	     "scm":"test",
	     "t":1,
	     "tbl":"t1",
	     "ts":423217537329659906
	    },
"eventValue":{
	     "changeType":"u",
	     "columns":[{
			"f":0,
			"h":true,
			"name":"id",
			"t":3,
			"v":1
			},
			{
			"f":0,
			"h":false,
			"name":"c1",
			"t":3,
			"v":2
			}
		],
		"oldColumns":[
			{
			"f":0,
			"h":true,
			"name":"id",
			"t":3,
			"v":1
			},
			{
			"f":0,
			"h":false,
			"name":"c1",
			"t":3,
			"v":1
			}
		],
		"type":"rowChange"
	}
}

```
### DDL event
```mysql
create table t1(id int primary key,c1 int,index(c1))
```
```json
{
	"eventKey":{
		"scm":"test",
		"t":2,
		"tbl":"t1",
		"ts":423217447859912710
	},
	"eventValue":{
		"q":"create table t1(id int primary key,c1 int,index(c1))",
		"t":3,
		"type":"ddl"
	}
}
```
### Resolved event
```json
{
	"eventKey":{
		"t":3,
		"ts":423216567160668163
	},
	"eventValue":{
		"type":"resolved"
	}
}
```
