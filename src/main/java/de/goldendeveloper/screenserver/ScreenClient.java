package de.goldendeveloper.screenserver;

import de.goldendeveloper.mysql.entities.RowBuilder;
import de.goldendeveloper.mysql.entities.SearchResult;
import de.goldendeveloper.mysql.entities.Table;

import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Random;

public class ScreenClient {

    private String name;
    private Integer port;
    private Integer ID;
    private String IPAdresse;

    public ScreenClient(String name, int port, String IPAdresse, Integer ID) {
        this.IPAdresse = IPAdresse;
        this.port = port;
        this.name = name;
        this.ID = ID;
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

    public void setSSHPrivateKey(String SSHPrivateKey) throws URISyntaxException {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            String FileDirectory = table.getRow(table.getColumn("id"), String.valueOf(getID())).get().get(MysqlConnection.ColumnFileDirectory).getAsString();
            String directoryName = new File(ScreenClient.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath() + "/" + FileDirectory;

            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdir();
            }

            if (!SSHPrivateKey.isBlank()) {
                File file = new File(directoryName + "/id_rsa");
                try {
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(SSHPrivateKey);
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
    }

    public void setSSHPublicKey(String SSHPublicKey) throws URISyntaxException {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            String FileDirectory = table.getRow(table.getColumn("id"), String.valueOf(getID())).get().get(MysqlConnection.ColumnFileDirectory).getAsString();
            String directoryName = new File(ScreenClient.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath() + "/" + FileDirectory;

            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdir();
            }

            if (!SSHPublicKey.isBlank()) {
                File file = new File(directoryName + "/id_rsa.pub");
                try {
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(SSHPublicKey);
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
        }
    }

    public void delete() {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            if (table.hasColumn(MysqlConnection.ColumnPort)) {
                HashMap<String, SearchResult> map = table.getRow(table.getColumn(MysqlConnection.ColumnPort), String.valueOf(port)).get();
                table.dropRow(map.get("id").getAsInt());
            }
        }
    }

    public static ScreenClient findByPort(int port) {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            if (table.hasColumn(MysqlConnection.ColumnPort)) {
                HashMap<String, SearchResult> map = table.getRow(table.getColumn(MysqlConnection.ColumnPort), String.valueOf(port)).get();
                return new ScreenClient(map.get(MysqlConnection.ColumnName).getAsString(), port, map.get(MysqlConnection.ColumnIPAdresse).getAsString(), map.get("id").getAsInt());
            }
        }
        return null;
    }

    public static ScreenClient findByID(int ID) {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            if (table.hasColumn(MysqlConnection.ColumnPort)) {
                HashMap<String, SearchResult> map = table.getRow(table.getColumn("id"), String.valueOf(ID)).get();
                return new ScreenClient(map.get(MysqlConnection.ColumnName).getAsString(), map.get(MysqlConnection.ColumnPort).getAsInt(), map.get(MysqlConnection.ColumnIPAdresse).getAsString(),  map.get("id").getAsInt());
            }
        }
        return null;
    }

    public static ScreenClient findByName(String name) {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            if (table.hasColumn(MysqlConnection.ColumnPort)) {
                HashMap<String, SearchResult> map = table.getRow(table.getColumn(MysqlConnection.ColumnName), name).get();
                return new ScreenClient(map.get(MysqlConnection.ColumnName).getAsString(), map.get(MysqlConnection.ColumnPort).getAsInt(), map.get(MysqlConnection.ColumnIPAdresse).getAsString(),  map.get("id").getAsInt());
            }
        }
        return null;
    }

    public static ScreenClient create(String name, int port, String IPAdresse, String SSHPublicKey) throws URISyntaxException {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);

            int leftLimit = 97;
            int rightLimit = 122;
            int targetStringLength = 10;
            Random random = new Random();
            String FileDirectory = random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            String directoryName = new File(ScreenClient.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            directoryName = directoryName + "/" + FileDirectory;
            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdir();
            }

            if (!SSHPublicKey.isBlank()) {
                File file = new File(directoryName + "/id_rsa.pub");
                try {
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(SSHPublicKey);
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }

            table.insert(new RowBuilder()
                    .with(table.getColumn(MysqlConnection.ColumnName), name)
                    .with(table.getColumn(MysqlConnection.ColumnIPAdresse), IPAdresse)
                    .with(table.getColumn(MysqlConnection.ColumnPort), String.valueOf(port))
                    .with(table.getColumn(MysqlConnection.ColumnFileDirectory), FileDirectory)
                    .build()
            );

            int id = table.getRow(table.getColumn(MysqlConnection.ColumnName), name).get().get("id").getAsInt();

            return new ScreenClient(name, port, IPAdresse, id);
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

    public String getSSHPrivateKey() throws URISyntaxException {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            if (table.hasColumn("id")) {
                HashMap<String, SearchResult> map = table.getRow(table.getColumn("id"), String.valueOf(this.ID)).get();
                String FileDirectory = map.get(MysqlConnection.ColumnFileDirectory).getAsString();
                String directoryName = new File(ScreenClient.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath() + "/" + FileDirectory;

                try(BufferedReader br = new BufferedReader(new FileReader(directoryName + "/id_rsa"))) {
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();
                    while (line != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                        line = br.readLine();
                    }
                    return sb.toString();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getID() {
        return ID;
    }

    public String getSSHPublicKey() throws URISyntaxException {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            if (table.hasColumn("id")) {
                HashMap<String, SearchResult> map = table.getRow(table.getColumn("id"), String.valueOf(this.ID)).get();
                String FileDirectory = map.get(MysqlConnection.ColumnFileDirectory).getAsString();
                String directoryName = new File(ScreenClient.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath() + "/" + FileDirectory;

                try(BufferedReader br = new BufferedReader(new FileReader(directoryName + "/id_rsa.pub"))) {
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();
                    while (line != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                        line = br.readLine();
                    }
                    return sb.toString();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
}
