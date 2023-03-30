import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ClientHandlerTest {

    private ServerSocket serverSocket;
    private Socket clientSocket1;
    private Socket clientSocket2;
    private ClientHandler clientHandler1;
    private ClientHandler clientHandler2;

    @Before
    public void setUp() throws Exception {
        // Create a server socket and accept two client connections
        serverSocket = new ServerSocket(1234);
        clientSocket1 = new Socket("localhost", 1234);
        clientSocket2 = new Socket("localhost", 1234);

        // Create two client handlers and start them as separate threads
        clientHandler1 = new ClientHandler(clientSocket1);
        clientHandler2 = new ClientHandler(clientSocket2);
        Thread thread1 = new Thread(clientHandler1);
        Thread thread2 = new Thread(clientHandler2);
        thread1.start();
        thread2.start();

        // Send a message from client 1 to client 2
        BufferedWriter writer1 = new BufferedWriter(new OutputStreamWriter(clientSocket1.getOutputStream()));
        writer1.write("Hello from client 1");
        writer1.newLine();
        writer1.flush();
    }

    @After
    public void tearDown() throws Exception {
        clientHandler1.closeEverything(clientSocket1, clientHandler1.bufferedReader, clientHandler1.bufferedWriter);
        clientHandler2.closeEverything(clientSocket2, clientHandler2.bufferedReader, clientHandler2.bufferedWriter);
        serverSocket.close();
    }

    @Test
    public void testBroadcastMessage() throws IOException {
        // Check if the message from client 1 was received by client 2
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()));
        String messageFromClient2 = reader2.readLine();
        assertTrue(messageFromClient2.contains("client 1"));
        assertTrue(messageFromClient2.contains("Hello from client 1"));
        assertNotNull(messageFromClient2);
    }

    @Test
    public void testRemoveClientHandler() throws IOException {
        // Remove client handler 2 and check if the client 2 has left the chat
        clientHandler2.removeClientHandler();
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
        String messageFromClient1 = reader1.readLine();
        assertTrue(messageFromClient1.contains("client 2"));
        assertTrue(messageFromClient1.contains("has left"));
        assertNotNull(messageFromClient1);

        // Check if the client handlers list is updated correctly
        ArrayList<ClientHandler> expectedClientHandlers = new ArrayList<>();
        expectedClientHandlers.add(clientHandler1);
        assertEquals(expectedClientHandlers, ClientHandler.clientHandlers);
    }

    @Test
    public void testGetClientsInfo() throws IOException {
        // Check if the getClientsInfo method returns a string with all connected clients' info
        String clientsInfo = ClientHandler.getClientsInfo();
        assertTrue(clientsInfo.contains("clientUsername"));
        assertTrue(clientsInfo.contains("clientID"));
        assertTrue(clientsInfo.contains("clientIP"));
        assertNotNull(clientsInfo);
    }

}