package cn.learning;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.os72.protobuf.dynamic.DynamicSchema;
import com.google.protobuf.DynamicMessage;
import com.googlecode.protobuf.format.JsonFormat;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.msgpack.MessagePack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void test1() throws IOException {
        String msg = "{\"log_type\":\"log\",\"collect_time_hour\":\"2018121316\",\"net_dom\":10,\"struct1\":{\"d1\":\"d1\"},\"collect_ip\":\"127.0.0.1\",\"collect_time_date\":\"20181213\",\"collect_time\":1544691005000,\"dev_ip\":\"127.0.0.1\",\"raw_msg\":\"raw_msg\",\"dev_type\":\"dev_type\",\"serial_num\":\"serial_num\"}";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(msg, Map.class);
        System.out.println(map);
        MessagePack msgpack = new MessagePack();
        byte[] raw = msgpack.write(map);
        Files.write(Paths.get("/tmp/msgpack.raw"), raw);
    }

    @Test
    public void test2() throws IOException {
        byte[] data = Files.readAllBytes(Paths.get("/tmp/msgpack.raw"));
        MessagePack msgpack = new MessagePack();
        String line = msgpack.read(data).toString();
        System.out.println(line);
    }

    @Test
    public void test3() throws Exception {
        Properties props = new Properties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        // props.put("kafka.log.trace", true);
        // props.put("kafka.log.topic", "test");
        // props.put("max.block.ms", 6000); // default 60000
        // props.put("batch.size", 1024); // default 16384
        props.put("linger.ms", 5);
        props.put("retries", 6);
        props.put("retry.backoff.ms", 1000);
        props.put("reconnect.backoff.ms", 1000);
        // props.put("request.timeout.ms", 10000); // default 30000
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        KafkaProducer producer = new KafkaProducer<>(props);

        String msg = "{\"log_type\":\"log\",\"collect_time_hour\":\"2018121316\",\"net_dom\":10,\"struct1\":{\"d1\":\"d1\"},\"collect_ip\":\"127.0.0.1\",\"collect_time_date\":\"20181213\",\"collect_time\":1544691005000,\"dev_ip\":\"127.0.0.1\",\"raw_msg\":\"raw_msg\",\"dev_type\":\"dev_type\",\"serial_num\":\"serial_num\"}";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(msg, Map.class);
        System.out.println(map);
        MessagePack msgpack = new MessagePack();
        byte[] raw = msgpack.write(map);
        System.out.println(raw.length);

        ProducerRecord<String, byte[]> record = new ProducerRecord<>("test", raw);
        while (true) {
            producer.send(record);
            Thread.sleep(1000);
        }
    }

    @Test
    public void test4() throws Exception {
        LogMessageOuterClass.LogMessage log = LogMessageOuterClass.LogMessage.newBuilder()
                .setLogType("log")
                .setCollectTimeHour("2018121316")
                .setNetDom(10)
                .setCollectIp("127.0.0.1")
                .setCollectTimeDate("20181213")
                .setCollectTime(1544691005000L)
                .setDevIp("127.0.0.1")
                .setRawMsg("raw_msg")
                .setDevType("123")
                .setSerialNum("12345678").build();
        byte[] raw = log.toByteArray();
        System.out.println(raw.length);

        Properties props = new Properties();
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        // props.put("kafka.log.trace", true);
        // props.put("kafka.log.topic", "test");
        // props.put("max.block.ms", 6000); // default 60000
        // props.put("batch.size", 1024); // default 16384
        props.put("linger.ms", 5);
        props.put("retries", 6);
        props.put("retry.backoff.ms", 1000);
        props.put("reconnect.backoff.ms", 1000);
        // props.put("request.timeout.ms", 10000); // default 30000
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        KafkaProducer producer = new KafkaProducer<>(props);
        ProducerRecord<String, byte[]> record = new ProducerRecord<>("test", raw);

        while (true) {
            producer.send(record);
            Thread.sleep(100);
        }
    }

    @Test
    public void test5() throws Exception {
        LogMessageOuterClass.LogMessage log = LogMessageOuterClass.LogMessage.newBuilder()
                .setLogType("log")
                .setCollectTimeHour("2018121316")
                .setNetDom(10)
                .setCollectIp("127.0.0.1")
                .setCollectTimeDate("20181213")
                .setCollectTime(1544691005000L)
                .setDevIp("127.0.0.1")
                .setRawMsg("raw_msg")
                .setDevType("123")
                .setSerialNum("12345678").build();
        byte[] raw = log.toByteArray();
        System.out.println(raw.length);

        byte[] descBytes = Files.readAllBytes(Paths.get("src/test/resources/log_message.proto.desc"));
        DynamicSchema schema = DynamicSchema.parseFrom(descBytes);
        Set<String> set = schema.getMessageTypes();
        // System.out.println(Arrays.deepToString(set.toArray()));
        DynamicMessage message = DynamicMessage.parseFrom(schema.getMessageDescriptor(set.iterator().next()), raw);
        // System.out.println(message.toString());
        JsonFormat jsonFormat = new JsonFormat();
        String json = jsonFormat.printToString(message);
        System.out.println(json);
    }

    @Test
    public void test6() throws Exception {
        LogMessageOuterClass.LogMessage log = LogMessageOuterClass.LogMessage.newBuilder()
                .setLogType("log")
                .setCollectTimeHour("2018121316")
                .setNetDom(10)
                .setCollectIp("127.0.0.1")
                .setCollectTimeDate("20181213")
                .setCollectTime(1544691005000L)
                .setDevIp("127.0.0.1")
                .setRawMsg("raw_msg")
                .setDevType("123")
                .setSerialNum("12345678").build();
        byte[] raw = log.toByteArray();

        System.out.println(new String(raw));

        byte[] descBytes = Files.readAllBytes(Paths.get("src/test/resources/log_message.proto.desc"));
        ProtobufParser parser = new ProtobufParser(descBytes);
        String line = parser.parse(raw);
        System.out.println(line);
    }
}
