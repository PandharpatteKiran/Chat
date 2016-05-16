import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Author: Kiran Chandrakant Pandharpatte UCID : kcp35 Project: Chat Application
 * Semester: Fall 2015 Submitted on : 18 December, 2015
 */

public class Client extends Thread implements ActionListener {

	ChatMessage myObject;
	boolean sendingdone = false, receivingdone = false;
	Scanner scan;
	Socket socketToServer;
	ObjectOutputStream myOutputStream;
	ObjectInputStream myInputStream;
	Frame f;
	TextField tf, tname;
	TextArea ta;
	Label l;
	Button connect;
	Panel p;

	public Client() {

		f = new Frame();
		f.setSize(300, 400);
		f.setTitle("Chat Client");
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		tf = new TextField();
		tf.addActionListener(this);
		f.add(tf, BorderLayout.SOUTH);
		ta = new TextArea();
		f.add(ta, BorderLayout.CENTER);

		l = new Label();
		l.setText("Enter your name : ");
		tname = new TextField();
		connect = new Button("Connect");

		p = new Panel();
		p.setLayout(new GridLayout(1, 3));
		p.add(l);
		p.add(tname);
		p.add(connect);
		f.add(p, BorderLayout.NORTH);

		// Menu providing options to get History, User-List, Whiteboard or Disconnect
		MenuBar mb;
		mb = new MenuBar();
		Menu file = new Menu("File");
		MenuItem ulist = new MenuItem("Userlist");
		MenuItem history = new MenuItem("History");
		MenuItem wboard = new MenuItem("White Board");
		MenuItem disc = new MenuItem("Disconnect");

		file.add(ulist);
		file.add(history);
		file.add(wboard);
		file.add(disc);

		mb.add(file);
		f.setMenuBar(mb);

		// Listener to Display user list
		ulist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ta.append("***User List***" + "\n");

				myObject = new ChatMessage();
				myObject.setMessage("Userlist");
				myObject.setName("Userlist");

				try {
					myOutputStream.reset();
					myOutputStream.writeObject(myObject);
				} catch (IOException ioe) {
					System.out.println(ioe.getMessage());
				}

			}
		});

		// Listener to Read the file and display the History
		history.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				/*
				 * String fileName = tname.getText(); File file = new File(
				 * "C:\\Users\\Vivek\\Desktop\\Test\\", fileName + ".txt"); try
				 * { String read; BufferedReader br = new BufferedReader(new
				 * FileReader(file)); ta.append("----Your History----");
				 * ta.append("\n"); while ((read = br.readLine()) != null) {
				 * ta.append(read + System.lineSeparator()); read.toString();
				 * 
				 * 
				 * }
				 * 
				 * } catch (FileNotFoundException e) { // TODO Auto-generated
				 * catch block e.printStackTrace(); } catch (IOException e) { //
				 * TODO Auto-generated catch block e.printStackTrace(); }
				 * 
				 */

				ta.append("***History***" + "\n");

				myObject = new ChatMessage();
				myObject.setMessage("History");
				myObject.setName("History");

				try {
					myOutputStream.reset();
					myOutputStream.writeObject(myObject);
				} catch (IOException ioe) {
					System.out.println(ioe.getMessage());
				}

			}
		});

		// Listener to implement whiteboard
		wboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFrame frame = new JFrame("White Board");
				Container content = frame.getContentPane();
				// set layout on content pane
				content.setLayout(new BorderLayout());
				// create Whiteboard
				Whiteboard w = new Whiteboard();
				content.add(w, BorderLayout.CENTER);
				
				frame.setSize(600, 600);
				// show the swing paint result
				frame.setVisible(true);			
			}
		});

		// Listener to Close the streams when user wants to Disconnect
		disc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					socketToServer.close();
					myOutputStream.close();
					myInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// Listener for Text Field when user hits connect button
		connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					// scan = new Scanner(System.in);
					// myObject = new ChatMessage();
					// myObject.setName(n);
					socketToServer = new Socket("afsconnect1.njit.edu", 4321);
					myOutputStream = new ObjectOutputStream(socketToServer.getOutputStream());
					myInputStream = new ObjectInputStream(socketToServer.getInputStream());
					start();

					myObject = new ChatMessage();
					myObject.setMessage("Login");
					myObject.setName(tname.getText());
					tf.setText("");
					try {
						myOutputStream.reset();
						myOutputStream.writeObject(myObject);
					} catch (IOException ioe) {
						System.out.println(ioe.getMessage());
					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		});

		// Listener for the Text Field when user hits ENTER
		tname.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent ke) {
				// TODO Auto-generated method stub
				if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						// scan = new Scanner(System.in);
						// myObject = new ChatMessage();
						// myObject.setName(n);
						socketToServer = new Socket("afsconnect1.njit.edu", 4321);
						myOutputStream = new ObjectOutputStream(socketToServer.getOutputStream());
						myInputStream = new ObjectInputStream(socketToServer.getInputStream());
						/*
						 * 
						 * ChatMessage c = new ChatMessage();
						 * c.setMessage("Joining"); myOutputStream.reset();
						 * myOutputStream.writeObject(c);
						 */

						myObject = new ChatMessage();
						myObject.setMessage("Login");
						myObject.setName(tname.getText());
						tf.setText("");
						try {
							myOutputStream.reset();
							myOutputStream.writeObject(myObject);
						} catch (IOException ioe) {
							System.out.println(ioe.getMessage());
						}

						start();

					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
					System.out.println("Connected..");
				}
			}
		});

		f.setVisible(true);
	}

	public void actionPerformed(ActionEvent ae) {
		myObject = new ChatMessage();
		myObject.setMessage(tf.getText());
		String n = tname.getText();
		myObject.setName(n);

		tf.setText("");
		try {
			myOutputStream.reset();
			myOutputStream.writeObject(myObject);
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}

	public void run() {
		System.out.println("Listening for messages from server . . . ");
		try {			
			while (!receivingdone) {
				myObject = (ChatMessage) myInputStream.readObject();
				if (myObject.getName().equals("UserList\n") || myObject.getName().equals("History")) {
					ta.append(myObject.getMessage() + "\n");
				} else
					ta.append(myObject.getName() + ": " + myObject.getMessage() + "\n");
			}
		} catch (IOException ioe) {
			System.out.println("IOE: " + ioe.getMessage());
		} catch (ClassNotFoundException cnf) {
			System.out.println(cnf.getMessage());
		}
	}

	public static void main(String[] arg) {

		Client c = new Client();

	}
}
