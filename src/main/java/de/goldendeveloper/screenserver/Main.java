package de.goldendeveloper.screenserver;

import java.io.IOException;

public class Main {

    private static Config config;
    private static MysqlConnection mysqlConnection;

    public static void main(String[] args) {
        config = new Config();
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
}
