import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.*;

public class ClientTest {

    private Socket socket;
    private Client client;

    @Before
    public void setUp() throws Exception {
        socket = new Socket("localhost", 1234);
        client = new Client(socket, "testUser");
    }

    @After
    public void tearDown() throws Exception {
        client.closeEverything(socket, client.bufferedReader, client.bufferedWriter);
    }

    @Test
    public void testSendMessage() throws IOException {
        String testMessage = "This is a test message.";
        InputStream inputStream = new ByteArrayInputStream(testMessage.getBytes());
        System.setIn(inputStream);

        client.sendMessage();

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String messageFromServer = reader.readLine();
        assertTrue(messageFromServer.contains(testMessage));
        assertTrue(messageFromServer.contains("testUser"));
        assertNotNull(messageFromServer);
    }

    @Test
    public void testListenForMessage() throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

        String testMessage = "This is a test message.";
        writer.write(testMessage);
        writer.newLine();
        writer.flush();

        client.listenForMessage();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream));
        Thread.sleep(1000);
        String consoleOutput = byteArrayOutputStream.toString();
        assertTrue(consoleOutput.contains(testMessage));
    }
}