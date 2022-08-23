package de.goldendeveloper.screenserver;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class Config {

    private String WebServerPort;
    private int MysqlPort;
    private int ServerPort;
    private String MysqlIpAdresse;
    private String MysqlUsername;
    private String MysqlPassword;

    public Config() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream local = classloader.getResourceAsStream("config.xml");
        readXML(local);
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
                        this.WebServerPort = WebServerPort;
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

    public String getWebServerPort() {
        return WebServerPort;
    }
}

