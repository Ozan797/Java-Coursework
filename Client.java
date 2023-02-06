import java.io.*;
import java.net.*;

class Client {
    public static void main(String args[]) throws Exception {
        System.out.print("Enter Server IP Address: ");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String serverAddress = input.readLine();

        Socket client = new Socket(serverAddress, 6789);
        System.out.println("Connected to Server");

        PrintWriter output = new PrintWriter(client.getOutputStream(), true);
        BufferedReader serverInput = new BufferedReader(new InputStreamReader(client.getInputStream()));

        while (true) {
            System.out.print("Message to Server: ");
            String message = input.readLine();
            output.println(message);
            System.out.println("Response from Server: " + serverInput.readLine());
        }
    }
}