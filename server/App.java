import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class App {

    public static void main(String[] args) throws IOException {

        String dirPath = "";
        int port = 3000;
        String fileName = "";
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
            dirPath = args[1];
            fileName = args[2];
        } else {
            System.out.println("Invalid number of arguments expected");
            System.exit(0);
        }
        File newDirectory = new File(dirPath);
        if (!newDirectory.exists()) {
            newDirectory.mkdir();
        }

        // Read & print cookies
        Cookie c = new Cookie();
        c.readCookieFile(dirPath + File.separator + fileName);
        // c.printCookies();

        ServerSocket ss = new ServerSocket(port);

        while (true) {
            Socket sock = ss.accept();
            System.out.printf("Websocket server started on port... %d\r\n", port);

            try {
                InputStream is = sock.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                DataInputStream dis = new DataInputStream(bis);

                String messageReceived = "";

                OutputStream os = sock.getOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);

                while(!messageReceived.toLowerCase().equals("quit")) {
                    messageReceived = dis.readUTF();

                    String retrievedCookie = c.getRandomCookie();
                    dos.writeUTF(retrievedCookie);
                    dos.flush();

                }

                dos.close();
                bos.close();
                os.close();

                dis.close();
                bis.close();
                is.close();

            } catch (EOFException ex) {
                System.err.println(ex.toString());

            } finally {
                sock.close();
            }
        }
    }
}