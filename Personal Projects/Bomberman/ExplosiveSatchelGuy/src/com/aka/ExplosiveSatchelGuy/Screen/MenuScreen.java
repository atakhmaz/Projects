package com.aka.ExplosiveSatchelGuy.Screen;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.aka.ExplosiveSatchelGuy.Main.GamePanel;
import com.aka.ExplosiveSatchelGuy.Manager.*;

public class MenuScreen extends Screen
{
	private BufferedImage menuScreen;
	private BufferedImage highlight;
	
	private int currentOption = 0;
	private String[] options = 
	{
		"PLAY",
		"QUIT"
	};
	
	public MenuScreen(ScreenManager sm)
	{
		super(sm);
	}
	public void init()
	{
		menuScreen = Assets.MENU[0][0];
		highlight = Assets.HIGHLIGHT[0][0];
		Sound.load("/Sound/backgroundmusic.wav", "backgroundmusic");
		Sound.load("/Sound/confirm.wav", "confirm");
		Sound.load("/Sound/select.wav", "select");
		Sound.setVolume("backgroundmusic", -10);
		Sound.play("backgroundmusic");
	}
	public void update()
	{
		handleInput();
	}
	public void draw(Graphics2D g)
	{
		g.drawImage(menuScreen, 0, 0, null);
		Assets.drawString(g, options[0], (GamePanel.WIDTH/2)-16, (GamePanel.HEIGHT/2));
		Assets.drawString(g, options[1], (GamePanel.WIDTH/2)-16, (GamePanel.HEIGHT/2)+16);
		
		if(currentOption == 0) 
			g.drawImage(highlight, (GamePanel.WIDTH/2)-21, (GamePanel.HEIGHT/2)-5, null);
		else if(currentOption == 1) 
			g.drawImage(highlight, (GamePanel.WIDTH/2)-21, (GamePanel.HEIGHT/2)+10, null);
	}
	public void handleInput()
	{
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
			Sound.stop("backgroundmusic");
			sm.setScreen(ScreenManager.PLAY);
		}
		if(currentOption == 1)
		{
			Sound.stop("backgroundmusic");
			System.exit(0);
		}
	}
}
