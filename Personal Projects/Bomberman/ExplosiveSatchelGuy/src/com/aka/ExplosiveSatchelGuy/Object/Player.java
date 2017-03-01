package com.aka.ExplosiveSatchelGuy.Object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.aka.ExplosiveSatchelGuy.Manager.Assets;

public class Player extends object {
	
	// sprites
	private BufferedImage[] downSprites;
	private BufferedImage[] leftSprites;
	private BufferedImage[] rightSprites;
	private BufferedImage[] upSprites;
	private BufferedImage[] deathSprites;
	
	// animation
	private final int DOWN = 0;
	private final int LEFT = 1;
	private final int RIGHT = 2;
	private final int UP = 3;
	private final int DEATH = 4;
	
	// powerups
	private int numBombs;
	private int bombStrength;
	private boolean hasKickBomb;
	
	private int droppedBombs;
	
	//  score board
	private int kills;
	
	private boolean remove;
	private boolean dead;
	
	public Player(int X, int Y, int playerNum) {
		super(X, Y);
		
		width = height = 16;
		
		movementSpeed = 1;
		bombStrength = 1;
		numBombs = 1;
		hasKickBomb = false;
		droppedBombs = 0;
		
		remove = false;
		kills = 0;
		
		if (playerNum == 1) {
			//  WIP: set for different player somehow
			downSprites = Assets.PLAYER1[0];
			rightSprites = Assets.PLAYER1[1];
			leftSprites = Assets.PLAYER1[2];
			upSprites = Assets.PLAYER1[3];
			deathSprites = Assets.DEATH[0];
		}
		else if (playerNum == 2) {
			//  WIP: set for different player somehow
			downSprites = Assets.PLAYER2[0];
			rightSprites = Assets.PLAYER2[1];
			leftSprites = Assets.PLAYER2[2];
			upSprites = Assets.PLAYER2[3];
			deathSprites = Assets.DEATH[0];
		}
		
		
		animation.setFrames(downSprites);
		animation.setDelay(10);
	}
	
	private void setAnimation(int i, BufferedImage[] bi, int d) {
		currentAnimation = i;
		animation.setFrames(bi);
		animation.setDelay(d);
	}
	
	//	added
	public int getYDest() {
		return yDest / tileSize;
	}
	
	public int getXDest() {
		return xDest / tileSize;
	}
	
	//  adding powerup
	public void addMoveSpeed() { if (movementSpeed < 2) ++movementSpeed; }
	public void addNumBombs() { ++numBombs; }
	public void addBombStrength() { ++bombStrength; }
	public void addKickBomb() { hasKickBomb = true; }
	
	//  checking power up states
	public int getMoveSpeed() { return movementSpeed; }
	public int getNumBombs() { return numBombs; }
	public int getBombStrength() { return bombStrength; }
	public boolean hasKickBomb() { return hasKickBomb; }
	
	public int droppedBombs() { return droppedBombs; }
	public void addBomb() { ++droppedBombs; }
	public void boomBomb() { --droppedBombs; }
	
	//  used to keep score
	public int getKills() { return kills; }
	public void addKill() { ++kills; }
	
	public void setDestroyed() { dead = true; }
	public void respawn() {
		remove = false;
		dead = false;
		setAnimation(DOWN, downSprites, 10);
		
		movementSpeed = 1;
		numBombs = 1;
		bombStrength = 1;
	}
	public boolean shouldRemove() { return remove; }
	public boolean isDead() { return dead; }
	
	// Keyboard input. Moves the player.
	public void setDown() {
		if (!dead)
		super.setDown();
	}
	public void setLeft() {
		if (!dead)
		super.setLeft();
	}
	public void setRight() {
		if (!dead)
		super.setRight();
	}
	public void setUp() {
		if (!dead)
		super.setUp();
	}
	
	public void setAction() {
	}
	
	public void update() {
		if (!dead){
			// set animation
			if(down) {
				if(currentAnimation != DOWN) {
					setAnimation(DOWN, downSprites, 10);
				}
			}
			else if(left) {
				if(currentAnimation != LEFT) {
					setAnimation(LEFT, leftSprites, 10);
				}
			}
			else if(right) {
				if(currentAnimation != RIGHT) {
					setAnimation(RIGHT, rightSprites, 10);
				}
			}
			else if(up) {
				if(currentAnimation != UP) {
					setAnimation(UP, upSprites, 10);
				}
			}
		}
		else {
			if(currentAnimation != DEATH) 
				setAnimation(DEATH, deathSprites, 8);
			else if (animation.hasPlayed() == 1)
			{
				setAnimation(DOWN, downSprites, 10);
				remove = true;
				dead = false;
			}
		}
		
		// update position
		super.update();
		
	}
	
	// Draw Player.
	public void draw(Graphics2D g) {
		g.drawImage(
				animation.getImage(),
				xPixel,
				yPixel - 8,
				null
				);
	}
}