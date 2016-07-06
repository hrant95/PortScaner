package func;

import main.Gui;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by hrant on 2/28/16.
 */
public class Check {

    public Check() {
    }

    public boolean checkByPort(String address, int port) {
        SocketAddress socketAddress = new InetSocketAddress(address, port);
        Socket socket = new Socket();

        try {
            socket.connect(socketAddress, 3000);
            socket.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
