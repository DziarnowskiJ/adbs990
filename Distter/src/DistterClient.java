import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;

public class DistterClient extends DistterPeer {
	
	public DistterClient(Socket socket, String username) {
		super(socket, username);
	}
	
	/**
	 * Wait for user's selection of action
	 * (close connection, get time, get post, create post)
	 */
	public void pickAction() {
		try {
			sayHello();
			
			// request all posts every minute and save them
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
			
			Scanner scanner = new Scanner(System.in);
			while (!socket.isClosed()) {
				System.out.println("");
				System.out.println("Possible options:");
				System.out.println("0 - GOODBYE!");
				System.out.println("1 - WHEN?");
				System.out.println("2 - GET POSTS");
				System.out.println("3 - CREATE POST");
				
				System.out.print("What should be done - ");
				String action = scanner.nextLine();
				
				switch (action) {
					case "0":
						sendGoodbye("Client closed down a connection");
						break;
					// WHEN?
					case "1":
						requestTime();
						break;
					// POST?
					case "2":
						String postID = requestPosts();
						if (postID != null) {
							Post post = fetchPost(postID);
							printPost(post);
						}
						break;
					case "3":
						createPost();
						break;
					// Unknown command
					default:
						System.out.println("Unknown command :(");
						break;
					
				}
			}
		} catch (IOException e) {
			closeEverything(socket, reader, writer);
		}
	}
	
	/**
	 * Listens for messages from the peer.
	 * Automatically responds to 5 requests (HELLO?, POSTS?, FETCH?, WHEN?, GOODBYE!)
	 * All other messages adds to ArrayList<String> 'responses'
	 */
	@Override
	public void listenForMessage() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String msg;
				
				try {
					
					// make sure the first message is hello
					msg = reader.readLine();
					String[] firstMessage = msg.split(" ");
					
					if (firstMessage.length == 3) {
						if (!firstMessage[0].equals("HELLO?")) {
							sendGoodbye("Didn't get hello message");
						}
						else if (!firstMessage[1].equals("DISTTER/1.0")) {
							sendGoodbye("Yours DISTTER version is incorrect");
						}
						else {
							System.out.println("Connected with " + firstMessage[2]);
						}
					} else {
						sendGoodbye("Didn't get proper hello message");
					}
					
					
					while (socket.isConnected()) {
						
						msg = reader.readLine();
						String[] tokenizedMessage = msg.split(" ");
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
									System.out.println("\n\nConnection was closed by the peer");
									if (tokenizedMessage.length > 1)
										System.out.print("Message received: ");
									for (int i = 1; i < tokenizedMessage.length; i++)
										System.out.print(tokenizedMessage[i] + " ");
									System.out.println("\n");
									sendGoodbye("You said bye!");
									break;
								default:
									responses.add(msg);
									break;
							}
						}
					}
				} catch (IOException e) {
					closeEverything(socket, reader, writer);
				}
				
				System.out.println("\nPROGRAM TERMINATED");
				System.exit(0);
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
		System.out.println("\nServer's time:");
		System.out.println(getFormattedDate(unixSeconds));
	}
	
	/**
	 * Request post from the peer
	 * @param postID - id of the post (SHA-256 .....)
	 * @return Post - fetched post or null if post was not found
	 * @throws IOException
	 */
	private Post fetchPost(String postID) throws IOException {
		String code = null;
		long date = 0;
		String author = null;
		final HashSet<String> hashtags = new HashSet<>();
		final ArrayList<String> otherHeaders = new ArrayList<>();
		String content = "";
		String replyCode = null;
		
		// Request that post
		writer.write("FETCH? " + postID + "\n");
		writer.flush();
		
		// Print post headers
		String line = getResponse();
		if (line.equals("FOUND")) {
			// SHA code
			code = getResponse().substring(17);
			
			// Rest of headers
			line = getResponse();
			while (!line.contains("Contents: ")) {
				// Meta data
				while (!line.contains("Contents: ")) {
					// Time
					if (line.contains("Created: ")) {
						date = Long.parseLong(line.substring(9));
					}
					// Author
					else if (line.contains("Author: ")) {
						author = line.substring(8);
					}
					// Hashtags
					else if (line.charAt(0) == '#') {
						hashtags.add(line);
					}
					// Reply
					else if (line.contains("Reply: ")) {
						replyCode = line.substring(15);
					}
					// Other headers
					else {
						otherHeaders.add(line);
					}
					
					line = getResponse();
				}
			}
			
			// Message content
			int lineCount = Integer.parseInt(line.substring(10));
			for (int i = 0; i < lineCount; i++) {
				line = getResponse();
				content += line + "\n";
			}
			content = content.substring(0, content.length() -1);
			
			return new Post(code, date, author, hashtags, otherHeaders, content, replyCode);
		}
		else {
			return null;
		}
	}
	
	/**
	 * Request posts with parameters that will be provided in the console,
	 * Method will ask user to choose a post form the list
	 * @return ID of the post that user chose
	 * @throws IOException
	 */
	private String requestPosts() throws IOException {
		// REQUEST POSTS
		
		// get time
		System.out.print("\nGet posts since (linux timestamp) - ");
		long startDate = Long.parseLong(terminal.readLine());
		while (startDate > System.currentTimeMillis() / 1000L ) {
			System.out.println("Time cannot be in the future");
			System.out.print("Input proper time - ");
			startDate = Integer.parseInt(terminal.readLine());
		}
		
		// get hashtags
		String hashtagsString = null;
		String[] hashtagsArray = new String[0];
		System.out.print("Any hashtags? - Y/N - ");
		boolean hasHashtags = terminal.readLine().equalsIgnoreCase("Y");
		if (hasHashtags) {
			System.out.println("Separate your hashtags with one space");
			System.out.println("When you are done click enter:");
			hashtagsString = terminal.readLine();
		}
		if (hashtagsString != null) {
			hashtagsArray = hashtagsString.split(" ");
		}
		
		// request posts from the peer
		writer.write("POSTS? " + startDate + " " + hashtagsArray.length + "\n");
		for (String tag : hashtagsArray) {
			writer.write(tag + "\n");
		}
		writer.flush();
		
		// RECEIVE POST COUNT
		String resp = getResponse();
		int postCount = Integer.parseInt(resp.substring(8));
		if (postCount > 0) {
			System.out.println("\nFound " + postCount + " posts:");
			String[] posts = new String[postCount];
			for (int i = 0; i < postCount; i++) {
				posts[i] = getResponse();
				System.out.println((i) + " - " + posts[i]);
			}
			
			// Chose which post to open
			System.out.print("Which post to open? - ");
			int chosenPost = Integer.parseInt(terminal.readLine());
			
			return posts[chosenPost];
		} else {
			System.out.println("\nNo posts found");
			
			return null;
		}
	}
	
	/**
	 * Print out post to the console
	 * @param post - post to be printed out
	 * @throws IOException
	 */
	private void printPost(Post post) throws IOException {
		if (post != null) {
			// Meta data
			System.out.println("\nMeta-data:");
			// code
			System.out.println("ID: " + post.getCode());
			System.out.println("Author: " + post.getAuthor());
			System.out.println("Date: " + getFormattedDate(post.getDate()));
			if (post.getReplyCode() != null) {
				System.out.println("Reply to: " + post.getReplyCode());
			}
			for (String otherHeader : post.getOtherHeaders())
				System.out.println(otherHeader);
			
			// Hashtags
			if (post.getHashtags().size() > 0) {
				System.out.println("\nHashtags:");
				for (String tag : post.getHashtags())
					System.out.print(tag + " ");
				System.out.println("");
			}
			
			// Content
			System.out.println("\nPost:");
			System.out.println(post.getContent());
			
			// Reply
			if (post.getReplyCode() != null) {
				System.out.println("\nThis post is a reply message");
				System.out.print("Would you like to see the original post? - Y/N - ");
				boolean nextPost = terminal.readLine().equalsIgnoreCase("Y");
				if (nextPost) {
					try {
						
						Post replyPost = fetchPost("SHA-256 " + post.getReplyCode());
						printPost(replyPost);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			System.out.println("No posts with this SHA found");
		}
	}
	
}
