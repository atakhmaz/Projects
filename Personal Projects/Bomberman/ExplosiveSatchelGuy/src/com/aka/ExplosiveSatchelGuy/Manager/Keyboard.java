package com.aka.ExplosiveSatchelGuy.Manager;

import java.awt.event.KeyEvent;

public class Keyboard
{
	//  number of keys
	public static final int NUM_KEYS = 13;

	public static boolean keyPress[] = new boolean[NUM_KEYS];
	public static boolean prevKeyPress[] = new boolean[NUM_KEYS];

	public static int ENTER  = 0;
	public static int ESCAPE = 1;
	public static int SPACE = 2;
	
	public static int UP = 3;
	public static int DOWN = 4;
	public static int LEFT = 5;
	public static int RIGHT = 6;
	
	public static int P1UP = 8;
	public static int P1DOWN = 9;
	public static int P1LEFT = 10;
	public static int P1RIGHT = 11;
	public static int P1BOMB = 2;
	
	public static int P2UP = 3;
	public static int P2DOWN = 4;
	public static int P2LEFT = 5;
	public static int P2RIGHT = 6;
	public static int P2BOMB = 7;
	
	
	//	setting key state to true or false (whether it is pressed or not)
	public static void keySet(int i, boolean b)
	{
		switch(i)
		{
		case (KeyEvent.VK_UP):
		{
			keyPress[UP] = b;
			keyPress[P2UP] = b;
			break;
		}
		case (KeyEvent.VK_DOWN):
		{
			keyPress[DOWN] = b;
			keyPress[P2DOWN] = b;
			break;
		}
		case (KeyEvent.VK_LEFT):
		{
			keyPress[LEFT] = b;
			keyPress[P2LEFT] = b;
			break;
		}
		case (KeyEvent.VK_RIGHT):
		{
			keyPress[RIGHT] = b;
			keyPress[P2RIGHT] = b;
			break;
		}
		case (KeyEvent.VK_NUMPAD0):
		{
			keyPress[P2BOMB] = b;
			break;
		}
		case (KeyEvent.VK_SPACE):
		{
			keyPress[SPACE] = b;
			keyPress[P1BOMB] = b;
			break;
		}
		case (KeyEvent.VK_ENTER):
		{
			keyPress[ENTER] = b;
			break;
		}
		case (KeyEvent.VK_ESCAPE):
		{
			keyPress[ESCAPE] = b;
			break;
		}
		case (KeyEvent.VK_W):
		{
			keyPress[P1UP] = b;
			break;
		}
		case (KeyEvent.VK_S):
		{
			keyPress[P1DOWN] = b;
			break;
		}
		case (KeyEvent.VK_A):
		{
			keyPress[P1LEFT] = b;
			break;
		}
		case (KeyEvent.VK_D):
		{
			keyPress[P1RIGHT] = b;
			break;
		}
		}
	}
	//	updates previous key states
	public static void update()
	{
		for(int i = 0; i < NUM_KEYS; i++)
		{
			prevKeyPress[i] = keyPress[i];
		}
	}
	//	checks to see if the key was just pressed and not held down
	public static boolean isPressed(int i)
	{
		return keyPress[i] && !prevKeyPress[i];
	}
	//	checks to see if the key is pressed or held down
	public static boolean isDown(int i)
	{
		return keyPress[i];
	}
	//	checks to see if any key is pressed or held down	
	public static boolean anyKeyDown()
	{
		for(int i = 0; i < NUM_KEYS; i++)
		{
			if(keyPress[i]) return true;
		}
		return false;
	}
	//	checks to see if any key was just pressed and not held down
	public static boolean anyKeyPress()
	{
		for(int i = 0; i < NUM_KEYS; i++)
		{
			if(keyPress[i] && !prevKeyPress[i]) return true;
		}
		return false;
	}
}