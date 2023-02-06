import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int ID = 0;
        System.out.print("Enter the server IP address: ");
        String serverIP = sc.nextLine();
        System.out.print("Enter the server port number: ");
        int port = sc.nextInt();
        sc.nextLine();

        try (Socket socket = new Socket(serverIP, port)) {
            System.out.println("Connected to server.");
            ID = ID + 1;
            System.out.println("Your ID number is " + ID);

            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while (true) {
                System.out.print("Enter a message to send: ");
                String message = sc.nextLine();
                pw.println(message);
                System.out.println("Response from server: " + br.readLine());
            }
        } catch (UnknownHostException e) {
            System.out.println("Error: Could not connect to server.");
        } catch (IOException e) {
            System.out.println("Error: Could not send message.");
        }
    }
}   