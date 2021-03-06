import java.io.*;

/**
 * Author: Kiran Chandrakant Pandharpatte UCID : kcp35 Project: Chat Application
 * Semester: Fall 2015 Submitted on : 18 December, 2015
 */

public class ChatMessage implements Serializable {
	public String name;
	public String message;

	public ChatMessage() {
	}

	public ChatMessage(String name, String message) {
		setName(name);
		setMessage(message);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}