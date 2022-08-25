package de.goldendeveloper.screenserver;

import de.goldendeveloper.mysql.MYSQL;
import de.goldendeveloper.mysql.entities.Database;
import de.goldendeveloper.mysql.entities.Table;

public class MysqlConnection {

    private final MYSQL mysql;
    public static String DatabaseNAME = "ScreenServer";
    public static String TableClients = "Clients";
    public static String ColumnName = "Name";
    public static String ColumnPort = "Port";
    public static String ColumnIPAdresse = "IPAdresse";

    public MysqlConnection() {
        mysql = new MYSQL(Main.getConfig().getMysqlIpAdresse(), Main.getConfig().getMysqlUsername(), Main.getConfig().getMysqlPassword(), Main.getConfig().getMysqlPort());

        if (mysql.existsDatabase(DatabaseNAME)) {
            Database db = mysql.getDatabase(DatabaseNAME);
            if (db.existsTable(TableClients)) {
                Table table = db.getTable(TableClients);
                if (!table.hasColumn(ColumnName)) {
                    table.addColumn(ColumnName);
                }

                table = db.getTable(TableClients);
                if (!table.hasColumn(ColumnPort)) {
                    table.addColumn(ColumnPort);
                }

                table = db.getTable(TableClients);
                if (!table.hasColumn(ColumnIPAdresse)) {
                    table.addColumn(ColumnIPAdresse);
                }
            }
        }
        System.out.println("MYSQL Connection Finish");
    }

    public MYSQL getMysql() {
        return mysql;
    }
}
