package org.pdcarv.Runners;

import java.io.File;
import java.util.Properties;
import java.util.Random;

import kafka.metrics.KafkaMetricsReporter;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;
import kafka.utils.VerifiableProperties;
import scala.collection.Seq;

public class RunKafka extends Thread {

    protected File tempDir() {
        String ioDir = System.getProperty("java.io.tmpdir");
        File f = new File(ioDir, "kafka-" + new Random().nextInt(1000000));
        f.mkdirs();
        f.deleteOnExit();

        return f;
    }

    public String zookeeperConnect() {
        return "127.0.0.1:2182";
    }

    public Properties createBrokerConfig(Integer nodeId, Integer port) {
        Properties props = new Properties();
        props.put("brokerid", nodeId.toString());
        props.put("port", port.toString());
        props.put("log.dir", this.tempDir().getAbsolutePath());
        props.put("log.flush.interval", "1");
        props.put("socket.request.max.bytes", 1685417328);
        props.put("zookeeper.connect", this.zookeeperConnect());
        props.put("offsets.topic.replication.factor", (short) 1);

        return props;
    }

    public void run() {
        Properties props = createBrokerConfig(0, 8181);
        props.put(KafkaConfig.LogRetentionTimeHoursProp(), "1");
        KafkaConfig config = KafkaConfig.fromProps(props);
        Seq<KafkaMetricsReporter> metrics = KafkaMetricsReporter.startReporters(new VerifiableProperties(props));
        KafkaServerStartable kafkaServer = new KafkaServerStartable(config, metrics);
        kafkaServer.startup();
    }
}