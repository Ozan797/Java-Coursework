import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private String clientID;
    private boolean isCoordinator = false;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            this.clientID = UUID.randomUUID().toString();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            if (isCoordinator) {
                bufferedWriter.write(username + " with ID " + clientID + " [Coordinator]");
            } else {
                bufferedWriter.write(username + " with ID " + clientID);
            }
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + " with ID " + clientID + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
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
                        msgFromGroup = bufferedReader.readLine();
                        System.out.println(msgFromGroup);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
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

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter the IP address: ");
        String ipAddress = scanner.nextLine();
        Socket socket = new Socket(ipAddress, 1234);
        Client client = new Client(socket, username);
        
        // Listen for client connection events
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromServer;

                try {
                    while (socket.isConnected()) {
                        msgFromServer = client.bufferedReader.readLine();
                        System.out.println(msgFromServer);
                        if (msgFromServer.startsWith("You are now the coordinator!")) {
                            client.isCoordinator = true;
                            System.out.println("You are now the coordinator!");
                        }
                    }
                } catch (IOException e) {
                    client.closeEverything(client.socket, client.bufferedReader, client.bufferedWriter);
                }
            }
        }).start();
        
        client.listenForMessage();
        client.sendMessage();
    }
}
