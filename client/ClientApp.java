import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientApp {
    
    public static void main(String[] args) throws IOException {

        String host = "";
        int port = 3000;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
            host = args[1];
        } else {
            System.out.println("Invalid number of arguments excepted");
            System.exit(0);
        }

        Socket sock = new Socket(host, port);

        InputStream is = sock.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        DataInputStream dis = new DataInputStream(bis);

        OutputStream os = sock.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(os);
        DataOutputStream dos = new DataOutputStream(bos);

        Console cons = System.console();
        String cookie = "";
        String keyBoardInput = "";

        while(!keyBoardInput.toLowerCase().equals("quit")) {
            keyBoardInput = cons.readLine("Enter '1' to request for a cookie ('quit' to terminate): ");
            
            dos.writeUTF(keyBoardInput);
            dos.flush();

            cookie = dis.readUTF();
            System.out.println(cookie);
        }

        dos.close();
        bos.close();
        os.close();

        dis.close();
        bis.close();
        is.close();
        sock.close();
    }
}
