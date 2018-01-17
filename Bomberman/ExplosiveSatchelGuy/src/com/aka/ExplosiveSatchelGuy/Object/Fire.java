package com.aka.ExplosiveSatchelGuy.Object;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.aka.ExplosiveSatchelGuy.Manager.Assets;
import com.aka.ExplosiveSatchelGuy.Screen.PlayScreen;

//added
enum DIR {LEFT, L_END, RIGHT, R_END, UP, U_END, DOWN, D_END}

public class Fire extends object {
	
	private boolean remove;
	private int strength;
	
	//Added
	private int leftDistance;
	private int rightDistance;
	private int upDistance;
	private int downDistance;
	
	private Animation[] fireAnim;
	private int NUMFIRE = 8;
	
	public Fire(int X, int Y, int str) {
		super(X, Y);
		width = height = 16;
		strength = str;
		
		fireAnim = new Animation[NUMFIRE];
		for(int i = 0; i < NUMFIRE; ++i)
		{
			fireAnim[i] = new Animation();
			fireAnim[i].setFrames(Assets.FIRE[i+1]);
			fireAnim[i].setDelay(2+strength*2);
		}
		
		setFire();
			
		animation.setFrames(Assets.FIRE[0]);
		animation.setDelay(2+strength*2);
	}
	
	public boolean shouldRemove() {
		return remove;
	}
	
	public boolean collides(object other)
	{
		Rectangle otherRect = other.getRectangle();
		
		//check updown fire
		int updownDistance = upDistance + downDistance + 1;
		int newYPixel = yPixel - upDistance * tileSize;
		
		 if((new Rectangle(xPixel, newYPixel, tileSize, updownDistance * tileSize).intersects(otherRect)))
			 return true;
			 
		//check leftright fire
		int leftrightDistance = leftDistance + rightDistance + 1;
		int newXPixel = xPixel - leftDistance * tileSize;
		
		 if((new Rectangle(newXPixel, yPixel, leftrightDistance * tileSize, tileSize).intersects(otherRect)))
			 return true;
			 
		return false;
	}
	
	//Added function
	public void setFire()
	{
		leftDistance = shortest(DIR.LEFT);
		rightDistance = shortest(DIR.RIGHT);
		upDistance = shortest(DIR.UP);
		downDistance = shortest(DIR.DOWN);	
	}
	
	private int shortest(DIR d) {
		int dx = 0, dy = 0;
		switch (d) {
			case UP :
				--dy;
				break;
			case DOWN :
				++dy;
				break;
			case LEFT :
				--dx;
				break;
			case RIGHT :
				++dx;
				break;
			default:
				break;
		}
		
		for(int i = 0; i < strength; ++i)
		{
			int newX = x + dx * (i + 1),
				newY = y + dy * (i + 1);
			for(Bomb b : PlayScreen.bombs)
				if(b.getX() == newX && b.getY() == newY) {
					//added
					b.setDestroyed();
					return i;
				}
					
			for(Wall w : PlayScreen.walls)
				if(w.getX() == newX && w.getY() == newY) {
					w.setDestroyed();
					return i + 1;
				}
					
			if(PlayScreen.onWall(newX,newY))
				return i;
		}
		
		return strength;
	}
	
	public Rectangle getRectangle()
	{
		System.out.println("Not possible to get fire's rectangle");
		
		return null;
	}
	
	public void update() {
		animation.update();
		
		for (int i = 0; i < NUMFIRE; ++i)
		{
			fireAnim[i].update();
		}
		if(animation.hasPlayedOnce()) remove = true;
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
		
		drawFire(g, DIR.UP);
		
		drawFire(g, DIR.DOWN);
		
		drawFire(g, DIR.LEFT);
		
		drawFire(g, DIR.RIGHT);
	}
	
	public void drawFire(Graphics2D g, DIR d) {
		DIR fDir = null;
		int distance = 0, dx = 0, dy = 0;
		
		switch (d) {
			case UP :
				--dy;
				distance = upDistance;
				fDir = DIR.UP;
				break;
				
			case DOWN :
				++dy;
				distance = downDistance;
				fDir = DIR.DOWN;
				break;
				
			case LEFT :
				--dx;
				distance = leftDistance;
				fDir = DIR.LEFT;
				break;
				
			case RIGHT :
				++dx;
				distance = rightDistance;
				fDir = DIR.RIGHT;
				break;
		default:
			break;
		}
		
		
		//	drawing the arm
		for (int i = distance - 1; i > 0; --i) {
			g.drawImage(fireAnim[fDir.ordinal()].getImage(), (x + (dx * i)) * tileSize, (y + (dy * i)) * tileSize, null);
		}
		
		//	drawing the end
		if (distance > 0)
			g.drawImage(fireAnim[fDir.ordinal() + 1].getImage(), (x + (dx * distance)) * tileSize, (y + (dy * distance)) * tileSize, null);
	}
}
