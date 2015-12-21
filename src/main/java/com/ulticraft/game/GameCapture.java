package com.ulticraft.game;

import com.ulticraft.GlacialRush;

public class GameCapture implements GameRegistrant
{
	protected GlacialRush pl;
	protected Boolean halfTick;
	
	public GameCapture(GlacialRush pl)
	{
		this.pl = pl;
		this.halfTick = false;
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
		if(halfTick)
		{
			g.getMap().tick();
		}
		
		halfTick = !halfTick;
	}
}
