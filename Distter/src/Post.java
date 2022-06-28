import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;

public class Post {
	private String code;
	private long date;
	private String author;
	private HashSet<String> hashtags;
	private ArrayList<String> otherHeaders;
	private String content = "";
	private int contentLength;
	private String replyCode;
	
	public String getCode() {
		return code;
	}
	public long getDate() {
		return date;
	}
	public String getAuthor() {
		return author;
	}
	public HashSet<String> getHashtags() {
		return hashtags;
	}
	public ArrayList<String> getOtherHeaders() {
		return otherHeaders;
	}
	public String getContent() {
		return content;
	}
	public String getReplyCode() {
		return replyCode;
	}
	public int getContentLength() {
		return contentLength;
	}
	
	public Post(String filePath) {
		hashtags = new HashSet<>();
		otherHeaders = new ArrayList<>();
		BufferedReader br = null;
		
		try {
			String line;
			br = new BufferedReader(new FileReader(filePath));
			
			// SHA code
			line = br.readLine();
			code = line.substring(9);
			
			while ((line = br.readLine()) != null) {
				// Meta data
				while (!line.startsWith("Contents: ")) {
					// Time
					if (line.startsWith("Created: ")) {
						date = Long.parseLong(line.substring(9));
					}
					// Author
					else if (line.startsWith("Author: ")) {
						author = line.substring(8);
					}
					// Hashtags
					else if (line.charAt(0) == '#') {
						hashtags.add(line);
					}
					// Content
					else if (line.startsWith("Contents: ")) {
						break;
					}
					// Reply
					else if (line.startsWith("Reply: ")) {
						replyCode = line.substring(7);
					}
					// Other headers
					else {
						otherHeaders.add(line);
					}

					line = br.readLine();

				}
				
				// Message content
				int lineCount = Integer.parseInt(line.substring(10));
				for (int i = 0; i < lineCount; i++) {
					line = br.readLine();
					content += line + "\n";
				}
				// remove last break line - "\n";
				content = content.substring(0, content.length() - 1);
			}
			
			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public Post(String code, long date, String author, HashSet<String> hashtags,
	            ArrayList<String> otherHeaders, String content, String replyCode) {
		this.code = code;
		this.date = date;
		this.author = author;
		this.hashtags = hashtags;
		this.otherHeaders = otherHeaders;
		this.content = content;
		this.replyCode = replyCode;
		this.contentLength = countLines(content);
	}
	
	public static Post createPost(long date, String author, HashSet<String> hashtags,
	                              ArrayList<String> otherHeaders, String content, String replyCode, File directory) {
		
		int contentLength = countLines(content);
		
		String postText = "Author: " + author + "\n" +
				"Created: " + date + "\n";
		if (replyCode != null)
			postText += "Reply: SHA-256 " + replyCode + "\n";
		for (String header : otherHeaders)
			postText += header + "\n";
		for (String tag : hashtags)
			postText += tag + "\n";
		postText += "Contents: " + contentLength + "\n" + content;
		
		String code = sha256(postText);
		
		Post post = new Post(code, date, author, hashtags, otherHeaders, content, replyCode);
		
		post.savePost(directory);
		
		return post;
	}
	
	public void savePost(File directory) {
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(directory.getName() + "/" + code + ".txt"), StandardCharsets.UTF_8))) {
			
			writer.write("Post-id: SHA-256 " + code + "\n");
			writer.write("Author: " + author + "\n");
			writer.write("Created: " + date + "\n");
			if (replyCode != null)
				writer.write("Reply: SHA-256 " + replyCode + "\n");
			for (String header : otherHeaders)
				writer.write(header + "\n");
			for (String tag : hashtags)
				writer.write(tag + "\n");
			writer.write("Contents: " + contentLength + "\n");
			writer.write(content);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void savePost(File directory, String post) {
		String code = post.substring(17, 81);
		
		
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(directory.getName() + "/" + code + ".txt"), StandardCharsets.UTF_8))) {
			
			writer.write(post);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static int countLines(String text) {
		int count = 0;
		if (!text.isEmpty())
			count++;
		for(char x : text.toCharArray()) {
			if (x == '\n')
				count++;
		}
		return count;
	}
	
	public static String sha256(final String base) {
		try{
			final MessageDigest digest = MessageDigest.getInstance("SHA-256");
			final byte[] hash = digest.digest(base.getBytes("UTF-8"));
			final StringBuilder hexString = new StringBuilder();
			for (int i = 0; i < hash.length; i++) {
				final String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
}