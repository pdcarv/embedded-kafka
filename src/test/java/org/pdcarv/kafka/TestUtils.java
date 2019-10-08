package org.pdcarv.kafka;

import java.io.File;
import java.util.Properties;
import java.util.Random;

public class TestUtils 
{
    public static Properties createBrokerConfig(Integer nodeId, Integer port) 
    {
        Properties props = new Properties();
        props.put("brokerid", nodeId.toString());
        props.put("port", port.toString());
        props.put("log.dir", TestUtils.tempDir().getAbsolutePath());
        props.put("log.flush.interval", "1");
        props.put("zookeeper.connect", TestUtils.zookeeperConnect());

        return props;
    }

    public static File tempDir() 
    {
        String ioDir = System.getProperty("java.io.tmpdir");
        File f = new File(ioDir, "kafka-" + new Random().nextInt(1000000));
        f.mkdirs();
        f.deleteOnExit();

        return f;
      }

    public static String zookeeperConnect() 
    {
        return "127.0.0.1:2182";
    }
}