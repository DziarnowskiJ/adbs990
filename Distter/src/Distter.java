import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Distter {

//	private static final String host = "distter.city.ac.uk";
	private static final String host = "localhost";
	private static final int port = 20111;
	
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter username: ");
		String username = scanner.nextLine();
		Socket socket;
		
		try {
			// try to connect to a server
			socket = new Socket(host, port);
			
			DistterClient client = new DistterClient(socket, username);
			client.listenForMessage();
			client.pickAction();
			
		} catch (IOException e) {
			// no servers were found - become a server
			System.out.println("No servers found");
			System.out.println("Becoming a new server...");
			
			ServerSocket serverSocket = new ServerSocket(port);
			
			try {
				// wait for incoming connections
				while(!serverSocket.isClosed()) {
					socket = serverSocket.accept();
					System.out.println("SOMEONE HAS CONNECTED");
					DistterServer clientHandler = new DistterServer(socket, username);
					
					Thread thread = new Thread(clientHandler);
					thread.start();
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}
}
