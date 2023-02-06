import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        String serverIp = args[0];
        int portNumber = Integer.parseInt(args[1]);
        Socket socket = new Socket(serverIp, portNumber);
        System.out.println("Connected to server " + serverIp + " on port " + portNumber);
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print("Enter message to send: ");
            String message = input.nextLine();
            // send message to server here
        }
    }
}
    