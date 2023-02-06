import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) throws IOException {
        int port = 0;
        System.out.print("Enter the port number to listen on: ");
        Scanner sc = new Scanner(System.in);
        port = sc.nextInt();
        sc.nextLine();

        try (ServerSocket ss = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                try (Socket socket = ss.accept()) {
                    System.out.println("Client connected.");
                    OutputStream os = socket.getOutputStream();
                    PrintWriter pw = new PrintWriter(os, true);
                    InputStream is = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));

                    while (true) {
                        String message = br.readLine();
                        if (message == null) {
                            break;
                        }
                        pw.println("Message received: " + message);
                    }
                } catch (IOException e) {
                    System.out.println("Error: Could not receive message.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Could not listen on port " + port);
        }
    }
}  