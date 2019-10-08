package org.pdcarv.Runners;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.pdcarv.zookeeper.ZookeeperStartable;

public class RunZooKeeper extends Thread {

    protected QuorumPeerConfig createZooKeeperProperties(Integer port) throws IOException, ConfigException {
        QuorumPeerConfig quorumConfiguration = new QuorumPeerConfig();
        File zkTmpDir = File.createTempFile("zookeeper", "test");

        if (zkTmpDir.delete() && zkTmpDir.mkdir()) {
            Properties properties = new Properties();
            properties.setProperty("dataDir", zkTmpDir.getAbsolutePath());
            properties.setProperty("clientPort", String.valueOf(port));

            quorumConfiguration.parseProperties(properties);
        }

        return quorumConfiguration;
    }

    public void run() {
        try {
            ServerConfig zkConfig = new ServerConfig();
            zkConfig.readFrom(createZooKeeperProperties(2182));
            ZookeeperStartable zKServer = new ZookeeperStartable(zkConfig);
            zKServer.startup();
        } catch (IOException | ConfigException e) {
            e.printStackTrace();
        }
    }
}