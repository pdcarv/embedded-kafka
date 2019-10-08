package org.pdcarv.kafka;

import org.junit.Test;

import kafka.metrics.KafkaMetricsReporter;
import kafka.server.KafkaConfig;
import kafka.utils.VerifiableProperties;
import scala.collection.Seq;

import java.util.Properties;

/**
 * Unit test for simple App.
 */
public class KafkaServerStartableTest {
    /**
     * Rigorous Test.
     */
    @Test
    public void KafkaServerStartable_StartsDaemon() {
        Properties props = TestUtils.createBrokerConfig(0, 8181);
        props.put(KafkaConfig.LogRetentionTimeHoursProp(), "1");
        KafkaConfig config = KafkaConfig.fromProps(props);
        Seq<KafkaMetricsReporter> metrics = KafkaMetricsReporter.startReporters(new VerifiableProperties(props));
        KafkaServerStartable server = new KafkaServerStartable(config, metrics);

        server.startup();

    }
}
