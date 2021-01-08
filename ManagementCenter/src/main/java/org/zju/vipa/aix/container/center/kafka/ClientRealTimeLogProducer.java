package org.zju.vipa.aix.container.center.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.zju.vipa.aix.container.center.util.ExceptionUtils;
import org.zju.vipa.aix.container.center.log.LogUtils;
import org.zju.vipa.aix.container.common.config.NetworkConfig;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @Date: 2020/6/23 15:58
 * @Author: EricMa
 * @Description:
 */
public class ClientRealTimeLogProducer {

    private static KafkaProducer<String, String> producer;
    public static volatile  boolean isActive=false;
    public static volatile  String client_token=null;
    static  {
//        // 1. 生产者配置
//        Properties properties = new Properties();
//        // 指定kafka地址
//        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, NetworkConfig.KAFKA_SERVER_URL);
//        // 指定ack等级
//        properties.put(ProducerConfig.ACKS_CONFIG, "all");
//        // 指定重试次数，即生产者发送数据后没有收到ack应答时的重试次数
//        properties.put(ProducerConfig.RETRIES_CONFIG, 1);
//        // 指定批次大小 16k = 16 * 1024
//        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        // 指定等待时间，单位毫秒
//        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//        // 指定RecordAccumulator缓冲区大小 32m = 32 * 1024 * 1024
//        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//        // 指定k-v序列化规则
//        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//
//        // 2. 创建生产者
//        producer = new KafkaProducer<>(properties);

    }

    public static void send(String key, String value) {
        // 3. 准备数据
        ProducerRecord<String, String> record = new ProducerRecord<>(NetworkConfig.KAFKA_TOPIC, key, value);
        // 4. 发送数据（带回调）
        Future<RecordMetadata> future = producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    // 回调函数，该方法会在 Producer 收到 ack 时调用，为异步调用
                    String result = String.format("kafka消息发送成功，主题Topic: %s,分区Partition: %s,偏移量Offset: %s",
                        metadata.topic(), metadata.partition(), metadata.offset());
                    LogUtils.debug(result);
                } else {
                    ExceptionUtils.handle(exception);
                }

            }
        });
        try {
            future.get();//同步
        } catch (InterruptedException | ExecutionException e) {
            ExceptionUtils.handle(e);
        }
    }

    public static void close(){
        // 5. 关闭连接
        if (producer!=null) {
            producer.close();
        }
    }


}
