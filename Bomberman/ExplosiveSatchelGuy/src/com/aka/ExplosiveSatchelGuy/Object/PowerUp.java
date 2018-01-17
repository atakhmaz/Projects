package com.aka.ExplosiveSatchelGuy.Object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.aka.ExplosiveSatchelGuy.Manager.Assets;

public class PowerUp extends object {
	private BufferedImage sprite;
	private int type;
	
	public final int NUMBOMBS = 0;
	public final int BOMBSTRENGTH = 1;
	public final int MOVESPEED = 2;
	public final int KICKBOMB = 3;
	
	//	no kickbomb
	private final int NUMPOWERUPS = 3;
	
	private Random rand;

	public PowerUp(int X, int Y) {
		super(X, Y);
		
		rand = new Random();
	    
	    type = rand.nextInt(NUMPOWERUPS);
	    
	    // WIP: get this sprite working
	    //	modify this to have type correlated to item
		sprite = Assets.POWERUP[0][type];
		
		width = height = 16;
	}
	
	public void collected(Player p) {
		switch (type) {
			case (NUMBOMBS) :
				p.addNumBombs();
				break;
			case (BOMBSTRENGTH) :
				p.addBombStrength();
				break;
			case (MOVESPEED) :
				p.addMoveSpeed();
				break;
			default:
		}
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(sprite, x * width, y * height, null);
	}
}