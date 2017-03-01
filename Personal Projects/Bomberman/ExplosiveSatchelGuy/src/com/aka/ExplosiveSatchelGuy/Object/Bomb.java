package com.aka.ExplosiveSatchelGuy.Object;

import java.awt.Graphics2D;

import com.aka.ExplosiveSatchelGuy.Manager.Assets;

public class Bomb extends object
{
	private boolean remove;
	private int strength;
	private int id;
	
	public Bomb(int X, int Y, int str, int pid)
	{
		super(X, Y);
		
		animation.setFrames(Assets.BOMB[0]);
		animation.setDelay(8);
		width = height = 16;
		
		strength = str;
		id = pid;
	}
	public int getStrength()
	{
	    return strength;
	}
	
	public int getID() {
		return id;
	}
	
	public void setDestroyed() {
		remove = true;
	}
	public boolean shouldRemove()
	{
		return remove;
	}
	public void update()
	{
		animation.update();
		
	    if(animation.hasPlayed() >= 8) {
			remove = true;
	    }
	    else if(animation.hasPlayed() == 3) {
			//set blowup animation
			animation.setFrames(Assets.INITIALEXPLOSION[0]);
			animation.setPlayed(4);
		}
	}
	public void draw(Graphics2D g)
	{
		super.draw(g);
	}
}