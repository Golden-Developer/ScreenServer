import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {


        JFrame jframe = new JFrame("Server");
        jframe.setSize(400, 400);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JLabel jLableText = new JLabel("Waiting for image from client...");

        jframe.add(jLableText, BorderLayout.SOUTH);

        jframe.setVisible(true);


        ServerSocket serverSocket = new ServerSocket(1234);

        Socket socket = serverSocket.accept();

        InputStream inputStream = socket.getInputStream();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);


        BufferedImage bufferedImage = ImageIO.read(bufferedInputStream);

        bufferedInputStream.close();
        socket.close();

        JLabel jLablePic = new JLabel(new ImageIcon(bufferedImage));
        jLableText.setText("image received.");
        jframe.add(jLablePic, BorderLayout.CENTER);


    }
}
