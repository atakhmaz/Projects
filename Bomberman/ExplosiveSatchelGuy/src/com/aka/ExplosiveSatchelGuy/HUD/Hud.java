package com.aka.ExplosiveSatchelGuy.HUD;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.aka.ExplosiveSatchelGuy.Manager.Assets;
import com.aka.ExplosiveSatchelGuy.Screen.PlayScreen;

public class Hud
{
	private int yoffset;

	private BufferedImage bar;
	private BufferedImage player1hud;
	private BufferedImage player2hud;

	public Hud()
	{
		yoffset = 240;

		bar = Assets.HUD[0][0];
		player1hud = Assets.PLAYER1HUD[0][0];
		player2hud = Assets.PLAYER2HUD[0][0];
	}

	public void draw(Graphics2D g)
	{
		g.drawImage(bar, yoffset, 0, null);
		g.drawImage(player1hud, yoffset+15, 70, null);
		g.drawImage(player2hud, yoffset+15, 120, null);

		String s = String.valueOf(PlayScreen.players[0].getKills());

		if(PlayScreen.players[0].getKills() >= 10)
			Assets.drawString(g, s, yoffset + 15, 85);
		else 
			Assets.drawString(g, s, yoffset + 19, 85);

		s = String.valueOf(PlayScreen.players[1].getKills());

		if(PlayScreen.players[1].getKills() >= 10)
			Assets.drawString(g, s, yoffset + 15, 135);
		else 
			Assets.drawString(g, s, yoffset + 19, 135);

		@SuppressWarnings("static-access")
		long time = PlayScreen.timer.getTime();
		int seconds = (int) (time/1000%60);
		int minutes = (int) (time/1000/60);
		String timer;

		if(seconds < 10)
			timer = String.valueOf(minutes) + ":0" + String.valueOf(seconds);
		else
			timer = String.valueOf(minutes) + ":" + String.valueOf(seconds);

		if(minutes < 10)
			Assets.drawString(g, timer, yoffset+7, 35);
		else
			Assets.drawString(g, timer, yoffset+10, 35);
	}
}