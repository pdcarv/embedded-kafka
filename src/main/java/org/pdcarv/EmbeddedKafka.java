package org.pdcarv;

import java.io.IOException;

import org.pdcarv.Runners.RunKafka;
import org.pdcarv.Runners.RunZooKeeper;

public class EmbeddedKafka {
    public static void main(String[] args) throws IOException {
        RunZooKeeper zKeeper = new RunZooKeeper(); 
        RunKafka rKafka = new RunKafka();
        zKeeper.start();
        rKafka.start();
    }
}