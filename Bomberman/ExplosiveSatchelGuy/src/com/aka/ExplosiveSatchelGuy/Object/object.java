package com.aka.ExplosiveSatchelGuy.Object;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.aka.ExplosiveSatchelGuy.Object.Bomb;
import com.aka.ExplosiveSatchelGuy.Object.Wall;
import com.aka.ExplosiveSatchelGuy.Screen.PlayScreen;

public abstract class object {

	//dimensions
	protected int width;
	protected int height;

	//current position
	protected int x;
	protected int y;
	protected int xPixel;
	protected int yPixel;

	//position to move to
	protected int xDest;
	protected int yDest;

	//movement
	protected boolean moving;
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;

	//attributes
	protected int movementSpeed;

	protected int tileSize;
	
	protected Animation animation;
	protected int currentAnimation;

	public object(int X, int Y) {
		tileSize = 16;
		animation = new Animation();
		
		x = X;
		y = Y;
		
		xPixel = xDest = x * tileSize;
		yPixel = yDest = y * tileSize;
	}

	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getRowPixel()
	{
		return xPixel;
	}
	
	public int getColPixel()
	{
		return yPixel;
	}
	
	public void setPosition(int X, int Y)
	{
		x = X;
		y = Y;
		
		xPixel = xDest = x * tileSize;
		yPixel = yDest = y * tileSize;
	}
	
	public void setLeft()
	{
		if(moving) return;
		left = true;
		moving = validateNextPosition();
	}
	
	public void setRight()
	{
		if(moving) return;
		right = true;
		moving = validateNextPosition();
	}
	
	public void setUp()
	{
		if(moving) return;
		up = true;
		moving = validateNextPosition();
	}
	
	public void setDown()
	{
		if(moving) return;
		down = true;
		moving = validateNextPosition();
	}
	
	//Check for collisions
	public boolean collides(object other)
	{
		return getRectangle().intersects(other.getRectangle());	
	}
	
	public Rectangle getRectangle()
	{
		return new Rectangle(xPixel, yPixel, width, height);
	}
	
	public boolean validateNextPosition()
	{
		if(moving)
			return true;

		xPixel = x * tileSize;
		yPixel = y * tileSize;
		
		int newX = x, newY = y;
		
		if(left)
			newX--;
		else if(right)
			newX++;
		else if(up)
			newY--;
		else if(down)
			newY++;
		
		for(Bomb b : PlayScreen.bombs)
		{
			if(b.getX() == newX && b.getY() == newY)
				return false;
		}
		for(Wall w : PlayScreen.walls)
		{
			if(w.getX() == newX && w.getY() == newY)
				return false;
		}
		//checks the indestructible walls
		if (PlayScreen.onWall(newX, newY))
			return false;
			
		if (left || right)
			xDest = newX * tileSize;
		else //if (up || down)
			yDest = newY * tileSize;
			
		return true;
	}

	public void getNextPosition()
	{
		if(left && xPixel > xDest) xPixel -= movementSpeed;
		else left = false;
		if(left && xPixel < xDest) xPixel = xDest;

		if(right && xPixel < xDest) xPixel += movementSpeed;
		else right = false;
		if(right && xPixel > xDest) xPixel = xDest;

		if(up && yPixel > yDest) yPixel -= movementSpeed;
		else up = false;
		if(up && yPixel < yDest) yPixel = yDest;

		if(down && yPixel < yDest) yPixel += movementSpeed;
		else down = false;
		if(down && yPixel > yDest) yPixel = yDest;   
	}


	public void update()
	{
		if(moving) getNextPosition();

		if(xPixel == xDest && yPixel == yDest)
		{
			left = right = up = down = moving = false;
			x = xPixel / tileSize;
			y = yPixel / tileSize;
		}

		//update animation
		animation.update();
	}

	public void draw(Graphics2D g)
	{
		g.drawImage(
				animation.getImage(),
				xPixel,
				yPixel,
				null
				);
	}
}
