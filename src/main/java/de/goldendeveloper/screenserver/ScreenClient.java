package de.goldendeveloper.screenserver;

import de.goldendeveloper.mysql.entities.RowBuilder;
import de.goldendeveloper.mysql.entities.SearchResult;
import de.goldendeveloper.mysql.entities.Table;

import java.util.HashMap;

public class ScreenClient {

    private String name;
    private Integer port;
    private String SSHPublicKey;
    private String IPAdresse;
    private String SSHPrivateKey;

    public ScreenClient(String name, int port, String IPAdresse) {
        this.IPAdresse = IPAdresse;
        this.port = port;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIPAdresse(String IPAdresse) {
        this.IPAdresse = IPAdresse;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setSSHPrivateKey(String SSHPrivateKey) {
        this.SSHPrivateKey = SSHPrivateKey;
    }

    public void setSSHPublicKey(String SSHPublicKey) {
        this.SSHPublicKey = SSHPublicKey;
    }

    public void delete() {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            if (table.hasColumn(MysqlConnection.ColumnPort)) {
                HashMap<String, SearchResult> map = table.getRow(table.getColumn(MysqlConnection.ColumnPort),  String.valueOf(port)).get();
                table.dropRow(map.get("id").getAsInt());
            }
        }
    }

    public static ScreenClient findByPort(int port) {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            if (table.hasColumn(MysqlConnection.ColumnPort)) {
              HashMap<String, SearchResult> map = table.getRow(table.getColumn(MysqlConnection.ColumnPort),  String.valueOf(port)).get();
                return new ScreenClient(map.get(MysqlConnection.ColumnName).getAsString(), port, map.get(MysqlConnection.ColumnIPAdresse).getAsString());
            }
        }
        return null;
    }

    public static ScreenClient findByID(int ID) {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            if (table.hasColumn(MysqlConnection.ColumnPort)) {
                HashMap<String, SearchResult> map = table.getRow(table.getColumn("id"),  String.valueOf(ID)).get();
                return new ScreenClient(map.get(MysqlConnection.ColumnName).getAsString(), map.get(MysqlConnection.ColumnPort).getAsInt(), map.get(MysqlConnection.ColumnIPAdresse).getAsString());
            }
        }
        return null;
    }

    public static ScreenClient findByName(String name) {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            if (table.hasColumn(MysqlConnection.ColumnPort)) {
              HashMap<String, SearchResult> map = table.getRow(table.getColumn(MysqlConnection.ColumnName),  name).get();
                return new ScreenClient(map.get(MysqlConnection.ColumnName).getAsString(),  map.get(MysqlConnection.ColumnPort).getAsInt(), map.get(MysqlConnection.ColumnIPAdresse).getAsString());
            }
        }
        return null;
    }

    public static ScreenClient create(String name, int port, String IPAdresse) {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            table.insert(new RowBuilder()
                    .with(table.getColumn(MysqlConnection.ColumnName), name)
                    .with(table.getColumn(MysqlConnection.ColumnIPAdresse), IPAdresse)
                    .with(table.getColumn(MysqlConnection.ColumnPort), String.valueOf(port))
                    .build()
            );
            return new ScreenClient(name, port, IPAdresse);
        }
        return null;
    }

    public Integer getPort() {
        return port;
    }

    public String getIPAdresse() {
        return IPAdresse;
    }

    public String getName() {
        return name;
    }

    public String getSSHPrivateKey() {
        return SSHPrivateKey;
    }

    public String getSSHPublicKey() {
        return SSHPublicKey;
    }
}
