package com.aka.ExplosiveSatchelGuy.Manager;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import com.aka.ExplosiveSatchelGuy.Manager.Assets;

public class Assets
{
	public static BufferedImage[][] MENU = load("/Menu/menuscreen.gif", 284, 208);
	public static BufferedImage[][] HIGHLIGHT = load("/Menu/highlight.gif", 42, 18);
	public static BufferedImage[][] HIGHLIGHT2 = load("/Menu/highlight2.gif", 62, 18);
	public static BufferedImage[][] MAP = load("/Map/map.gif", 240, 208);
	public static BufferedImage[][] HUD = load("/Map/ui.gif", 44, 208);
	public static BufferedImage[][] font = load("/Menu/font.gif", 8, 8);
	public static BufferedImage[][] INTRO = load("/Menu/intro.gif", 284, 208);
	public static BufferedImage[][] PLAYER1HUD = load("/Menu/player1hud.gif", 14, 12);
	public static BufferedImage[][] PLAYER2HUD = load("/Menu/player2hud.gif", 14, 12);
	public static BufferedImage[][] POWERUP = load("/Sprites/items.gif", 16, 16);
	public static BufferedImage[][] PLAYER1 = load("/Sprites/player1.gif", 16, 24);
	public static BufferedImage[][] PLAYER2 = load("/Sprites/player2.gif", 16, 24);
	public static BufferedImage[][] BOMB = load("/Sprites/bomb.gif", 16, 16);
	public static BufferedImage[][] WALL = load("/Map/breakablewall.gif", 16, 16);
	public static BufferedImage[][] FIRE = load("/Sprites/fire.gif", 16, 16);
	public static BufferedImage[][] DEATH = load("/Sprites/death.gif", 18, 24);
	public static BufferedImage[][] INITIALEXPLOSION = load("/Sprites/initialexplostion.gif", 16, 16);
	public static BufferedImage[][] WALLDEATH = load("/Map/breakablewalldeath.gif", 16, 16);
	
	public static BufferedImage[][] load(String s, int w, int h) 
	{
		BufferedImage[][] ret;
		try 
		{
			BufferedImage spritesheet = ImageIO.read(Assets.class.getResourceAsStream(s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			ret = new BufferedImage[height][width];
			for(int i = 0; i < height; i++) 
			{
				for(int j = 0; j < width; j++) 
				{
					ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return ret;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
	public static void drawString(Graphics2D g, String s, int x, int y)
	{
		s = s.toUpperCase();
		for(int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if(c == 47)
				c = 36; // slash
			if(c == 58)
				c = 37; // colon
			if(c == 32)
				c = 38; // space
			if(c >= 65 && c <= 90)
				c -= 65; // letters
			if(c >= 48 && c <= 57)
				c -= 22; // numbers
			int row = c / font[0].length;
			int col = c % font[0].length;
			g.drawImage(font[row][col], x + 8 * i, y, null);
		}
	}
}