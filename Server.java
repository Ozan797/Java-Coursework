import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // responsible to listen to the port and accept the connection request from the
    // client
    private ServerSocket serverSocket;

    // constructor responsible to create the server socket and keep it running
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        
        try {
            //this keeps running as long as the server is running
            while (!serverSocket.isClosed()) {
                //accept the connection request from the client
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");
                //responsible for communicating with the client
                ClientHandler clientHandler = new ClientHandler(socket);

                //creates the thread
                Thread thread = new Thread(clientHandler);
                //starts the thread
                thread.start();
                
            }

        } catch (IOException e) {
            
        }
    }

        //responsible to close the server socket if error occurs
    public void classServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        //server listening to clients on port 1234
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}