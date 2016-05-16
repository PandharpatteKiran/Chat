
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JComponent;

import org.omg.Messaging.SyncScopeHelper;

/**
 * Author: Kiran Chandrakant Pandharpatte UCID : kcp35 Project: Chat Application
 * Semester: Fall 2015 Submitted on : 18 December, 2015
 */

public class ChatServer {
	public static void main(String[] args) {
		ArrayList<ChatHandler> AllHandlers = new ArrayList<ChatHandler>();

		try {
			ServerSocket s = new ServerSocket(4321);
			for (;;) {
				Socket incoming = s.accept();
				new ChatHandler(incoming, AllHandlers).start();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static StringBuffer history = new StringBuffer();
}

class ChatHandler extends Thread {

	ChatMessage myObject = null;
	private Socket incoming;
	boolean done = false;
	ArrayList<ChatHandler> handlers;
	ObjectOutputStream out;
	ObjectInputStream in;

	String name;

	public ChatHandler(Socket i, ArrayList<ChatHandler> h) {
		incoming = i;
		handlers = h;
		handlers.add(this);
		try {
			in = new ObjectInputStream(incoming.getInputStream());
			out = new ObjectOutputStream(incoming.getOutputStream());
		} catch (IOException ioe) {
			System.out.println("Could not create streams.");
		}
	}

	public synchronized void broadcast() {
		ChatHandler left = null;

		if (myObject.getMessage().equals("Login")) {
			name = myObject.getName();
		}

		if (myObject.getMessage().equals("Userlist")) {

			StringBuffer sb = new StringBuffer();
			for (ChatHandler handler : handlers) {
				sb.append(handler.name + "\n");
			}
			ChatMessage cm = new ChatMessage();
			cm.setMessage(sb.toString());
			cm.setName("UserList\n");
			try {
				out.writeObject(cm);
				System.out.println("Writing to handler outputstream: " + cm.getMessage());
			} catch (IOException ioe) {
				// one of the other handlers hung up
				// remove that handler from the ArrayList
			}
			return;
		}

		if (myObject.getMessage().equals("History")) {

			ChatMessage cm = new ChatMessage();
			cm.setMessage(ChatServer.history.toString());
			cm.setName("History");
			try {
				out.writeObject(cm);
				System.out.println("Writing to handler outputstream: " + cm.getMessage());
			} catch (IOException ioe) {
				// one of the other handlers hung up
				// remove that handler from the ArrayList
			}
			return;
		}
		/*
		 * if(myObject.getMessage().equals("Whiteboard")){ ChatMessage cm = new
		 * ChatMessage(); cm.setMessage("Whiteboard"); cm.setName("Whiteboard");
		 * try { int ox = in.readInt(); int oy = in.readInt(); int cx =
		 * in.readInt(); int cy = in.readInt();
		 * 
		 * System.out.println("In Server Coordinates: " + ox + oy + cx + cy); }
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * try { out.writeObject(cm); System.out.println(
		 * "Writing to handler outputstream: " + cm.getMessage()); } catch
		 * (IOException ioe) { // one of the other handlers hung up // remove
		 * that handler from the ArrayList } return; }
		 */

		for (ChatHandler handler : handlers) {
			ChatMessage cm = new ChatMessage();
			cm.setMessage(myObject.getMessage());
			cm.setName(myObject.getName());
			try {
				handler.out.writeObject(cm);
				System.out.println("Writing to handler outputstream: " + cm.getMessage());
			} catch (IOException ioe) {
				// one of the other handlers hung up
				left = handler; // remove that handler from the ArrayList
			}
		}
		handlers.remove(left);

		if (myObject.getMessage().equals("bye")) { // my client wants to leave
			done = true;
			handlers.remove(this);
			System.out.println("Removed handler. Number of handlers: " + handlers.size());
		}
		System.out.println("Number of handlers: " + handlers.size());

	}

	public void run() {
		try {
			while (!done) {
				myObject = (ChatMessage) in.readObject();

				if (!"History".equals(myObject.getName()) && !"Userlist".equals(myObject.getName())) {
					ChatServer.history.append(myObject.getName() + ":" + myObject.getMessage());
					ChatServer.history.append("\n");
				}
				System.out.println("Message From : " + myObject.getName());
				System.out.println("Message Read : " + myObject.getMessage());
				broadcast();
			}
		} catch (IOException e) {
			if (e.getMessage().equals("Connection reset")) {
				System.out.println("A client terminated its connection.");
			} else {
				System.out.println("Problem receiving: " + e.getMessage());
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} finally {
			handlers.remove(this);
		}
	}
}
