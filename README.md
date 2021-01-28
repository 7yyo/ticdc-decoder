## TiCDC - Decoder in Java    [Demo]

A Java decoder for [TiCDC Open Protocol](https://docs.pingcap.com/tidb/stable/ticdc-open-protocol)

## How to build

The alternative way to build a usable jar for testing will be
```shell
mvn clean install -Dmaven.test.skip=true
```
The following command can install dependencies for you.
```shell
mvn package
```
The jar can be found in `./target/`

## Usage
Demo is avaliable in [TestDecoder](https://github.com/7yyo/ticdc-decoder/blob/master/src/main/java/test/TestDecoder.java
## API
```java
/**
 * Parse Byte[] to Json. Note, param keys and values can not be null in same time.
 *
 * @param keys   message key bytes, can be null.
 * @param values message value bytes, can be null.
 * @return String
 */
 public static String DecodeJson(byte[] keys, byte[] values)
```

