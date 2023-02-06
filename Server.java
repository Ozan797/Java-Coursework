import java.io.*;
import java.net.*;

class Server {
    public static void main(String args[]) throws Exception {
        ServerSocket server = new ServerSocket(6789);
        System.out.println("Server Started");

        Socket client = server.accept();
        System.out.println("Client Connected");

        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter output = new PrintWriter(client.getOutputStream(), true);

        while (true) {
            String received = input.readLine();
            System.out.println("Received: " + received);
            output.println("Response: " + received);
        }
    }
}
