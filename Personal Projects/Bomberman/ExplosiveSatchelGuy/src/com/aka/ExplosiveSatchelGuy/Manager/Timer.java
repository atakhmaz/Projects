package com.aka.ExplosiveSatchelGuy.Manager;

public class Timer
{
	public static long start;
	public static long time;
	public static long stop;
	
	public static boolean paused;
	
	public Timer(int t)
	{
		paused = false;
		stop = start = System.currentTimeMillis();
		time = t;
	}
	public static long getTime()
	{
		if (time <= 0)
			return 0;
		if (paused)
			return time;
		else
			return time - (System.currentTimeMillis() - start);
	}
	public void pause()
	{
		if (time <= 0)
			return;
		if (paused)
		{
			start = System.currentTimeMillis();
			paused = false;
		}
		else
		{
			stop = System.currentTimeMillis();
			time -= stop - start;
			paused = true;
		}
	}
}