package de.goldendeveloper.screenserver.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.goldendeveloper.screenserver.client.ScreenClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class WebReader {

    public WebReader() throws IOException {
        System.out.println("[WebReader] Start WebReader...");
        ServerSocket serverSocket = new ServerSocket(9958);
        System.out.println("[WebReader] WebReader ready");
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();

                BufferedReader incoming = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                PrintStream outgoing = new PrintStream(socket.getOutputStream());
                Stream<String> ts = incoming.lines();
                for (Object st : ts.toArray()) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode node = mapper.readTree(st.toString());
                    System.out.println(node);
                    if (node.has("type") && node.get("type").asText().equalsIgnoreCase("request")) {

                        sendToWebServer(ScreenClient.getScreenClients());
                    } else {
                        int id = node.get("id").asInt();
                        if (node.has("image")) {
                            String img = node.get("image").asText();
                            if (!img.isBlank()) {
                                System.out.println("[WebReader] Bild Empfangen");
                                ScreenClient screenClient = ScreenClient.findByID(id);
                                if (screenClient != null) {
                                    System.out.println("[WebReader] Bild weiter an Client gesendet");
                                    screenClient.uploadImage(img, 0);
                                }
                            }
                        }

                        if (node.has("update")) {
                            System.out.println("[WebReader] Update Infos Empfangen");
                            ScreenClient screenClient = ScreenClient.findByID(id);
                            if (screenClient != null) {
                                JsonNode update = node.get("update");
                                if (update.has("type")) {
                                    screenClient.update(update.get("type").asText(), update.get("data").asText());
                                }
                                System.out.println("[WebReader] Update Infos weiter an Client gesendet");
                            }
                        }
                    }

                }
                outgoing.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void sendToWebServer(ObjectNode node) {
        System.out.println("[WebReader] Sending Data to WebServer...");
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 54654); //TODO: Get IpAdresse From Config or Default Settings
            OutputStream output = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(output, StandardCharsets.UTF_8);
            osw.write(node.toString());
            osw.flush();
            osw.close();
        } catch (UnknownHostException e) {
            System.out.println("[WebReader] Unknown Host...");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[WebReader] IOProbleme...");
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                    System.out.println("[WebReader] Socket geschlossen...");
                } catch (IOException e) {
                    System.out.println("[WebReader] Socket konnte nicht geschlossen werden...");
                    e.printStackTrace();
                }
            }
        }
    }
}