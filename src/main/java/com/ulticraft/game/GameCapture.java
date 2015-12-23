package com.ulticraft.game;

import java.util.Random;
import com.ulticraft.GlacialRush;
import com.ulticraft.map.Map;
import com.ulticraft.uapi.UList;

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
		UList<Map> maps = pl.getState().getMaps();
		Random r = new Random();
		Map selected = maps.get(r.nextInt(maps.size()));
		
		pl.getFactionComponent().rebalance();
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
			tick = 0;
		}
		
		tick++;
	}
}
