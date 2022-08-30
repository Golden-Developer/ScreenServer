package de.goldendeveloper.screenserver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
                    JsonNode node = mapper.readTree(st.toString());
                    String name;
                    if (node.has("name")) {
                        name = node.get("name").asText();
                    } else {
                        name = null;
                    }
                    String Port = node.get("Port").asText();
                    String IPAdresse = node.get("IPAdresse").asText();

                    System.out.println("Receiving Data Name: " + name + " Port: " + Port + " Server: " + IPAdresse);
                    System.out.println("Get Data: " + name + Port + IPAdresse);

                    //RUN CODE
                }
                outgoing.close();


/*                InputStream inputStream = socket.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
                File outputfile = new File(Main.getConfig().getImageOutputPath() + generatedString + ".jpg");
                ImageIO.write(bufferedImage, "jpg", outputfile);
                System.out.println(socket.getPort());
                System.out.println("Bild Empfangen");*/


                //bufferedInputStream.close();
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
