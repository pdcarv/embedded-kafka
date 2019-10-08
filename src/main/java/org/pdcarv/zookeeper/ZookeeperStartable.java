package org.pdcarv.zookeeper;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.ZooKeeperServerMain;

public class ZookeeperStartable {
    private ServerCnxnFactory cnxnFactory;
    private EmbeddedZooKeeperServer embeddedZooKeeperServer;
    private ServerConfig config;

    static final Logger logger = LogManager.getLogger(ZookeeperStartable.class.getName());

    public ZookeeperStartable(ServerConfig config) throws IOException {
        this.config = config;
        this.embeddedZooKeeperServer = new EmbeddedZooKeeperServer();
    }

    public void startup() throws IOException {
        try {
            final CountDownLatch shutdownLatch = new CountDownLatch(1);
            this.embeddedZooKeeperServer.configure(this.config);
            this.embeddedZooKeeperServer.registerShutdownHandler(
                    EmbeddedZooKeeperServerShutdownHandler.create(shutdownLatch));
            ZooKeeperServer zkInstance = this.embeddedZooKeeperServer.getInstance();

            this.cnxnFactory = ServerCnxnFactory.createFactory();
            this.cnxnFactory.configure(config.getClientPortAddress(), config.getMaxClientCnxns());
            this.cnxnFactory.startup(zkInstance);

            // Watch status of ZooKeeper server. It will do a graceful shutdown
            // if the server is not running or hits an internal error.
            shutdownLatch.await();
            shutdown();

            cnxnFactory.join();
            zkInstance.shutdown(true);
        } catch (InterruptedException e) {
            logger.warn("Program interrupted", e);
        }
    }

    public void shutdown() {
        if (cnxnFactory != null) {
            this.cnxnFactory.shutdown();
        }
    }
}