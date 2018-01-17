package com.aka.ExplosiveSatchelGuy.Screen;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.aka.ExplosiveSatchelGuy.Manager.Assets;
import com.aka.ExplosiveSatchelGuy.Manager.Keyboard;
import com.aka.ExplosiveSatchelGuy.Manager.ScreenManager;
import com.aka.ExplosiveSatchelGuy.Manager.Sound;

public class PauseScreen extends Screen
{
	private BufferedImage highlight2;
	private BufferedImage highlight;
	
	private int currentOption;
	private String[] options = 
	{
		"RESUME",
		"MENU",
		"QUIT"
	};
	
	public PauseScreen(ScreenManager sm)
	{
		super(sm);
		currentOption = 0;
		highlight2 = Assets.HIGHLIGHT2[0][0];
		highlight = Assets.HIGHLIGHT[0][0];
	}
	public void init()
	{
		currentOption = 0;
	}
	public void update()
	{
		handleInput();
	}
	public void draw(Graphics2D g)
	{
		PlayScreen.pauseDraw(g);
		Assets.drawString(g, options[0], 110, 82);
		Assets.drawString(g, options[1], 118, 100);
		Assets.drawString(g, options[2], 118, 118);
		
		if(currentOption == 0) 
			g.drawImage(highlight2, 103, 77, null);
		else if(currentOption == 1) 
			g.drawImage(highlight, 113, 95, null);
		else if(currentOption == 2) 
			g.drawImage(highlight, 113, 113, null);
		
	}
	public void handleInput()
	{
		if(Keyboard.isPressed(Keyboard.ESCAPE))
		{
			PlayScreen.timer.pause();
			Sound.play("confirm");
			sm.setPaused(false);
			Sound.resumeLoop("music1");
		}
		if(Keyboard.isPressed(Keyboard.DOWN) && currentOption < options.length - 1)
		{
			Sound.play("select");
			currentOption++;
		}
		if(Keyboard.isPressed(Keyboard.UP) && currentOption > 0)
		{
			Sound.play("select");
			currentOption--;
		}
		if(Keyboard.isPressed(Keyboard.ENTER))
		{
			Sound.play("confirm");
			selectOption();
		}
	}
	private void selectOption()
	{
		if(currentOption == 0)
		{
			PlayScreen.timer.pause();
			sm.setPaused(false);
			Sound.resumeLoop("music1");
		}
		if(currentOption == 1)
		{
			sm.setScreen(ScreenManager.MENU);
			sm.setPaused(false);
		}
		if(currentOption == 2)
		{
			System.exit(0);
		}
	}
}
