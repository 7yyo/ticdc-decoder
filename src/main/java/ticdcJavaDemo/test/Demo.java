package ticdcJavaDemo.test;

import org.junit.Assert;
import org.junit.Test;
import com.alibaba.fastjson.JSON;
import ticdcJavaDemo.KafkaMessage;
import ticdcJavaDemo.TicdcEventData;
import ticdcJavaDemo.TicdcEventDecoder;
import ticdcJavaDemo.TicdcEventFilter;
import ticdcJavaDemo.value.TicdcEventDDL;
import ticdcJavaDemo.value.TicdcEventResolve;
import ticdcJavaDemo.value.TicdcEventRowChange;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Demo {

    @Test
    public void test() throws IOException {
        // Get kafka message from binary file
        List<KafkaMessage> kafkaMessagesFromTestData = getKafkaMessagesFromTestData();
        TicdcEventFilter filter = new TicdcEventFilter();
        for (KafkaMessage kafkaMessage : kafkaMessagesFromTestData) {
            byte[] keyBytes = kafkaMessage.getKey();
            byte[] valueBytes = kafkaMessage.getValue();
            // TODO
            TicdcEventDecoder ticdcEventDecoder = new TicdcEventDecoder(keyBytes, valueBytes);
            while (ticdcEventDecoder.hasNext()) {
                TicdcEventData data = ticdcEventDecoder.next();
                if (data.getTicdcEventValue() instanceof TicdcEventRowChange) {
                    boolean ok = filter.check(data.getTicdcEventKey().getTbl(), data.getTicdcEventValue().getKafkaPartition(), data.getTicdcEventKey().getTs());
                    if (ok) {
                        // deal with row change event
                    } else {
                        // ignore duplicated messages
                    }
                } else if (data.getTicdcEventValue() instanceof TicdcEventDDL) {
                    // deal with ddl event
                } else if (data.getTicdcEventValue() instanceof TicdcEventResolve) {
                    filter.resolveEvent(data.getTicdcEventValue().getKafkaPartition(), data.getTicdcEventKey().getTs());
                    // deal with resolve event
                }
                System.out.println(JSON.toJSONString(data, true));
            }
        }
    }

    /**
     * Mock Kafka messages, which usually consumed from kafka.
     */
    private List<KafkaMessage> getKafkaMessagesFromTestData() throws IOException {
        List<KafkaMessage> kafkaMessages = new ArrayList<>();
        // Get KEY binary files from path`
        File[] keyFiles = getClasspathFile("data/key").listFiles();
        // Get VALUE binary files from path
        File[] valueFiles = getClasspathFile("data/value").listFiles();

        Assert.assertNotNull(keyFiles);
        Assert.assertNotNull(valueFiles);
        Assert.assertEquals(keyFiles.length, valueFiles.length);

        // Because the two files have the same length, they loop together
        for (int i = 0; i < keyFiles.length; i++) {
            File kf = keyFiles[i];
            // Reads all the bytes from Key file
            byte[] kafkaMessageKey = Files.readAllBytes(kf.toPath());
            System.out.printf("read key msg: %s\n", kf.toPath());
            File vf = valueFiles[i];
            // Reads all the bytes from Values file
            byte[] kafkaMessageValue = Files.readAllBytes(vf.toPath());
            System.out.printf("read value msg: %s\n", vf.toPath());
            // Compose the kafka message.
            KafkaMessage kafkaMessage = new KafkaMessage(kafkaMessageKey, kafkaMessageValue);
            kafkaMessage.setPartition(1);
            kafkaMessage.setOffset(1L);
            kafkaMessage.setTimestamp(System.currentTimeMillis());
            kafkaMessages.add(kafkaMessage);
//            System.out.println(kafkaMessages.size());
        }
        // return kafka message list
        return kafkaMessages;
    }

    // Get kafka binary file
    private File getClasspathFile(String path) {
        URL url = getClass().getClassLoader().getResource(path);
        Assert.assertNotNull(url);
        return new File(url.getFile());
    }

}