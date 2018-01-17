package com.aka.ExplosiveSatchelGuy.Main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import com.aka.ExplosiveSatchelGuy.Manager.Keyboard;
import com.aka.ExplosiveSatchelGuy.Manager.ScreenManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener
{
	//	thread loop
	private Thread thread;
	private boolean running;
	
	//	display
	public final static int HEIGHT = 208;
	public final static int WIDTH = 284;
	
	//  draw resolution scale
	private final int SCALE = 4;
	
	//	drawing
	private Graphics2D g;
	private BufferedImage image;

	//	screen manager
	private ScreenManager sm;

	public GamePanel()
	{
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}
	public void addNotify()
	{
		super.addNotify();
		if(thread == null)
		{
			addKeyListener(this);
			thread = new Thread(this);
			thread.start();
		}
	}
	@SuppressWarnings("static-access")
	public void run()
	{
		initialize();
		
		while(running)
		{
			//	drawing screen aka paint component
			update();
			draw();
			drawToScreen();
			
			try
			{
				thread.sleep(16);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	//	initialize variables
	public void initialize()
	{
		running = true;
		//	creating bufferedImage to draw to
		image = new BufferedImage(WIDTH, HEIGHT, 1);
		//	bufferedImage is going to be drawn to
		g = (Graphics2D) image.getGraphics();
		sm = new ScreenManager();
	}
	private void update()
	{
		//	updates the variables
		sm.update();
		Keyboard.update();
	}
	private void draw()
	{
		//	draws to the bufferedImage
		sm.draw(g);
	}
	//	takes bufferedImage to screen
	private void drawToScreen()
	{
		//	get graphics of this panel
		Graphics g2 = getGraphics();
		//	drawing bufferedImage to the panel
		g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		//	gets rid of memory being used by g2
		g2.dispose();
	}
	//	handling keyboard inputs
	public void keyTyped(KeyEvent key)
	{
		
	}
	//	whenever a key is pressed set it to true
	public void keyPressed(KeyEvent key)
	{
		Keyboard.keySet(key.getKeyCode(), true);
	}
	//	whenever a key is released set it to false
	public void keyReleased(KeyEvent key)
	{
		Keyboard.keySet(key.getKeyCode(), false);
	}
}