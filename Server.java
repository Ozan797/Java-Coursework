import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException {
        // Initialize the port number to 0
        int port = 0;

        // Prompt the user to enter the port number to listen on
        System.out.print("Enter the port number to listen on: ");

        // Read the port number from the user
        Scanner sc = new Scanner(System.in);
        port = sc.nextInt();

        // Consume the newline character after the port number
        sc.nextLine();

        // Try to create a ServerSocket on the specified port
        try (ServerSocket ss = new ServerSocket(port)) {
            // Print a message indicating that the server is listening on the specified port
            System.out.println("Server is listening on port " + port);

            // Keep listening for incoming connections indefinitely
            while (true) {
                // Try to accept a new incoming connection
                try (Socket socket = ss.accept()) {
                    // Print a message indicating that a client has connected
                    System.out.println("Client connected.");

                    // Get the output stream to send data to the client
                    OutputStream os = socket.getOutputStream();
                    PrintWriter pw = new PrintWriter(os, true);

                    // Get the input stream to receive data from the client
                    InputStream is = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));

                    // Keep reading messages from the client until the connection is closed
                    while (true) {
                        // Read the next message from the client
                        String message = br.readLine();

                        // If the message is null, the connection has been closed, so break out of the loop
                        if (message == null) {
                            break;
                        }

                        // Respond to the client by sending back a message indicating that the message was received
                        pw.println("Message received: " + message);
                    }
                } catch (IOException e) {
                    // If there was an error receiving the message, print an error message
                    System.out.println("Error: Could not receive message.");
                }
            }
        } catch (IOException e) {
            // If there was an error creating the ServerSocket, print an error message
            System.out.println("Error: Could not listen on port " + port);
        }
    }
}
