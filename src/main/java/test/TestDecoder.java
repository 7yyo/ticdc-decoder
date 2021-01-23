package test;

import org.junit.Assert;
import org.junit.Test;
import pojo.KafkaMessage;
import util.DecodeUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class TestDecoder {

    /**
     * Just fot test
     * Use binary file to verify the correctness of binary conversion json.
     * You can select the test file from the path resources/data and put it in resouces/data/key or value.
     * Note. Please select the key/value test file with the same file name suffix. And put a pair of files each time
     *
     * @throws IOException Binary file could not be found.
     */
    @Test
    public void test() throws IOException {
        List<KafkaMessage> kafkaMessagesFromTestData = getKafkaMessagesFromTestData();
        for (KafkaMessage kafkaMessage : kafkaMessagesFromTestData) {
            byte[] keyBytes = kafkaMessage.getKey();
            byte[] valueBytes = kafkaMessage.getValue();
            String result = DecodeUtil.ParseBinaryToJson(keyBytes, valueBytes);
            System.out.println(result);
        }
    }

    /**
     * Mock Kafka messages, which usually consumed from kafka.
     *
     * @return kafka message list
     */
    private List<KafkaMessage> getKafkaMessagesFromTestData() throws IOException {
        List<KafkaMessage> kafkaMessages = new ArrayList<>();
        File[] keyFiles = getClasspathFile("data/key").listFiles();
        File[] valueFiles = getClasspathFile("data/value").listFiles();

        Assert.assertNotNull(keyFiles);
        Assert.assertNotNull(valueFiles);
        Assert.assertEquals(keyFiles.length, valueFiles.length);

        for (int i = 0; i < keyFiles.length; i++) {
            File kf = keyFiles[i];
            byte[] kafkaMessageKey = Files.readAllBytes(kf.toPath());
            File vf = valueFiles[i];
            byte[] kafkaMessageValue = Files.readAllBytes(vf.toPath());
            KafkaMessage kafkaMessage = new KafkaMessage(kafkaMessageKey, kafkaMessageValue);
            kafkaMessage.setPartition(1);
            kafkaMessage.setOffset(1L);
            kafkaMessage.setTimestamp(System.currentTimeMillis());
            kafkaMessages.add(kafkaMessage);
        }
        return kafkaMessages;
    }

    /**
     * Get binary file from path
     *
     * @param path file path
     * @return file
     */
    private File getClasspathFile(String path) {
        URL url = getClass().getClassLoader().getResource(path);
        Assert.assertNotNull(url);
        return new File(url.getFile());
    }

}
