import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DistterServer extends DistterPeer implements Runnable{
	
	private String clientUsername = "UNIDENTIFIED";
	
	public DistterServer(Socket socket, String username) {
		super(socket, username);
	}
//
	@Override
	public void run() {
		try {
			listenForMessage();
			
			// send hello
			sayHello();
			requestTime();
			
			// request all messages every minute
			// and save them
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (!socket.isClosed()) {
						try {
							getNewPosts();
						} catch (IOException e) {
							e.printStackTrace();
						}
						try {
							Thread.sleep(60000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Listens for messages from the peer.
	 * Automatically responds to 5 requests (HELLO?, POSTS?, FETCH?, WHEN?, GOODBYE!)
	 * All other messages adds to ArrayList<String> 'responses'
	 * Additionally, prints out all messages to the console with name of the peer that send it
	 */
	@Override
	public void listenForMessage() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String messageFromServer;
				
				// make sure the first message is hello
				try {
					while ((messageFromServer = reader.readLine()) == null) {
					}
					
					String[] firstMessage = messageFromServer.split(" ");
					
					if (firstMessage.length == 3) {
						if (!firstMessage[0].equals("HELLO?")) {
							sendGoodbye("Didn't get hello message");
						}
						else if (!firstMessage[1].equals("DISTTER/1.0")) {
							sendGoodbye("Yours DISTTER version is incorrect");
						}
						else {
							System.out.println("Connected with " + firstMessage[2]);
							clientUsername = firstMessage[2];
						}
					} else {
						sendGoodbye("Didn't get proper hello message");
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				while (socket.isConnected()) {
					try {
						messageFromServer = reader.readLine();
						if (messageFromServer != null) {
							System.out.println(clientUsername + ": " + messageFromServer);
							
							String[] tokenizedMessage = messageFromServer.split(" ");
							if (tokenizedMessage.length > 0) {
								switch (tokenizedMessage[0]) {
									case "HELLO?":
										sendGoodbye("Received HELLO message in the middle of the connection");
										break;
									case "WHEN?":
										sendTime();
										break;
									case "POSTS?":
										// get post requirements (time and hashtags count)
										long postsSince = Long.parseLong(tokenizedMessage[1]);
										int hashCount = Integer.parseInt(tokenizedMessage[2]);
										
										// get hashtags (if any)
										String[] hashes = new String[hashCount];
										for (int i = 0; i < hashCount; i++) {
											hashes[i] = reader.readLine();
											System.out.println(hashes[i]);
										}
										
										// make sure the time of the post is not in the future
										if (postsSince > System.currentTimeMillis() / 1000L) {
											sendGoodbye("ERROR - You cannot request posts from the future!");
											break;
										}
										
										// send possible options
										sendPost(postsSince, hashes);
										break;
									case "FETCH?":
										sendFetch(tokenizedMessage[2]);
										break;
									case "GOODBYE!":
										sendGoodbye("You said bye!");
										break;
									default:
										responses.add(messageFromServer);
										break;
								}
							}
						}
					} catch (IOException e) {
						closeEverything(socket, reader, writer);
						break;
					}
				}
			}
		}).start();
	}
	
// requests
	
	/**
	 * Request peer's current time and print it out
	 * @throws IOException
	 */
	@Override
	protected void requestTime() throws IOException {
		writer.write("WHEN?\n");
		writer.flush();
		long unixSeconds = Long.parseLong(getResponse().substring(4));
		System.out.println("\n" + clientUsername + "'s time:");
		System.out.println(getFormattedDate(unixSeconds));
	}
}
