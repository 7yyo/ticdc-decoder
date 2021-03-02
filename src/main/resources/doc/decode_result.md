```shell
Chmod6751!
sh ./zookeeper-server-start.sh /home/tidb/yuyang/kafka_2.13-2.7.0/config/zookeeper.properties
sh ./kafka-server-start.sh /home/tidb/yuyang/kafka_2.13-2.7.0/config/server.properties
sh ./kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
sh ./kafka-console-producer.sh --broker-list 172.16.4.35:9092 --topic test
sh ./kafka-console-consumer.sh --bootstrap-server 172.16.4.35:9092 --topic test --from-beginning
tiup ctl cdc  changefeed create --pd=http://172.16.4.33:2307 --sink-uri="kafka://172.16.4.35:9092/test?kafka-version=2.7.0" --changefeed-id="kafka-task-1"
```

## DDL

### CREATE TABLE

```mysql
CREATE TABLE IF NOT EXISTS t
(
    id BIGINT       NOT NULL PRIMARY KEY auto_increment,
    a  VARCHAR(200) NOT NULL
);
```

```json
{
  "k": {
    "scm": "test",
    "t": 2,
    "tbl": "t",
    "ts": 423272374356934661
  },
  "v": {
    "q": "CREATE TABLE IF NOT EXISTS t\n(\n    id BIGINT       NOT NULL PRIMARY KEY auto_increment,\n    a  VARCHAR(200) NOT NULL\n)",
    "t": 3,
    "type": "DDL"
  }
}
```

### ADD COLUMN

```mysql
ALTER TABLE t
    ADD COLUMN b float(11);
```

```json
{
  "k": {
    "scm": "test",
    "t": 2,
    "tbl": "t",
    "ts": 423272384882016260
  },
  "v": {
    "q": "ALTER TABLE t\n    ADD COLUMN b float(11)",
    "t": 5,
    "type": "DDL"
  }
}
```

```mysql
ALTER TABLE t
    ADD COLUMN c decimal(10, 2);
```

```json
{
  "k": {
    "scm": "test",
    "t": 2,
    "tbl": "t",
    "ts": 423272393270624259
  },
  "v": {
    "q": "ALTER TABLE t\n    ADD COLUMN c decimal(10,2)",
    "t": 5,
    "type": "DDL"
  }
}
```

### ADD INDEX

```mysql
ALTER TABLE t
    ADD INDEX INDEX (a);
```

```json
{
  "k": {
    "scm": "test",
    "t": 2,
    "tbl": "t",
    "ts": 423259200362643459
  },
  "v": {
    "q": "ALTER TABLE t     ADD INDEX (a)",
    "t": 7,
    "type": "DDL"
  }
}
```

## ROW CHANGE

### INSERT INTO

```mysql
INSERT INTO t (a, b, c)
VALUES ('a', 1.23, 11.23);
```

```json
{
  "k": {
    "scm": "test",
    "t": 1,
    "tbl": "t",
    "ts": 423272404241350657
  },
  "v": {
    "col": [
      {
        "f": 0,
        "h": false,
        "n": "a",
        "t": 15,
        "v": "a"
      },
      {
        "f": 0,
        "h": false,
        "n": "b",
        "t": 4,
        "v": 1.2300000190734863
      },
      {
        "f": 0,
        "h": false,
        "n": "c",
        "t": 246,
        "v": "11.23"
      },
      {
        "f": 0,
        "h": true,
        "n": "id",
        "t": 8,
        "v": 1
      }
    ],
    "rcType": "u",
    "type": "ROW_CHANGE"
  }
}
```

### UPDATE

```mysql
UPDATE t
SET b = 0.23,
    c = 1.23
WHERE id = 1;
```

```json
{
  "k": {
    "scm": "test",
    "t": 1,
    "tbl": "t",
    "ts": 423272506490617858
  },
  "v": {
    "col": [
      {
        "f": 0,
        "h": false,
        "n": "a",
        "t": 15,
        "v": "a"
      },
      {
        "f": 0,
        "h": false,
        "n": "b",
        "t": 4,
        "v": 0.23000000417232513
      },
      {
        "f": 0,
        "h": false,
        "n": "c",
        "t": 246,
        "v": "1.23"
      },
      {
        "f": 0,
        "h": true,
        "n": "id",
        "t": 8,
        "v": 1
      }
    ],
    "o_col": [
      {
        "f": 0,
        "h": false,
        "n": "a",
        "t": 15,
        "v": "a"
      },
      {
        "f": 0,
        "h": false,
        "n": "b",
        "t": 4,
        "v": 1.2300000190734863
      },
      {
        "f": 0,
        "h": false,
        "n": "c",
        "t": 246,
        "v": "11.23"
      },
      {
        "f": 0,
        "h": true,
        "n": "id",
        "t": 8,
        "v": 1
      }
    ],
    "rcType": "u",
    "type": "ROW_CHANGE"
  }
}
```

---------------------------

```mysql
UPDATE t
SET a = "abc";
```

```json
{
  "k": {
    "scm": "test",
    "t": 1,
    "tbl": "t",
    "ts": 423278837543469058
  },
  "v": {
    "col": [
      {
        "f": 0,
        "h": false,
        "n": "a",
        "t": 15,
        "v": "abc"
      },
      {
        "f": 0,
        "h": true,
        "n": "id",
        "t": 8,
        "v": 3
      }
    ],
    "o_col": [
      {
        "f": 0,
        "h": false,
        "n": "a",
        "t": 15,
        "v": "aaa"
      },
      {
        "f": 0,
        "h": true,
        "n": "id",
        "t": 8,
        "v": 3
      }
    ],
    "rcType": "u",
    "type": "ROW_CHANGE"
  }
}
```

```json
{
  "k": {
    "scm": "test",
    "t": 1,
    "tbl": "t",
    "ts": 423278837543469058
  },
  "v": {
    "col": [
      {
        "f": 0,
        "h": false,
        "n": "a",
        "t": 15,
        "v": "abc"
      },
      {
        "f": 0,
        "h": true,
        "n": "id",
        "t": 8,
        "v": 1
      }
    ],
    "o_col": [
      {
        "f": 0,
        "h": false,
        "n": "a",
        "t": 15,
        "v": "aaa"
      },
      {
        "f": 0,
        "h": true,
        "n": "id",
        "t": 8,
        "v": 1
      }
    ],
    "rcType": "u",
    "type": "ROW_CHANGE"
  }
}
```

```json
{
  "k": {
    "scm": "test",
    "t": 1,
    "tbl": "t",
    "ts": 423278837543469058
  },
  "v": {
    "col": [
      {
        "f": 0,
        "h": false,
        "n": "a",
        "t": 15,
        "v": "abc"
      },
      {
        "f": 0,
        "h": true,
        "n": "id",
        "t": 8,
        "v": 2
      }
    ],
    "o_col": [
      {
        "f": 0,
        "h": false,
        "n": "a",
        "t": 15,
        "v": "aaa"
      },
      {
        "f": 0,
        "h": true,
        "n": "id",
        "t": 8,
        "v": 2
      }
    ],
    "rcType": "u",
    "type": "ROW_CHANGE"
  }
}
```

### DELETE

```mysql
DELETE
FROM t
WHERE id = 2;
```

```json
{
  "k": {
    "scm": "test",
    "t": 1,
    "tbl": "t",
    "ts": 423272547568582658
  },
  "v": {
    "col": [
      {
        "f": 0,
        "h": false,
        "n": "a",
        "t": 15,
        "v": "a"
      },
      {
        "f": 0,
        "h": false,
        "n": "b",
        "t": 4,
        "v": 0.10000000149011612
      },
      {
        "f": 0,
        "h": false,
        "n": "c",
        "t": 246,
        "v": "0.01"
      },
      {
        "f": 0,
        "h": true,
        "n": "id",
        "t": 8,
        "v": 2
      }
    ],
    "rcType": "d",
    "type": "ROW_CHANGE"
  }
}
```

--------------------------

```mysql
DELETE
FROM t;
```

```json
{
  "k": {
    "scm": "test",
    "t": 1,
    "tbl": "t",
    "ts": 423278148589715458
  },
  "v": {
    "col": [
      {
        "f": 0,
        "h": false,
        "n": "a",
        "t": 15,
        "v": "abc"
      },
      {
        "f": 0,
        "h": false,
        "n": "b",
        "t": 4,
        "v": 1.1200000047683716
      },
      {
        "f": 0,
        "h": false,
        "n": "c",
        "t": 246,
        "v": "1.13"
      },
      {
        "f": 0,
        "h": true,
        "n": "id",
        "t": 8,
        "v": 3
      }
    ],
    "rcType": "d",
    "type": "ROW_CHANGE"
  }
}
```

```json
{
  "k": {
    "scm": "test",
    "t": 1,
    "tbl": "t",
    "ts": 423278148589715458
  },
  "v": {
    "col": [
      {
        "f": 0,
        "h": false,
        "n": "a",
        "t": 15,
        "v": "abc"
      },
      {
        "f": 0,
        "h": false,
        "n": "b",
        "t": 4,
        "v": 1.1200000047683716
      },
      {
        "f": 0,
        "h": false,
        "n": "c",
        "t": 246,
        "v": "1.13"
      },
      {
        "f": 0,
        "h": true,
        "n": "id",
        "t": 8,
        "v": 1
      }
    ],
    "rcType": "d",
    "type": "ROW_CHANGE"
  }
}
```

## key = null

### DDL

```json
{
  "v": {
    "q": "drop table t",
    "t": 4,
    "type": "DDL"
  }
}
```

### ROW CHANGE

```mysql
INSERT INTO t
VALUES (1, 'a');
```

```json
{
  "v": {
    "col": [
      {
        "f": 0,
        "h": false,
        "n": "a",
        "t": 15,
        "v": "a"
      },
      {
        "f": 0,
        "h": true,
        "n": "id",
        "t": 8,
        "v": 1
      }
    ],
    "rcType": "u",
    "type": "ROW_CHANGE"
  }
}
```

### RESOLVED

```json
{
  "v": {
    "type": "RESOLVED"
  }
}
```

## value = null

### DDL

```mysql
{
	"k":{
		"scm":"test",
		"t":2,
		"tbl":"t",
		"ts":423278607643181058
	}
}
```

### ROW CHANGE

```json
{
  "k": {
    "scm": "test",
    "t": 1,
    "tbl": "t",
    "ts": 423278637134381058
  }
}
```

### RESOLVED

```json
{
  "k": {
    "t": 3,
    "ts": 423278599372537857
  }
}
```

## key & value = null

```java
Exception in thread"main"java.lang.RuntimeException:Both byte[]keys and byte[]values are empty!Please check the message!
        at util.DecodeUtil.DecodeJson(DecodeUtil.java:18)
        at test.kafka.Consumer.main(Consumer.java:27)
```

