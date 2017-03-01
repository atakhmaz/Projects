package com.aka.ExplosiveSatchelGuy.Object;

import java.awt.Graphics2D;

import com.aka.ExplosiveSatchelGuy.Manager.Assets;

public class Wall extends object {
	
	private boolean remove;
	private boolean dead;
	
	public Wall(int X, int Y) {
		super(X, Y);
		
		width = height = 16;
		dead = remove = false;
		
		animation.setFrames(Assets.WALL[0]);
		animation.setDelay(10);
	}
	
	public void setDestroyed()
	{
		dead = true;
		animation.setFrames(Assets.WALLDEATH[0]);
		animation.setDelay(10);
	}
	public boolean shouldRemove() { return remove; }
	
	public void update() {
		if (dead)
			if (animation.hasPlayedOnce()) {
				remove = true;
				return;
			}
		
	    animation.update();
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}
}
