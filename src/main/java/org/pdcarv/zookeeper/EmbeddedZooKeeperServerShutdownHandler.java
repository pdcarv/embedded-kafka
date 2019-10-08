package org.pdcarv.zookeeper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;

class EmbeddedZooKeeperServerShutdownHandler {
    public static Object create(CountDownLatch countDownLatch) {
        Constructor<?> konstructor;
        Object shutdownHandler = null;

        try {
            Class<?> klass = Class.forName("org.apache.zookeeper.server.ZooKeeperServerShutdownHandler");
            konstructor = klass.getDeclaredConstructor(CountDownLatch.class);
            konstructor.setAccessible(true);
            shutdownHandler = konstructor.newInstance(countDownLatch);
        } catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException
                | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return shutdownHandler;
    }
}