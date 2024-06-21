package de.goldendeveloper.screenserver;

import io.github.coho04.mysql.MYSQL;
import io.github.coho04.mysql.entities.Database;
import io.github.coho04.mysql.entities.Table;

public class MysqlConnection {

    private final MYSQL mysql;
    public static String DatabaseNAME = "ScreenServer";
    public static String TableClients = "Clients";
    public static String ColumnName = "Name";
    public static String ColumnFileDirectory = "FileDirectory";
    public static String ColumnPort = "Port";
    public static String ColumnIPAdresse = "IPAdresse";

    public MysqlConnection() {
        mysql = new MYSQL(Main.getConfig().getMysqlIpAdresse(), Main.getConfig().getMysqlUsername(), Main.getConfig().getMysqlPassword(), Main.getConfig().getMysqlPort());

        if (!mysql.existsDatabase(DatabaseNAME)) {
            mysql.createDatabase(DatabaseNAME);
        }

        Database db = mysql.getDatabase(DatabaseNAME);
        if (!db.existsTable(TableClients)) {
            db.createTable(TableClients);
        }

        Table table = db.getTable(TableClients);
        if (!table.hasColumn(ColumnName)) {
            table.addColumn(ColumnName);
        }

        if (!table.hasColumn(ColumnPort)) {
            table.addColumn(ColumnPort);
        }

        if (!table.hasColumn(ColumnIPAdresse)) {
            table.addColumn(ColumnIPAdresse);
        }

        if (!table.hasColumn(ColumnFileDirectory)) {
            table.addColumn(ColumnFileDirectory);
        }

        System.out.println("MYSQL Connection Finish");
    }

    public MYSQL getMysql() {
        return mysql;
    }
}
