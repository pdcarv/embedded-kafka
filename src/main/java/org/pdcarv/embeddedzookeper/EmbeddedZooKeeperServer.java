package org.pdcarv.embeddedzookeper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;

public class EmbeddedZooKeeperServer {
    private ZooKeeperServer server;
    static final Logger logger = LogManager.getLogger(EmbeddedZooKeeperServer.class.getName());

    public EmbeddedZooKeeperServer() {
        this.server = new ZooKeeperServer();
    }

    public ZooKeeperServer getInstance() {
        return this.server;
    }

    public EmbeddedZooKeeperServer build(ServerConfig config) throws IOException {
        FileTxnSnapLog txnLog = null;

        try {
            txnLog = new FileTxnSnapLog(new File(config.getDataLogDir()), new File(config.getDataDir()));
            txnLog.setServerStats(this.server.serverStats());

            this.server.setTxnLogFactory(txnLog);
            this.server.setTickTime(config.getTickTime());
            this.server.setMinSessionTimeout(config.getMinSessionTimeout());
            this.server.setMaxSessionTimeout(config.getMaxSessionTimeout());
        } catch (IOException e) {
            logger.warn("IO Exception", e);
        } finally {
            if (txnLog != null) {
                txnLog.close();
            }
        }

        return this;
    }

    public EmbeddedZooKeeperServer registerServerShutdownHandler(Object handler) {
        String methodName = "registerShutdownHandler";

        try {
            Method registerServerhandler = ZooKeeperServer.class.getDeclaredMethod(methodName, (Class<?>[]) handler);
            registerServerhandler.setAccessible(true);
            registerServerhandler.invoke(this.server, handler);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
        }

        return this;
    }
}