package de.goldendeveloper.screenserver;

public class Main {

    private static Config config;
    private static MysqlConnection mysqlConnection;

    public static void main(String[] args) throws Exception {
        config = new Config();
        mysqlConnection = new MysqlConnection();
        new Reader();
    }

    public static Config getConfig() {
        return config;
    }

    public static MysqlConnection getMysqlConnection() {
        return mysqlConnection;
    }
}
