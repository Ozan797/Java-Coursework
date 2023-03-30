import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
// import java.util.UUID;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    // private String clientID;

    public Client(Socket socket, String username) {
        try {
            // Initialize socket, bufferedReader, bufferedWriter, username, and clientID
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            // this.clientID = UUID.randomUUID().toString(); // Generate a random UUID for the client ID
        } catch (IOException e) {
            // Close everything if there is an error
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            // Send the client's username and client ID to the server
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            try (Scanner scanner = new Scanner(System.in)) {
                while (socket.isConnected()) {
                    // Read a message from the user and send it to the server
                    String messageToSend = scanner.nextLine();
                    LocalTime timestamp = LocalTime.now();
                    String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("HH:mm"));
                    bufferedWriter.write( messageToSend + " [" + formattedTimestamp + "]");
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }
        } catch (IOException e) {
            // Close everything if there is an error
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroup;
                
                while (socket.isConnected()) {
                    try {
                        // Read a message from the server and print it to the console
                        msgFromGroup = bufferedReader.readLine();
                        System.out.println(msgFromGroup);
                    } catch (IOException e) {
                        // Close everything if there is an error
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            // Close the socket, bufferedReader, and bufferedWriter
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter your username: ");
            String username = scanner.nextLine();
            System.out.println("Enter the IP address: ");
            String ipAddress = scanner.nextLine();
            Socket socket = new Socket(ipAddress, 1234);
            Client client = new Client(socket, username);
            client.listenForMessage();
            client.sendMessage();
        }
    }
}
