package de.goldendeveloper.screenserver;

import com.google.common.net.InetAddresses;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Console {


    public void run() throws URISyntaxException, XMLStreamException, FileNotFoundException {
        String jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
        File file = new File(jarFolder + "/Config.xml");
        Scanner scanner = new Scanner(System.in);
        setWebServerPort(scanner, file);
        setServerPort(scanner, file);
        setMysqlPort(scanner, file);
        setMysqlIpAdresse(scanner, file);
        setMysqlUsername(scanner, file);
        setMysqlPassword(scanner, file);
        System.out.println("Alle Änderungen können im Dashboard angepasst werden!");
    }

    private void setWebServerPort(Scanner scanner, File file) throws XMLStreamException, FileNotFoundException {
        System.out.println("Bitte einen Web Server Port für diese Anwendung eingeben:");
        String webServerPort = scanner.nextLine();
        if (!webServerPort.isBlank()) {
            if (isInteger(webServerPort)) {
                System.out.println("Der Web Server Port lautet: " + webServerPort);
                Config.write(file, "WebServerPort", webServerPort);
            } else {
                System.out.println("Der Web Server Port muss eine Zahl sein!");
                this.setWebServerPort(scanner, file);
            }
        } else {
            Config.write(file, "WebServerPort", String.valueOf(Main.getConfig().getWebServerPort()));
            System.out.println("Der Web Server Port lautet: " + Main.getConfig().getWebServerPort());
        }
    }

    private void setMysqlPort(Scanner scanner, File file) throws XMLStreamException, FileNotFoundException {
        System.out.println("Bitte einen  Mysql Datenbank Port für diese Anwendung eingeben:");
        String mysqlPort = scanner.nextLine();
        if (!mysqlPort.isBlank()) {
            if (isInteger(mysqlPort)) {
                System.out.println("Der Mysql Datenbank Port lautet: " + mysqlPort);
                Config.write(file, "MysqlPort", mysqlPort);
            } else {
                System.out.println("Der Mysql Datenbank Port muss eine Zahl sein!");
                this.setMysqlPort(scanner, file);
            }
        } else {
            Config.write(file, "MysqlPort", String.valueOf(Main.getConfig().getMysqlPort()));
            System.out.println("Der Mysql Datenbank Port lautet: " + Main.getConfig().getMysqlPort());
        }
    }

    private void setMysqlUsername(Scanner scanner, File file) throws XMLStreamException, FileNotFoundException {
        System.out.println("Bitte einen Mysql Username für diese Anwendung eingeben:");
        String mysqlUsername = scanner.nextLine();
        if (!mysqlUsername.isBlank()) {
            System.out.println("Der Mysql Username lautet: " + mysqlUsername);
            Config.write(file, "MysqlUsername", mysqlUsername);
        } else {
            Config.write(file, "MysqlUsername", String.valueOf(Main.getConfig().getMysqlUsername()));
            System.out.println("Der Mysql Username lautet: " + Main.getConfig().getMysqlUsername());
        }
    }

    private void setMysqlPassword(Scanner scanner, File file) throws XMLStreamException, FileNotFoundException {
        System.out.println("Bitte ein Mysql Passwort für diese Anwendung eingeben:");
        String mysqlPassword = scanner.nextLine();
        if (!mysqlPassword.isBlank()) {
            System.out.println("Das Mysql Passwort lautet: " + mysqlPassword);
            Config.write(file, "MysqlPassword", mysqlPassword);
        } else {
            Config.write(file, "MysqlPassword", String.valueOf(Main.getConfig().getMysqlPassword()));
            System.out.println("Das Mysql Passwort lautet: " + Main.getConfig().getMysqlPassword());
        }
    }

    private void setMysqlIpAdresse(Scanner scanner, File file) throws XMLStreamException, FileNotFoundException {
        System.out.println("Bitte eine Mysql Ip-Adresse für diese Anwendung eingeben:");
        String mysqlIpAdresse = scanner.nextLine();
        if (!mysqlIpAdresse.isBlank()) {
            if (InetAddresses.isInetAddress(mysqlIpAdresse)) {
                System.out.println("Die Mysql Ip-Adresse lautet: " + mysqlIpAdresse);
                Config.write(file, "MysqlIpAdresse", mysqlIpAdresse);
            } else {
                System.out.println("Die Mysql Ip-Adresse " + mysqlIpAdresse + " konnte nicht gefunden werden");
                this.setMysqlIpAdresse(scanner, file);
            }
        } else {
            Config.write(file, "MysqlIpAdresse", Main.getConfig().getMysqlIpAdresse());
            System.out.println("Die Mysql Ip-Adresse lautet: " + Main.getConfig().getMysqlIpAdresse());
        }
    }

    private void setServerPort(Scanner scanner, File file) throws XMLStreamException, FileNotFoundException {
        System.out.println("Bitte einen Server Port für diese Anwendung eingeben:");
        String serverPort = scanner.nextLine();
        if (!serverPort.isBlank()) {
            if (isInteger(serverPort)) {
                System.out.println("Der Server Port lautet: " + serverPort);
                Config.write(file, "ServerPort", serverPort);
            } else {
                System.out.println("Der Server Port muss eine Zahl sein!");
                this.setServerPort(scanner, file);
            }
        } else {
            Config.write(file, "ServerPort", String.valueOf(Main.getConfig().getServerPort()));
            System.out.println("Der Server Port lautet: " + Main.getConfig().getServerPort());
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
