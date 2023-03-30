import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class ClientHandler implements Runnable {

    // Static array list of client handlers to keep track of all clients
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    // The socket, input and output streams associated with the client
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientName;
    private String clientID;
    private String clientIP;


    // The username of the client
    private String clientUsername;

    // Whether this client is the coordinator for the chat or not
    private boolean isCoordinator;

    public ClientHandler(Socket socket) {
        try {
            // Initialize the input and output streams for the client's socket
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    
            // Read the username from the client
            this.clientUsername = bufferedReader.readLine();
            this.clientName = clientUsername;
            this.clientID = UUID.randomUUID().toString();
            this.clientIP = socket.getInetAddress().getHostAddress();
            bufferedWriter.write("Your ID is: " + clientID);
            bufferedWriter.newLine();
            bufferedWriter.write("Your IP address is: " + clientIP);
            bufferedWriter.newLine();
            bufferedWriter.flush();

    
            // Set isCoordinator to true if there are no other clients connected
            this.isCoordinator = false;
            if (clientHandlers.size() == 0) {
                this.isCoordinator = true;
                // Send a message to the first client that joins the server
                bufferedWriter.write("You are the first client to join the server therefore, you have the role of the coordinator.");
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
    
            // Add this client handler to the list of client handlers and broadcast a message
            clientHandlers.add(this);
            broadcastMessage(clientUsername + " has joined");
        } catch (IOException e) {
            // Close everything if an exception occurs during initialization
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }


    public void handleClientCommand(String messageFromClient) {
        if (messageFromClient.startsWith("/clients")) {
            String clientsInfo = getClientsInfo();
            try {
                bufferedWriter.write(clientsInfo);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getClientsInfo() {
        StringBuilder clientsInfo = new StringBuilder();
        for (ClientHandler clientHandler : clientHandlers) {
            clientsInfo.append(clientHandler.clientName).append(" ID: ").append(clientHandler.clientID)
                    .append(" IP: ").append(clientHandler.clientIP).append("\n");
        }
        return clientsInfo.toString();
    }
    

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                // Read a message from the client and broadcast it to all other clients
                messageFromClient = bufferedReader.readLine();
                // if (messageFromClient.startsWith("@")) {
                //     privateMessage(messageFromClient);
                // }
                if (isCoordinator) {
                    if (messageFromClient.startsWith("/")) {
                        handleClientCommand(messageFromClient);
                    }
                    broadcastMessage("Coordinator " + clientUsername + " ID: " + clientID + ": " + messageFromClient);
                } else {
                    broadcastMessage(clientUsername + " ID: " + clientID + ": " + messageFromClient);
                }
            } catch (IOException e) {
                // Close everything if an exception occurs during message broadcasting
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        // Iterate over all client handlers and send the message to each of them (except this one)
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (clientHandler.socket.isConnected()) {
                    if (!clientHandler.clientUsername.equals(clientUsername)) {
                        clientHandler.bufferedWriter.write(messageToSend);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();
                    }
                } else {
                    // Remove the client handler if the socket is no longer connected
                    clientHandlers.remove(clientHandler);
                }
            } catch (IOException e) {
                // Close everything if an exception occurs during broadcasting
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        // Remove this client handler from the list of client handlers
        clientHandlers.remove(this);
    
        // If this client was the coordinator, assign coordinator status to the first remaining client
        if (isCoordinator) {
            if (clientHandlers.size() > 0) {
                ClientHandler newCoordinator = clientHandlers.get(0);
                newCoordinator.isCoordinator = true;
                newCoordinator.broadcastMessage(newCoordinator.clientUsername + " is now the new coordinator.");
    
                // Send a message to all clients except the new coordinator
                for (ClientHandler clientHandler : clientHandlers) {
                    if (!clientHandler.equals(newCoordinator)) {
                        clientHandler.broadcastMessage("You are now the new coordinator");
                    }
                }
            }
        }
    
        // Broadcast a message indicating that this client has left
        broadcastMessage(clientUsername + " has left");
    }
    
    

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        // Remove this client handler from the list of client handlers and close all streams
        removeClientHandler();
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
    
    public String getClientID() {
        return this.clientID;
    }

    public Object getClientSocket() {
        return this.socket;
    }

}

   