import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private static final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private static ClientHandler coordinator = null;

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket, ArrayList<ClientHandler> clientHandlers, boolean isCoordinator) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine();
            this.clientHandlers = clientHandlers;
            this.isCoordinator = isCoordinator;
            clientHandlers.add(this);
            broadcastMessage("Server: " + clientUsername + " has joined the chat!");
            if (coordinator == null && isCoordinator) {
                coordinator = this;
                sendMessage("You are the coordinator.");
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
        if (this == coordinator) {
            reassignCoordinatorRole();
        }
    }

    private void reassignCoordinatorRole() {
        if (!clientHandlers.isEmpty()) {
            coordinator = clientHandlers.get(0);
            coordinator.sendMessage("You are the new coordinator.");
            broadcastMessage("Server: " + coordinator.clientUsername + " is the new coordinator.");
        } else {
            coordinator = null;
        }
    }

    private void sendMessage(String messageToSend) {
        try {
            bufferedWriter.write(messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    private void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != this) {
                try {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }
    }

    private void removeClientHandler() {
        clientHandlers.remove(this);
        if (this == coordinator) {
            reassignCoordinatorRole();
        }
        broadcastMessage("Server: " + clientUsername + " has left the chat!");
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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
}
