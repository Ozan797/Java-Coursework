import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);

            // Prompt user to enter server IP address
            System.out.print("Enter server IP address: ");
            String serverAddress = sc.nextLine();

            // Prompt user to enter server port number
            System.out.print("Enter server port number: ");
            int portNumber = sc.nextInt();

            // Prompt user to enter client ID
            System.out.print("Enter client ID: ");
            sc.nextLine();
            String clientID = sc.nextLine();

            // Connect to the server
            Socket socket = new Socket(serverAddress, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Send client ID to server
            out.println(clientID);

            // Prompt user to enter message to send to server
            System.out.print("Enter message to send: ");
            String message = sc.nextLine();
            out.println(message);

            // Read response from server
            String response = in.readLine();
            System.out.println("Response from server: " + response);

            // Close the socket
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Unknown host " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("I/O exception " + e.getMessage());
            System.exit(1);
        }
    }
}
    