package com.aka.ExplosiveSatchelGuy.Screen;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.aka.ExplosiveSatchelGuy.Manager.Assets;
import com.aka.ExplosiveSatchelGuy.Manager.Keyboard;
import com.aka.ExplosiveSatchelGuy.Manager.ScreenManager;
import com.aka.ExplosiveSatchelGuy.Manager.Sound;

public class GameOverScreen extends Screen
{
	private BufferedImage highlight;
	
	private int currentOption;
	private String[] options = 
	{
		"MENU",
		"QUIT"
	};
	
	private String[] winner = 
	{
		"PLAYER 1 WINS",
		"PLAYER 2 WINS",
		"ITS A TIE"
	};
	
	public GameOverScreen(ScreenManager sm)
	{
		super(sm);
		currentOption = 0;
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
		Assets.drawString(g, "GAME OVER", 100, 60);
		Assets.drawString(g, options[0], 118, 100);
		Assets.drawString(g, options[1], 118, 118);
		
		if(PlayScreen.players[0].getKills() > PlayScreen.players[1].getKills())
			Assets.drawString(g, winner[0], 85, 80);
		else if (PlayScreen.players[0].getKills() < PlayScreen.players[1].getKills())
			Assets.drawString(g, winner[1], 85, 80);
		else
			Assets.drawString(g, winner[2], 90, 80);
		
		if(currentOption == 0) 
			g.drawImage(highlight, 113, 95, null);
		else if(currentOption == 1) 
			g.drawImage(highlight, 113, 113, null);
		
	}
	public void handleInput()
	{
		if(Keyboard.isPressed(Keyboard.ESCAPE))
		{
			Sound.play("confirm");
			System.exit(0);
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
			Sound.stop("Clapping");
			sm.setScreen(ScreenManager.MENU);
			sm.setPaused(false);
		}
		if(currentOption == 1)
		{
			Sound.stop("Clapping");
			System.exit(0);
		}
	}
}
