import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            // Listen on port 5555
            ServerSocket serverSocket = new ServerSocket(5555);

            while (true) {
                // Accept incoming connection
                Socket clientSocket = serverSocket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                // Read client ID from client
                String clientID = in.readLine();

                // Read message from client
                String message = in.readLine();

                // Send same message back to client
                out.println(message);

                // Close the socket
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("I/O exception " + e.getMessage());
            System.exit(1);
        }
    }
}
    