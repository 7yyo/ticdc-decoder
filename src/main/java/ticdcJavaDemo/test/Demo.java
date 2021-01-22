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
        System.out.println("开始读取 kafka 二进制消息...");
        List<KafkaMessage> kafkaMessagesFromTestData = getKafkaMessagesFromTestData();
        System.out.println("完成读取 kafka 二进制消息...");
        TicdcEventFilter filter = new TicdcEventFilter();
        System.out.println("+++++++++++++++++++++++++++++++++");
        System.out.println("开始循环 kafka message list");
        int n = 0;
        for (KafkaMessage kafkaMessage : kafkaMessagesFromTestData) {
            n++;
            byte[] keyBytes = kafkaMessage.getKey();
            byte[] valueBytes = kafkaMessage.getValue();
            System.out.println("第" + n + "组 kafka message 为：key = " + keyBytes + " / value = " + valueBytes);
            System.out.println("+++++++++++++++++++++++++++++++++");
            System.out.println("开始迭代 kv 对进行解析， message 格式请参考 https://docs.pingcap.com/zh/tidb/stable/ticdc-open-protocol 中 message 格式定义...");
            TicdcEventDecoder ticdcEventDecoder = new TicdcEventDecoder(keyBytes, valueBytes);
            int m = 0;
            while (ticdcEventDecoder.hasNext()) {
                m++;
                System.out.println("+++++++++++++++++++++++++++++++++");
                System.out.println("解析第" + m + "对 kv ...");
                TicdcEventData data = ticdcEventDecoder.next();
                if (data.getTicdcEventValue() instanceof TicdcEventRowChange) {
                    boolean ok = filter.check(data.getTicdcEventKey().getTbl(), data.getTicdcEventValue().getKafkaPartition(), data.getTicdcEventKey().getTs());
                    if (ok) {
                        // deal with row change event
                    } else {
                        // ignore duplicated messages
                    }
                    System.out.println("请操作 row change event");
                } else if (data.getTicdcEventValue() instanceof TicdcEventDDL) {
                    // deal with ddl event
                    System.out.println("请操作 ddl event");
                } else if (data.getTicdcEventValue() instanceof TicdcEventResolve) {
                    filter.resolveEvent(data.getTicdcEventValue().getKafkaPartition(), data.getTicdcEventKey().getTs());
                    // deal with resolve event
                    System.out.println("请操作 resolve event");
                }
                System.out.println("解析结果 ==== " + JSON.toJSONString(data, true));
            }
            System.out.println("迭代结束，程序退出");
        }
    }

    /**
     * Mock Kafka messages, which usually consumed from kafka.
     */
    private List<KafkaMessage> getKafkaMessagesFromTestData() throws IOException {
        List<KafkaMessage> kafkaMessages = new ArrayList<>();
        System.out.println("读取 data/key 路径下的  key 消息二进制文件列表完成...");
        File[] keyFiles = getClasspathFile("data/key").listFiles();
        System.out.println("读取 data/value 路径下的 value 消息二进制文件列表完成...");
        File[] valueFiles = getClasspathFile("data/value").listFiles();

        Assert.assertNotNull(keyFiles);
        Assert.assertNotNull(valueFiles);
        Assert.assertEquals(keyFiles.length, valueFiles.length);

        // Because the two files have the same length, they loop together
        for (int i = 0; i < keyFiles.length; i++) {
            File kf = keyFiles[i];
            byte[] kafkaMessageKey = Files.readAllBytes(kf.toPath());
            System.out.println("读取 key 二进制文件[" + i + "]内容成功");
//            System.out.printf("read key msg: %s\n", kf.toPath());
            File vf = valueFiles[i];
            // Reads all the bytes from Values file
            byte[] kafkaMessageValue = Files.readAllBytes(vf.toPath());
            System.out.println("读取 value 二进制文件[" + i + "]内容成功");
//            System.out.printf("read value msg: %s\n", vf.toPath());
            // Compose the kafka message.
            KafkaMessage kafkaMessage = new KafkaMessage(kafkaMessageKey, kafkaMessageValue);
            kafkaMessage.setPartition(1);
            kafkaMessage.setOffset(1L);
            kafkaMessage.setTimestamp(System.currentTimeMillis());
            kafkaMessages.add(kafkaMessage);
            System.out.println("将 key/value [" + i + "] 的内容封装为 kafka message，并加入 kafka message list");
//            System.out.println(kafkaMessages.size());
        }
        System.out.println("封装完成，kafka message list 的长度为 " + kafkaMessages.size());
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