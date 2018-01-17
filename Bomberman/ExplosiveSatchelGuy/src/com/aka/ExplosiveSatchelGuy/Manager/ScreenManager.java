package com.aka.ExplosiveSatchelGuy.Manager;

import java.awt.Graphics2D;

import com.aka.ExplosiveSatchelGuy.Screen.*;

public class ScreenManager
{
	//  Array of all our screens that the game can have
	private Screen[] gameScreens;
	private int currentScreen;
	private int previousScreen;

	//  Number of screens
	public static final int NUMBER_OF_SCREENS = 4;
	
	//  Different screen types
	public static final int INTRO = 0;
	public static final int MENU = 1;
	public static final int PLAY = 2;
	public static final int GAMEOVER = 3;
	
	// Pause state variables
	private boolean paused;
	private PauseScreen pauseState;

	public ScreenManager()
	{
		Sound.init();
		
		paused = false;
		pauseState = new PauseScreen(this);
		
		currentScreen = INTRO;
		gameScreens = new Screen[NUMBER_OF_SCREENS];
		setScreen(currentScreen);
	}
	public void setScreen(int i)
	{
		previousScreen = currentScreen;
		unloadScreen(previousScreen);
		currentScreen = i;

		switch(currentScreen)
		{
		case(INTRO):
		{
			gameScreens[i] = new IntroScreen(this);
			gameScreens[i].init();
			break;
		}
		case(MENU):
		{
			gameScreens[i] = new MenuScreen(this);
			gameScreens[i].init();
			break;
		}
		case (PLAY):
		{
			gameScreens[i] = new PlayScreen(this);
			gameScreens[i].init();
			break;
		}
		case (GAMEOVER):
		{
			gameScreens[i] = new GameOverScreen(this);
			gameScreens[i].init();
			break;
		}
		default:
		}
	}
	public void unloadScreen(int i)
	{
		gameScreens[i] = null;
	}
	
	public void setPaused(boolean b)
	{
		if(b == true)
			pauseState.init();
		paused = b;
	}
	
	public void update()
	{
		if(paused)
		{
			pauseState.update();
		}
		else if(gameScreens[currentScreen] != null)
		{
			gameScreens[currentScreen].update();
		}
	}
	
	public void draw(Graphics2D g)
	{
		if(paused)
		{
			pauseState.draw(g);
		}
		else if(gameScreens[currentScreen] != null)
		{
			gameScreens[currentScreen].draw(g);
		}
	}
}
