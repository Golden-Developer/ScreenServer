package de.goldendeveloper.screenserver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.Stream;

public class WebReader {

    public WebReader() throws IOException {
        System.out.println("[WebReader] Start WebReader...");
        ServerSocket serverSocket = new ServerSocket(9958);
        System.out.println("[WebReader] WebReader ready");
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
                    int id = node.get("id").asInt();

                    if (node.has("Image")) {
                        InputStream inputStream = socket.getInputStream();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                        BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);

                        File outputfile = new File(Main.getConfig().getImageOutputPath() + generatedString + ".jpg");
                        ImageIO.write(bufferedImage, "jpg", outputfile);
                        ScreenClient screenClient = ScreenClient.findByID(id);

                        if (hasImage()) {
                            assert screenClient != null;
                            screenClient.uploadImage(null, 0);
                        }

                        System.out.println("[WebReader] Bild Empfangen");
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

    public Boolean hasImage() {
        return true;
    }
}


/*
*
* ByteArrayOutputStream os = new ByteArrayOutputStream();
ImageIO.write(image,"png", os);
InputStream fis = new ByteArrayInputStream(os.toByteArray());
* */