package org.pdcarv.embeddedkafka;

import kafka.server.KafkaConfig;
import kafka.server.KafkaServer;
import kafka.utils.Exit;
import scala.Option;
import scala.collection.Seq;

import org.apache.logging.log4j.Logger;
import org.apache.kafka.common.utils.Time;
import org.apache.logging.log4j.LogManager;

import kafka.metrics.KafkaMetricsReporter;

public class KafkaServerStartable 
{
    private KafkaServer server;
    static final Logger logger = LogManager.getLogger(KafkaServerStartable.class.getName());

    public KafkaServerStartable(KafkaConfig config, Seq<KafkaMetricsReporter> reporters) 
    {
        this.server = new KafkaServer(config, Time.SYSTEM, Option.empty(), reporters);
    }

    public void startup() {
        try {
            server.startup();
        } catch (Exception e) {
            logger.fatal("Unhandled Exception", e);
            Exit.exit(1, Option.empty());
        }
    }

    public void shutdown() {
        try {
           server.shutdown(); 
        } catch (Exception e) {
            logger.fatal("Unhandled Exception", e);
            Exit.halt(1, Option.empty());
        }
    }
}