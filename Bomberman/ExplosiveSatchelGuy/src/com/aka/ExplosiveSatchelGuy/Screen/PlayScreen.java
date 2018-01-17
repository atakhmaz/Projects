package com.aka.ExplosiveSatchelGuy.Screen;

//  necessary for drawing
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
//	necessary for random
import java.util.Random;

import com.aka.ExplosiveSatchelGuy.HUD.Hud;
import com.aka.ExplosiveSatchelGuy.Manager.Assets;
import com.aka.ExplosiveSatchelGuy.Manager.Keyboard;
import com.aka.ExplosiveSatchelGuy.Manager.ScreenManager;
import com.aka.ExplosiveSatchelGuy.Manager.Sound;
import com.aka.ExplosiveSatchelGuy.Manager.Timer;
import com.aka.ExplosiveSatchelGuy.Object.Bomb;
import com.aka.ExplosiveSatchelGuy.Object.Fire;
import com.aka.ExplosiveSatchelGuy.Object.Player;
import com.aka.ExplosiveSatchelGuy.Object.PowerUp;
import com.aka.ExplosiveSatchelGuy.Object.Wall;

public class PlayScreen extends Screen
{
    private int numPlayers;
    
    public static Player[] players;

	public static ArrayList<Wall> walls;
	private static ArrayList<PowerUp> powerups;
	//added
	public static ArrayList<Bomb> bombs;
	private static ArrayList<Fire> fires;
    
	private static BufferedImage map;
	
	private static Hud hud;
	
	public static Timer timer;
	
	private Random rand;
	
	private boolean respawn;
	
	private boolean blink;
	private int blinks;
	
	//	percent of map filled with breakable walls
	double percentWalls = 0.40;
	//	percent of powerup drops in walls
	double percentPowerups = 30;
	
	public static int row, col;
	private int offset;

	public PlayScreen(ScreenManager sm)
	{
		super(sm);
	}

	public void init()
	{
		timer = new Timer(300000);
		
		//	gets row and cols of map
		row = 13;
		col = 15;
		//	offset of row border
		offset = 1;
		
		numPlayers = 2;
		players = new Player[numPlayers];
		players[0] = new Player(offset, offset, 1);
		players[1] = new Player(col - 2, row - 2, 2);
		
		Sound.load("/Sound/bombl.wav", "LargeBomb");
		Sound.load("/Sound/bombm.wav", "MediumBomb");
		Sound.load("/Sound/bombs.wav", "SmallBomb");
		
		Sound.load("/Sound/death.wav", "Death");
		Sound.setVolume("Death", -20);
		
		Sound.load("/Sound/spawnwalls.wav", "SpawnWalls");
		Sound.load("/Sound/clapping.wav", "Clapping");
		
		map = Assets.MAP[0][0];
		hud = new Hud();
		Sound.load("/Sound/battlemusic.wav", "music1");
		Sound.setVolume("music1", -10);
		Sound.loop("music1", 1000, 1000, Sound.getFrames("music1") - 1000);
		
		rand = new Random();
		
		//	set up array lists
		walls = new ArrayList<>();
		powerups = new ArrayList<>();
		//added
		bombs = new ArrayList<>();
		fires = new ArrayList<>();
		
		//	create breakable walls
		setupWalls();
		//	create powerups
		setupPowerUps();
	}

	@SuppressWarnings("static-access")
	public void update()
	{
		if (blinks > 0) {
			if (blinks % 4 == 0)
				blink = !blink;
			
			if (++blinks > 23) {
				blinks = 0;
				blink = false;
			}
			return;
		}
		
		if (walls.isEmpty())
		{
			Sound.play("SpawnWalls");
			setupWalls();
			setupPowerUps();
			
			blink = true;
			blinks++;
			return;
		}
		
		handleInput();
		
		//	update fire check if needed to be deleted
		//	update if fire destroyed anything
		for (int i = fires.size() - 1; i >= 0; i--) {
			Fire f = fires.get(i);
			f.update();
			
			//	check for player collision
			for (Player p : players) {
				if (f.collides(p))
				{
					p.setDestroyed();
				}
			}
			
			for (Wall w: walls) {
				if (f.collides(w))
					w.setDestroyed();
			}
			
			if (f.shouldRemove()) {
				fires.remove(i);
			}
		}

		//	update animations and position of players
		for (int i = 0; i < numPlayers; ++i) {
			Player p = players[i];
			if (p.shouldRemove())
			{
				Sound.play("Death");
				players[(i+1)%2].addKill();
				p.respawn();
				if (respawn)
					p.setPosition(offset, offset);
				else
					p.setPosition(col - 2, row - 2);
				
				respawn = !respawn;
			}
			p.update();
			if(p.getKills() >= 10 || timer.getTime() <= 0)
			{
				Sound.stop("music1");
				Sound.play("confirm");
				timer.pause();
				Sound.loop("Clapping");
				sm.setScreen(ScreenManager.GAMEOVER);
			}
		}
		
		
		//	update power ups
		for (int i = powerups.size() - 1; i >= 0; --i) {
			PowerUp pu = powerups.get(i);
			pu.update();
			
			//	checks collision for power ups
			for (Player p : players)
				if (p.collides(pu)) {
					pu.collected(p);
					powerups.remove(i);
					break;
				}
		}
		
		for (int i = bombs.size() - 1; i >= 0; --i) {
			Bomb b = bombs.get(i);
			
			b.update();
			if (b.shouldRemove()) {
				bombs.remove(i);
				players[b.getID()].boomBomb();
				
				if(b.getStrength() == 1)
					Sound.play("SmallBomb");
				else if(b.getStrength() == 2)
					Sound.play("MediumBomb");
				else if(b.getStrength() >= 3)
					Sound.play("LargeBomb");
					
				fires.add(new Fire(b.getX(), b.getY(), b.getStrength()));
			}
 		}
		
		//	update walls
		for (int i = walls.size() - 1; i >= 0; --i) {
			Wall w = walls.get(i);
			
			w.update();
			
			//	checks if needed to be deleted
			if (w.shouldRemove()) {
				walls.remove(i);
			}
		}
	}
	
	public void handleInput()
	{
		if(Keyboard.isPressed(Keyboard.ESCAPE))
		{
			Sound.play("confirm");
			timer.pause();
			Sound.stop("music1");
			sm.setPaused(true);
		}

		//	hardcode: is there a better way?
		//	player one movement and bomb
		if (!players[0].isDead()) {
			if(Keyboard.isDown(Keyboard.P1LEFT)) players[0].setLeft();
			if(Keyboard.isDown(Keyboard.P1RIGHT)) players[0].setRight();
			if(Keyboard.isDown(Keyboard.P1UP)) players[0].setUp();
			if(Keyboard.isDown(Keyboard.P1DOWN)) players[0].setDown();
			if(Keyboard.isPressed(Keyboard.P1BOMB)) { 
				if (players[0].droppedBombs() < players[0].getNumBombs()) {
					bombs.add(new Bomb(players[0].getXDest(), players[0].getYDest(), players[0].getBombStrength(), 0));
					players[0].addBomb();
				}
			}
		}
		

		//	player two movement and bomb
		if (!players[1].isDead()) {
			if(Keyboard.isDown(Keyboard.P2LEFT)) players[1].setLeft();
			if(Keyboard.isDown(Keyboard.P2RIGHT)) players[1].setRight();
			if(Keyboard.isDown(Keyboard.P2UP)) players[1].setUp();
			if(Keyboard.isDown(Keyboard.P2DOWN)) players[1].setDown();
			if(Keyboard.isPressed(Keyboard.P2BOMB)) {
				if (players[1].droppedBombs() < players[1].getNumBombs()) {
					bombs.add(new Bomb(players[1].getXDest(), players[1].getYDest(), players[1].getBombStrength(), 1));
					players[1].addBomb();
				}
			}
		}
	}
	
	public void setupWalls() {
		//	estimation of free space for walls
		int freeSpaceEstimate = (int) (percentWalls * ((row * col) - ((row / 2) * (col / 2))));
		
		for (int x = 1; x < col - 1; ++x) {
			for (int y = 1; y < row - 1; ++y) {
				if (!(onPlayer(x, y) || onWall(x, y) || onCorner(x, y))) {
					int spawn = rand.nextInt(100);
					if (spawn < freeSpaceEstimate)
						walls.add(new Wall(x, y));
				}
			}
		}
	}
	
	//	if in corner
	public boolean onCorner(int x, int y) {
		if (x < 3 || x > col - 4)
			if (y < 3 || y > row - 4)
				return true;
		
		return false;
	}

	//	if too close to player
	public boolean onPlayer(int x, int y) {
		for (Player p : players) {
			if (x > p.getX() - 2 && x < p.getX() + 2)
				if (y > p.getY() - 2 && y < p.getY() + 2)
					return true;
		}
		
		return false;
	}
	
	public static boolean onWall(int x, int y) {
		if (x == 0 || x == col - 1)
			return true;
			
		if (y == 0 || y == row - 1)
			return true;
		
		//	if on the breakable wall
		if (x % 2 == 0 && y % 2 == 0)
			return true;
		
		return false;
	}
	
	public void setupPowerUps() {
		//	for each wall, see if it has a power up underneath
		for (Wall w : walls) {
			//	adds a powerup if roll is good
			int puAppear = rand.nextInt(100);
			if (puAppear < percentPowerups) {
				powerups.add(new PowerUp(w.getX(), w.getY()));
			}
		}
	}

	public void draw(Graphics2D g)
	{
		//	draws map and hud
		g.drawImage(map, 0, 0, null);
		hud.draw(g);
			
		if(!blink)
		//	drawing all power ups
		for (PowerUp pu : powerups)
			pu.draw(g);
			
		if(!blink)
		//	drawing all walls
		for (Wall w : walls)
			w.draw(g);
			
		//	drawing all bombs
		for (Bomb b : bombs)
			b.draw(g);
			
		//	drawing all fires
		for (Fire f : fires)
			f.draw(g);
		
		//	drawing all players
		for(Player p : players)
			if (!p.shouldRemove())
				p.draw(g);
	}
	
	public static void pauseDraw(Graphics2D g)
	{
		//	draws map and hud
		g.drawImage(map, 0, 0, null);
		hud.draw(g);
		
		//	drawing all power ups
		for (PowerUp pu : powerups)
			pu.draw(g);
			
		//	drawing all walls
		for (Wall w : walls)
			w.draw(g);
			
		//	drawing all bombs
		for (Bomb b : bombs)
			b.draw(g);
			
		//	drawing all fires
		for (Fire f : fires)
			f.draw(g);
		
		//	drawing all players
		for(Player p : players)
			if (!p.shouldRemove())
				p.draw(g);
	}
}
