package de.goldendeveloper.screenserver;

import de.goldendeveloper.screenserver.client.ClientReader;
import de.goldendeveloper.screenserver.web.WebReader;

import java.io.*;

public class Main {

    private static Config config;
    private static MysqlConnection mysqlConnection;

    public static void main(String[] args) throws Exception {
        config = new Config();
        if (!Config.Exists()) {
            Config.Export("ExampleConfig.xml");
            new Console().run();
        }

        mysqlConnection = new MysqlConnection();

        new Thread(() -> {
            try {
                new ClientReader();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                new WebReader();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static Config getConfig() {
        return config;
    }

    public static MysqlConnection getMysqlConnection() {
        return mysqlConnection;
    }

    public static void setConfig(Config config) {
        Main.config = config;
    }
}
