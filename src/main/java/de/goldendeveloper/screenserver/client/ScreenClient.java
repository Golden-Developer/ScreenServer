package de.goldendeveloper.screenserver.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.goldendeveloper.mysql.entities.*;
import de.goldendeveloper.screenserver.Main;
import de.goldendeveloper.screenserver.MysqlConnection;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
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
                return new ScreenClient(map.get(MysqlConnection.ColumnName).getAsString(), map.get(MysqlConnection.ColumnPort).getAsInt(), map.get(MysqlConnection.ColumnIPAdresse).getAsString(), map.get("id").getAsInt());
            }
        }
        return null;
    }

    public static ScreenClient findByName(String name) {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            if (table.hasColumn(MysqlConnection.ColumnPort)) {
                HashMap<String, SearchResult> map = table.getRow(table.getColumn(MysqlConnection.ColumnName), name).get();
                return new ScreenClient(map.get(MysqlConnection.ColumnName).getAsString(), map.get(MysqlConnection.ColumnPort).getAsInt(), map.get(MysqlConnection.ColumnIPAdresse).getAsString(), map.get("id").getAsInt());
            }
        }
        return null;
    }

    public static ScreenClient findByIpAdresse(String ipadresse) {
        if (Main.getMysqlConnection().getMysql().existsDatabase(MysqlConnection.DatabaseNAME)) {
            Table table = Main.getMysqlConnection().getMysql().getDatabase(MysqlConnection.DatabaseNAME).getTable(MysqlConnection.TableClients);
            if (table.hasColumn(MysqlConnection.ColumnPort)) {
                try {
                    if (table.getColumn(MysqlConnection.ColumnIPAdresse).getAll().getAsString().contains(ipadresse)) {
                        HashMap<String, SearchResult> map = table.getRow(table.getColumn(MysqlConnection.ColumnIPAdresse), ipadresse).get();
                        return new ScreenClient(map.get(MysqlConnection.ColumnName).getAsString(), map.get(MysqlConnection.ColumnPort).getAsInt(), map.get(MysqlConnection.ColumnIPAdresse).getAsString(), map.get("id").getAsInt());
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    return null;
                }
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

                try (BufferedReader br = new BufferedReader(new FileReader(directoryName + "/id_rsa"))) {
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

    public void uploadImage(String img, int duration) {
        System.out.println("Sending Image to Client...");
        Socket socket = null;
        try {
            socket = new Socket(this.getIPAdresse(), this.getPort());
            OutputStream output = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(output, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode upload = mapper.createObjectNode();

            ObjectNode fileJson = mapper.createObjectNode();
            fileJson.put("byteArray", img);

            upload.set("image", fileJson);
            upload.put("duration", duration);

            ObjectNode json = mapper.createObjectNode();
            json.set("upload", upload);

            osw.write(json.toString());
            osw.flush();
            osw.close();
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host...");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOProbleme...");
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                    System.out.println("Socket geschlossen...");
                } catch (IOException e) {
                    System.out.println("Socket konnte nicht geschlossen werden...");
                    e.printStackTrace();
                }
            }
        }
    }

    public void update(String type, String info) {
        System.out.println("[ScreenClient] Sending Image to Client...");
        Socket socket = null;
        try {
            socket = new Socket(this.getIPAdresse(), this.getPort());
            OutputStream output = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(output, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode update = mapper.createObjectNode();

            switch (type.toLowerCase()) {
                case "clientport": update.put("clientport", Integer.parseInt(info));
                case "serveripadresse": update.put("serveripadresse", info);
                case "serverport": update.put("serverport", Integer.parseInt(info));
            }

            ObjectNode json = mapper.createObjectNode();
            json.set("update", update);

            osw.write(json.toString());
            osw.flush();
            osw.close();
            this.setPort(Integer.parseInt(info));
        } catch (UnknownHostException e) {
            System.out.println("[ScreenClient] Unknown Host...");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[ScreenClient] IOProbleme...");
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                    System.out.println("[ScreenClient] Socket geschlossen...");
                } catch (IOException e) {
                    System.out.println("[ScreenClient] Socket konnte nicht geschlossen werden...");
                    e.printStackTrace();
                }
            }
        }
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

                try (BufferedReader br = new BufferedReader(new FileReader(directoryName + "/id_rsa.pub"))) {
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
