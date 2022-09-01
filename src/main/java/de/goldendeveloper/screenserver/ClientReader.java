package de.goldendeveloper.screenserver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

public class ClientReader {

    public ClientReader() throws IOException {
        System.out.println("[ClientReader] Start ClientReader...");
        ServerSocket serverSocket = new ServerSocket(Main.getConfig().getServerPort());
        System.out.println("[ClientReader] ClientReader ready");
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
                    String name = null;
                    if (node.has("name")) {
                        name = node.get("name").asText();
                    }
                    int Port = node.get("Port").asInt();
                    String IPAdresse = node.get("IPAdresse").asText();
                    String SSHPublic = node.get("SSHPublic").asText();

                    System.out.println("[ClientReader] Receiving Data Name: " + name + " Port: " + Port + " Server: " + IPAdresse);
                    System.out.println("[ClientReader] Get Data: " + name + " " + Port + " " + IPAdresse);

                    ScreenClient screenClient = ScreenClient.findByIpAdresse(IPAdresse);
                    if (screenClient == null) {
                        if (name == null) {
                            name = "none";
                        }
                        ScreenClient.create(name, Port,IPAdresse, SSHPublic);
                    } else {
                        System.out.println("Bereits vorhanden");
                    }

                }
                outgoing.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
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
}



/*
        ServerSocket serverSocket = new ServerSocket(Main.getConfig().getServerPort());
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
        File outputfile = new File("C:\\Users\\\\Desktop\\saved.jpg");
        ImageIO.write(bufferedImage, "jpg", outputfile);
        bufferedInputStream.close();
        socket.close();
*/
