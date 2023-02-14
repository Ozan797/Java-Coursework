import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Server started on port 1234");

        ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
        ClientHandler coordinator = null;
        
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New client connected: " + socket);
        
            if (coordinator == null) {
                coordinator = new ClientHandler(socket, clientHandlers, true);
                System.out.println("Coordinator: " + coordinator.getClientUsername());
            } else {
                ClientHandler clientHandler = new ClientHandler(socket, clientHandlers, false);
                System.out.println("New client: " + clientHandler.getClientUsername());
            }
        }
        
    }
}

