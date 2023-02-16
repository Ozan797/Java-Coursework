import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // Responsible for listening to the port and accepting the connection request from the client
    private ServerSocket serverSocket;

    // Constructor responsible for creating the server socket and keeping it running
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    // Method that starts the server and accepts incoming connections
    public void startServer() {
        try {
            // This loop keeps running as long as the server is running
            while (!serverSocket.isClosed()) {
                // Accept the connection request from the client
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                // Create a client handler to communicate with the client
                ClientHandler clientHandler = new ClientHandler(socket);

                // Create a new thread to handle the communication with this client
                Thread thread = new Thread(clientHandler);
                // Start the thread
                thread.start();
            }
        } catch (IOException e) {
            // If an error occurs, we just catch it and continue
        }
    }

    // Method to close the server socket if an error occurs
    public void classServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Main method to create the server and start it listening for connections
    public static void main(String[] args) throws IOException {
        // Create a server socket that listens on port 1234
        ServerSocket serverSocket = new ServerSocket(8080);
        // Create a new Server object using the server socket
        Server server = new Server(serverSocket);
        // Start the server
        server.startServer();
    }
}
