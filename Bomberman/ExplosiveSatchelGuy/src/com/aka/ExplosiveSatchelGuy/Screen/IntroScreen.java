package com.aka.ExplosiveSatchelGuy.Screen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.aka.ExplosiveSatchelGuy.Main.GamePanel;
import com.aka.ExplosiveSatchelGuy.Manager.Assets;
import com.aka.ExplosiveSatchelGuy.Manager.Keyboard;
import com.aka.ExplosiveSatchelGuy.Manager.ScreenManager;
import com.aka.ExplosiveSatchelGuy.Manager.Sound;

public class IntroScreen extends Screen
{
	private BufferedImage logo;

	private int alpha;
	private int ticks;

	private final int FADE_IN = 60;
	private final int LENGTH = 10;
	private final int FADE_OUT = 60;

	public IntroScreen(ScreenManager sm)
	{
		super(sm);
	}
	public void init() 
	{
		ticks = 0;
		logo = Assets.INTRO[0][0];
		Sound.load("/Sound/intro.wav", "intro");
		Sound.play("intro");
	}
	public void update()
	{
		handleInput();
		ticks++;
		if(ticks < FADE_IN)
		{
			alpha = (int) (255 - 255 * (1.0 * ticks / FADE_IN));
			if(alpha < 0) 
				alpha = 0;
		}
		if(ticks > FADE_IN + LENGTH)
		{
			alpha = (int) (255 * (1.0 * ticks - FADE_IN - LENGTH) / FADE_OUT);
			if(alpha > 255)
				alpha = 255;
		}
		if(ticks > FADE_IN + LENGTH + FADE_OUT)
		{
			sm.setScreen(ScreenManager.MENU);
		}
	}
	public void draw(Graphics2D g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.drawImage(logo, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
		g.setColor(new Color(0, 0, 0, alpha));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
	}
	public void handleInput()
	{
		if(Keyboard.isPressed(Keyboard.ENTER) || Keyboard.isPressed(Keyboard.ESCAPE))
		{
			Sound.stop("intro");
			sm.setScreen(ScreenManager.MENU);
		}
	}
}
