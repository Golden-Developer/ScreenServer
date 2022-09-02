package de.goldendeveloper.screenserver;

import com.google.common.net.InetAddresses;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {

    private int WebServerPort;
    private int MysqlPort;
    private int ServerPort;
    private String MysqlIpAdresse;
    private String MysqlUsername;
    private String MysqlPassword;

    public Config() {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream local = classloader.getResourceAsStream("config.xml");

            String jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            File file = new File(jarFolder + "/Config.xml");

            Path path = Files.createTempFile("Config", ".xml");

            if (file.exists()) {
                InputStream targetStream = new FileInputStream(file);
                if (targetStream != null && targetStream.available() >= 0) {
                    readXML(targetStream);
                }
            } else {
                if (local != null && Files.exists(path)) {
                    readXML(local);
                } else {
                    local = classloader.getResourceAsStream("Config.xml");
                    readXML(local);
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        ;
    }

    private void readXML(InputStream inputStream) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputStream);
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("login");
            for (int i = 0; i < list.getLength(); i++) {
                if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) list.item(i);
                    String WebServerPort = element.getElementsByTagName("WebServerPort").item(0).getTextContent();
                    String MysqlPort = element.getElementsByTagName("MysqlPort").item(0).getTextContent();
                    String ServerPort = element.getElementsByTagName("ServerPort").item(0).getTextContent();
                    String MysqlIpAdresse = element.getElementsByTagName("MysqlIpAdresse").item(0).getTextContent();
                    String MysqlUsername = element.getElementsByTagName("MysqlUsername").item(0).getTextContent();
                    String MysqlPassword = element.getElementsByTagName("MysqlPassword").item(0).getTextContent();

                    if (!WebServerPort.isEmpty() || !WebServerPort.isBlank()) {
                        this.WebServerPort = Integer.parseInt(WebServerPort);
                    }
                    if (!MysqlPort.isEmpty() || !MysqlPort.isBlank()) {
                        this.MysqlPort = Integer.parseInt(MysqlPort);
                    }
                    if (!ServerPort.isEmpty() || !ServerPort.isBlank()) {
                        this.ServerPort = Integer.parseInt(ServerPort);
                    }
                    if (!MysqlIpAdresse.isEmpty() || !MysqlIpAdresse.isBlank()) {
                        this.MysqlIpAdresse = MysqlIpAdresse;
                    }
                    if (!MysqlUsername.isEmpty() || !MysqlUsername.isBlank()) {
                        this.MysqlUsername = MysqlUsername;
                    }
                    if (!MysqlPassword.isEmpty() || !MysqlPassword.isBlank()) {
                        this.MysqlPassword = MysqlPassword;
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public String getMysqlIpAdresse() {
        return MysqlIpAdresse;
    }

    public String getMysqlPassword() {
        return MysqlPassword;
    }

    public int getMysqlPort() {
        return MysqlPort;
    }

    public int getServerPort() {
        return ServerPort;
    }

    public String getMysqlUsername() {
        return MysqlUsername;
    }

    public Integer getWebServerPort() {
        return WebServerPort;
    }

    public static void write(File out, String key, String value) throws XMLStreamException, FileNotFoundException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = output.createXMLStreamWriter(new FileOutputStream(out));

        writer.writeStartElement("login");

        String WebServerPort = "WebServerPort";
        writer.writeStartElement("WebServerPort");
        if (WebServerPort.equalsIgnoreCase(key)) {
            writer.writeCharacters(value);
        } else {
            writer.writeCharacters(String.valueOf(Main.getConfig().getWebServerPort()));
        }
        writer.writeEndElement();

        String ServerPort = "ServerPort";
        writer.writeStartElement("ServerPort");
        if (ServerPort.equalsIgnoreCase(key)) {
            writer.writeCharacters(value);
        } else {
            writer.writeCharacters(String.valueOf(Main.getConfig().getServerPort()));
        }
        writer.writeEndElement();

        String MysqlPort = "MysqlPort";
        writer.writeStartElement(MysqlPort);
        if (key.equalsIgnoreCase(MysqlPort)) {
            writer.writeCharacters(value);
        } else {
            writer.writeCharacters(String.valueOf(Main.getConfig().getMysqlPort()));
        }
        writer.writeEndElement();


        String MysqlIpAdresse = "MysqlIpAdresse";
        writer.writeStartElement(MysqlIpAdresse);
        if (key.equalsIgnoreCase(MysqlIpAdresse)) {
            writer.writeCharacters(value);
        } else {
            writer.writeCharacters(String.valueOf(Main.getConfig().getMysqlIpAdresse()));
        }
        writer.writeEndElement();

        String MysqlUsername = "MysqlUsername";
        writer.writeStartElement(MysqlUsername);
        if (key.equalsIgnoreCase(MysqlUsername)) {
            writer.writeCharacters(value);
        } else {
            writer.writeCharacters(Main.getConfig().getMysqlUsername());
        }
        writer.writeEndElement();

        String MysqlPassword = "MysqlPassword";
        writer.writeStartElement(MysqlPassword);
        if (MysqlPassword.equalsIgnoreCase(key)) {
            writer.writeCharacters(value);
        } else {
            writer.writeCharacters(String.valueOf(Main.getConfig().getMysqlPassword()));
        }
        writer.writeEndElement();

        writer.writeEndElement();

        writer.flush();
        writer.close();

        Main.setConfig(new Config());
    }

    public static Boolean Exists() throws Exception {
        String jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
        return Files.exists(Paths.get(jarFolder + "/Config.xml"));
    }

    public static void Export(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            stream = classloader.getResourceAsStream(resourceName);
            if (stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }
            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            resStreamOut = new FileOutputStream(jarFolder + "/Config.xml");
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } finally {
            assert stream != null;
            stream.close();
            assert resStreamOut != null;
            resStreamOut.close();
        }
    }

    public static Boolean Check() {
        if (!Console.isInteger(String.valueOf(Main.getConfig().getWebServerPort()))) {
            return false;
        }
        if (!Console.isInteger(String.valueOf(Main.getConfig().getServerPort()))) {
            return false;
        }
        if (!Console.isInteger(String.valueOf(Main.getConfig().getMysqlPort()))) {
            return false;
        }
        return InetAddresses.isInetAddress(Main.getConfig().getMysqlIpAdresse());
    }
}

