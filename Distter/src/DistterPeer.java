import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public abstract class DistterPeer {
	
	protected final File postFolder = new File("postFolder");
	
	protected volatile Socket socket;
	protected BufferedReader reader;
	protected BufferedWriter writer;
	protected BufferedReader terminal;
	protected volatile ArrayList<String> responses;
	protected String username;
	
	public DistterPeer(Socket socket, String username) {
		try {
			this.socket = socket;
			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			terminal = new BufferedReader(new InputStreamReader(System.in));
			
			responses = new ArrayList<>();
			
			this.username = username;
		} catch (IOException e) {
			closeEverything(socket, reader, writer);
		}
	}
	
	/**
	 * Listens for messages from the peer.
	 * Automatically responds to 5 requests (HELLO?, POSTS?, FETCH?, WHEN?, GOODBYE!)
	 * All other messages adds to ArrayList<String> 'responses'
	 */
	public abstract void listenForMessage();
	
// requests
	/**
	 * Request peer's current time and print it out
	 * @throws IOException
	 */
	protected abstract void requestTime() throws IOException;
	
	/**
	 * Send hello message
	 * @throws IOException
	 */
	protected void sayHello() throws IOException {
		writer.write("HELLO? DISTTER/1.0 " + username + "\n");
		writer.flush();
	}
	
	/**
	 * Request all posts created after sinceDate
	 * @param sinceDate - date of the posts creation
	 * @return String[] with IDs of all posts
	 * @throws IOException
	 */
	protected String[] requestAllPosts(long sinceDate) throws IOException {
		// request all posts
		writer.write("POSTS? " + sinceDate + " " + "0\n");
		writer.flush();
		
		String response = getResponse();
		
		if (response.startsWith("OPTIONS ")) {
			int optionCount = Integer.parseInt(response.substring(8));
			String[] allPosts = new String[optionCount];
			
			for (int i = 0; i < optionCount; i++) {
				String line;
				if ((line = getResponse()).startsWith("SHA-256 "))
					allPosts[i] = line.substring(8);
				else
					System.out.println("ERROR!");
			}
			return allPosts;
		} else {
			return new String[0];
		}
	}
	
	/**
	 * request post and return it in string format
	 * @param postID
	 * @return String version of the requested post
	 * @throws IOException
	 */
	protected String getWholePost(String postID) throws IOException {
		String wholePost = "";
		
		writer.write("FETCH? SHA-256 " + postID + "\n");
		writer.flush();
		String line = getResponse();
		if (line.equals("FOUND")) {
			line = getResponse();
			// get all headers
			while (!line.startsWith("Contents: ")) {
				wholePost += line + "\n";
				line = getResponse();
				
			}
			// get content's line
			wholePost += line + "\n";
			int contentCount = Integer.parseInt(line.substring(10));
			// get content of the post
			for (int i = 0; i < contentCount; i++) {
				line = getResponse();
				wholePost += line + "\n";
			}
			// remove last break line - "\n"
			wholePost = wholePost.substring(0, wholePost.length() - 1);
			System.out.println(wholePost);
			return wholePost;
		}
		else {
			line = getResponse();
			return null;
		}
	}
	
	/**
	 * reqest all posts from the server and save the new ones
	 * @throws IOException
	 */
	protected void getNewPosts() throws IOException {
//		System.out.println("\nChecking for new posts...");
		String[] allPosts = requestAllPosts(0);
		int newPostCount = 0;
		for (String postID : allPosts) {
			File possiblePost = new File(postFolder + "/" + postID + ".txt");
			if (possiblePost.exists())
				continue;
			else {
				newPostCount++;
				String postContent = getWholePost(postID);
				
				Post.savePost(postFolder, postContent);
			}
		}
//		if (newPostCount > 0)
//			System.out.println("Saved " + newPostCount + " new post(s)");
//		else
//			System.out.println("No new posts found");
	}
	
	
// responses
	
	/**
	 * send current time
	 * @throws IOException
	 */
	protected void sendTime() throws IOException {
		long currentUnixTime = System.currentTimeMillis() / 1000L;
		
		writer.write("NOW " + currentUnixTime + "\n");
		writer.flush();
	}
	
	/**
	 * Send list of posts matching requested parameters
	 * @param postsSince
	 * @param hashes - hashtags that must be included in post
	 * @throws IOException
	 */
	protected void sendPost(long postsSince, String[] hashes) throws IOException {
		
		ArrayList<String> options = new ArrayList<>();
		
		for (File postFile : postFolder.listFiles()) {
			Post post = new Post(postFolder.getName() + "/" + postFile.getName());
			
			// show posts after requested time
			if (post.getDate() >= postsSince) {
				// show posts with requested headers
				if (hashes.length > 0) {
					boolean hasTags = true;
					for (String hash : hashes) {
						if (!post.getHashtags().contains(hash)) {
							hasTags = false;
							break;
						}
					}
					if (hasTags)
						options.add(post.getCode());
				} else {
					options.add(post.getCode());
				}
			}
		}
		
		writer.write("OPTIONS " + options.size() + "\n");
		for (String id : options) {
			writer.write(id + "\n");
		}
		writer.flush();
	}
	
	/**
	 * Send requested post
	 * @param postID
	 * @throws IOException
	 */
	protected void sendFetch(String postID) throws IOException {
		BufferedReader br = null;
		String line;
		try {
			br = new BufferedReader(new FileReader(postFolder + "/" + postID + ".txt"));
			
			writer.write("FOUND\n");
			
			String post = "";
			
			while ((line = br.readLine()) != null) {
				post += line + "\n";
			}
			writer.write(post);
			
		} catch (FileNotFoundException e) {
			
			writer.write("SORRY\n");
		}
		
		writer.flush();
	}
	
	/**
	 * Send goodbye and close down a connection
	 * @param message
	 * @throws IOException
	 */
	protected void sendGoodbye(String message) throws IOException {
		writer.write("GOODBYE! " + message + "\n");
		writer.flush();
		closeEverything(socket, reader, writer);
	}
	
// utilities
	
	/**
	 * Format linux timestamp to more user-friendly string
 	 * @param unixSeconds
	 * @return
	 */
	protected String getFormattedDate(long unixSeconds) {
		// convert seconds to milliseconds
		Date date = new Date(unixSeconds*1000L);
		// the format of date
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		// convert date to string
		String formattedDate = sdf.format(date);
		// return string date
		return formattedDate;
	}
	
	/**
	 * Create post, input for post's content comes from the console
	 * @throws IOException
	 */
	protected void createPost() throws IOException {
		
		// get hashtags
		String hashtagsString = null;
		String[] hashtagsArray = new String[0];
		HashSet<String> hashtags = new HashSet<String>();
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
		for (String tag : hashtagsArray)
			hashtags.add(tag);
		
		// custom headers
		ArrayList<String> otherHeaders = new ArrayList<String>();
		boolean moreHeaders;
		do {
			System.out.print("Any custom headers? - Y/N - ");
			moreHeaders = terminal.readLine().equalsIgnoreCase("Y");
			if (!moreHeaders)
				break;
			else
				otherHeaders.add(terminal.readLine());
		} while (moreHeaders);
		
		
		String terminalString = "--END!";
		System.out.println("Content of the post:");
		System.out.println("When finished write '" + terminalString + "'");
		String postContent = "";
		while (true) {
			String line = terminal.readLine();
			if (!line.equals(terminalString))
				postContent += line + "\n";
			else
				break;
		}
		// remove last break line - "\n"
		postContent = postContent.substring(0, postContent.length()-1);
		
		long currentDate = System.currentTimeMillis() / 1000L;
		
		// TODO: implement possibility of the reply
		Post.createPost(currentDate, username, hashtags,otherHeaders, postContent, null, postFolder);
	}
	
	protected String getResponse() {
		
		// wait until this peer receives any responses
		while (responses.size() <= 0) {
		}
		
		String resp = responses.get(0);
		responses.remove(0);
		
		return resp;
	}
	
	/**
	 * Close socet, bufferReader and bufferWriter
	 * @param socket
	 * @param bufferedReader
	 * @param bufferedWriter
	 */
	protected void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		
		try {
			if (bufferedReader != null)
				bufferedReader.close();
			if (bufferedWriter != null)
				bufferedWriter.close();
			if (socket != null)
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
