package test;

import org.junit.Assert;
import org.junit.Test;
import pojo.Message;
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
        List<Message> messageList = getMessageFromTestData();
        for (Message message : messageList) {
            byte[] keyBytes = message.getKey();
            byte[] valueBytes = message.getValue();
            String json = DecodeUtil.DecodeJson(keyBytes, valueBytes);
            System.out.println(json);
        }
    }

    /**
     * Mock Kafka messages, which usually consumed from kafka.
     *
     * @return kafka message list
     */
    private List<Message> getMessageFromTestData() throws IOException {
        List<Message> messageList = new ArrayList<>();
        File[] keyFileList = getClasspathFile("data/key").listFiles();
        File[] valueFileList = getClasspathFile("data/value").listFiles();
        Assert.assertNotNull(keyFileList);
        Assert.assertNotNull(valueFileList);
        Assert.assertEquals(keyFileList.length, valueFileList.length);
        for (int i = 0; i < keyFileList.length; i++) {
            File keyFile = keyFileList[i];
            byte[] keyBytes = Files.readAllBytes(keyFile.toPath());
            File valueFile = valueFileList[i];
            byte[] valueBytes = Files.readAllBytes(valueFile.toPath());
            Message message = new Message(keyBytes, valueBytes);
            messageList.add(message);
        }
        return messageList;
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
