package test.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.*;
import util.DecodeUtil;

import java.util.Collections;
import java.util.Properties;

public class Consumer {

    public static void main(String[] args) throws Throwable {
        Properties p = new Properties();
        p.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.16.4.35:9092");
        p.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        p.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        p.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        KafkaConsumer<byte[], byte[]> kafkaConsumer = new KafkaConsumer<>(p);
        kafkaConsumer.subscribe(Collections.singleton("test"));
        while (true) {
            ConsumerRecords<byte[], byte[]> records = kafkaConsumer.poll(Long.MAX_VALUE);
            for (ConsumerRecord<byte[], byte[]> record : records) {
                String json = DecodeUtil.DecodeJson(record.key(), record.value());
                System.out.println(json);
            }
        }
    }
}
