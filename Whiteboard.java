import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;


import javax.swing.JComponent;

/**
 * Author: Kiran Chandrakant Pandharpatte UCID : kcp35 Project: Chat Application
 * Semester: Fall 2015 Submitted on : 18 December, 2015
 */

class Whiteboard extends JComponent {
	
	
	ObjectOutputStream myOutputStream;
	ObjectInputStream myInputStream;
	private Image image;	
	private Graphics2D graphics2;	
	private int currentX, currentY, oldX, oldY;

	public int getCurrentX() {
		return currentX;
	}

	public void setCurrentX(int currentX) {
		this.currentX = currentX;
	}

	public int getCurrentY() {
		return currentY;
	}

	public void setCurrentY(int currentY) {
		this.currentY = currentY;
	}

	public int getOldX() {
		return oldX;
	}

	public void setOldX(int oldX) {
		this.oldX = oldX;
	}

	public int getOldY() {
		return oldY;
	}

	public void setOldY(int oldY) {
		this.oldY = oldY;
	}

	public Whiteboard() {

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				setOldX(e.getX());
				setOldY(e.getY());
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				setCurrentX(e.getX());
				setCurrentY(e.getY());

				if (graphics2 != null) {					
					graphics2.drawLine(getOldX(), getOldY(), getCurrentX(), getCurrentY());
					//System.out.println("OX:" + getOldX() + "OY:" + getOldY() + "CX:" + getCurrentX() + "CY:" + getCurrentY());					
					repaint();  // refresh the Whiteboard for repainting					
					setOldX(getCurrentX());
					setOldY(getCurrentY());								
				}			
			}
		});
	}

	protected void paintComponent(Graphics g) {
		if (image == null) {			
			image = createImage(getSize().width, getSize().height);  // Create Image
			graphics2 = (Graphics2D) image.getGraphics();
			graphics2.setPaint(Color.white);			
			graphics2.fillRect(0, 0, getSize().width, getSize().height);
			graphics2.setPaint(Color.black);
			repaint();
		}
		g.drawImage(image, 0, 0, null);
	}

}
