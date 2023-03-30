import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;

public class ServerTest {

    private ServerSocket serverSocket;

    @Before
    public void setUp() throws Exception {
        serverSocket = new ServerSocket(1234);
    }

    @After
    public void tearDown() throws Exception {
        serverSocket.close();
    }

    @Test
    public void testStartServer() throws IOException {
        Server server = new Server(serverSocket);
        Thread thread = new Thread(server::startServer);
        thread.start();

        Socket clientSocket = new Socket("localhost", 1234);
        assertNotNull(clientSocket);
        assertTrue(clientSocket.isConnected());

        thread.interrupt();
    }

    @Test
    public void testGetClientID() throws IOException {
        Server server = new Server(serverSocket);
        Thread thread = new Thread(server::startServer);
        thread.start();

        Socket clientSocket = new Socket("localhost", 1234);
        assertNotNull(clientSocket);

        String clientId = Server.getClientID(clientSocket);
        assertNotNull(clientId);

        thread.interrupt();
    }

    @Test
    public void testCloseServerSocket() throws IOException {
        Server server = new Server(serverSocket);
        server.classServerSocket();

        assertTrue(serverSocket.isClosed());
    }
}