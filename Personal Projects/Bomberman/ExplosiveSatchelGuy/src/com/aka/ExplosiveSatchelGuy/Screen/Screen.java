package com.aka.ExplosiveSatchelGuy.Screen;

import java.awt.Graphics2D;

import com.aka.ExplosiveSatchelGuy.Manager.ScreenManager;

public abstract class Screen 
{
	protected ScreenManager sm;

	public Screen(ScreenManager sm)
	{
		this.sm = sm;
	}

	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void handleInput();
}
