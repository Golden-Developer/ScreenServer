package de.goldendeveloper.screenserver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.Stream;

public class Reader {

    public Reader() throws IOException {
        ServerSocket serverSocket = new ServerSocket(Main.getConfig().getServerPort());
        while (true) {
            Socket socket = null;
            try {
                int leftLimit = 97;
                int rightLimit = 122;
                int targetStringLength = 10;
                Random random = new Random();

                String generatedString = random.ints(leftLimit, rightLimit + 1)
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();


                socket = serverSocket.accept();

                BufferedReader incoming = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                PrintStream outgoing = new PrintStream(socket.getOutputStream());
                Stream<String> ts = incoming.lines();
                for (Object st : ts.toArray()) {
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode object = mapper.createObjectNode();;
                    String name = object.get("name").asText();
                    String action = object.get("action").asText();
                    String server = object.get("server").asText();



                    System.out.println("Receiving Data Name: " + name + " Action: " + action + " Server: " + server);
                    System.out.println("Get Data: " + name + action + server);

                    //RUN CODE
                }
                outgoing.close();


                InputStream inputStream = socket.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
                File outputfile = new File(Main.getConfig().getImageOutputPath() + generatedString + ".jpg");
                ImageIO.write(bufferedImage, "jpg", outputfile);
                System.out.println(socket.getPort());
                System.out.println("Bild Empfangen");



                bufferedInputStream.close();
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
