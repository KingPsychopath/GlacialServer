package com.ulticraft.game;

import com.ulticraft.GlacialRush;

public class GameCapture implements GameRegistrant
{
	protected GlacialRush pl;
	protected Integer tick;
	
	public GameCapture(GlacialRush pl)
	{
		this.pl = pl;
		this.tick = 0;
	}
	
	@Override
	public void onStart(GameData g)
	{
		
	}

	@Override
	public void onStop(GameData g)
	{
		
	}

	@Override
	public void onTick(GameData g)
	{
		if(tick == 20)
		{
			g.getMap().tick();
		}
		
		tick++;
	}
}
