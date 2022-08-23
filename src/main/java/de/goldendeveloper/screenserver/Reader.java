package de.goldendeveloper.screenserver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Reader {

    public Reader() throws IOException {
        ServerSocket serverSocket = new ServerSocket(Main.getConfig().getServerPort());
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);
        File outputfile = new File("C:\\Users\\\\Desktop\\saved.jpg");
        ImageIO.write(bufferedImage, "jpg", outputfile);
        bufferedInputStream.close();
        socket.close();
    }
}



/*        ServerSocket serverSocket = new ServerSocket(Main.getConfig().getServerPort());

        Socket socket = serverSocket.accept();
55603
        InputStream inputStream = socket.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

        BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);


        File outputfile = new File("C:\\Users\\\\Desktop\\saved.jpg");
        ImageIO.write(bufferedImage, "jpg", outputfile);

        bufferedInputStream.close();
        socket.close();
        */
