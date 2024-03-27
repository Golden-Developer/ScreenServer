package de.goldendeveloper.screenserver;

import com.google.common.net.InetAddresses;
import io.github.cdimascio.dotenv.Dotenv;
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

    private final int WebServerPort;
    private final int MysqlPort;
    private final int ServerPort;
    private final String MysqlIpAdresse;
    private final String MysqlUsername;
    private final String MysqlPassword;

    public Config() {
        Dotenv dotenv = Dotenv.load();
        this.WebServerPort = Integer.parseInt(dotenv.get("WEB_SERVER_PORT"));
        this.MysqlPort = Integer.parseInt(dotenv.get("MYSQL_PORT"));
        this.ServerPort = Integer.parseInt(dotenv.get("SERVER_PORT"));
        this.MysqlIpAdresse = dotenv.get("MYSQL_IP_ADDRESS");
        this.MysqlUsername = dotenv.get("MYSQL_USERNAME");
        this.MysqlPassword = dotenv.get("MYSQL_PASSWORD");
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
}

