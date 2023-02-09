import java.io.*;
import java.net.*;
import java.util.*;

// import required classes for input/output and networking

public class Client {
    public static void main(String[] args) throws IOException {
        // create a Scanner object to read input from the user
        Scanner sc = new Scanner(System.in);
        int ID = 0;
        // prompt the user to enter the server IP address
        System.out.print("Enter the server IP address: ");
        String serverIP = sc.nextLine();
        // prompt the user to enter the server port number
        System.out.print("Enter the server port number: ");
        int port = sc.nextInt();
        sc.nextLine();

        // try to establish a socket connection to the server
        try (Socket socket = new Socket(serverIP, port)) {
            System.out.println("Connected to server.");
            ID = ID + 1;
            System.out.println("Your ID number is " + ID);

            // get the output stream from the socket and create a PrintWriter object
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            // get the input stream from the socket and create a BufferedReader object
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            // infinite loop to continuously prompt the user to enter a message and receive responses from the server
            while (true) {
                System.out.print("Enter a message to send: ");
                String message = sc.nextLine();
                // send the message to the server
                pw.println(message);
                // receive and display the response from the server
                System.out.println("Response from server: " + br.readLine());
            }
        } 
        // catch exceptions if the server cannot be found or a message cannot be sent
        catch (UnknownHostException e) {
            System.out.println("Error: Could not connect to server.");
        } catch (IOException e) {
            System.out.println("Error: Could not send message.");
        }
    }
}   
