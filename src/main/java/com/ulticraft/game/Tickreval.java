package com.ulticraft.game;

public enum Tickreval
{
	SECOND(20),
	HALFSECOND(10),
	QUARTERSECOND(5),
	MINUTE(1200);
	
	Tickreval(int ticks)
	{
		this.ticks = ticks;
	}
	
	private int ticks;

	public int getTicks()
	{
		return ticks;
	}

	public void setTicks(int ticks)
	{
		this.ticks = ticks;
	}
}
