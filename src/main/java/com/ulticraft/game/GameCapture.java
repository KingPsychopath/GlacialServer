package com.ulticraft.game;

import java.util.Random;
import org.bukkit.entity.Player;
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
	public void onStart(Game m, GameData g)
	{
		UList<Map> maps = pl.getState().getMaps();
		Random r = new Random();
		Map selected = maps.get(r.nextInt(maps.size()));
		m.getGameData().setMap(selected);
		pl.getFactionComponent().rebalance();
		
		for(Player i : pl.onlinePlayers())
		{
			selected.deploy(i);
		}
	}

	@Override
	public void onStop(Game m, GameData g)
	{
		
	}

	@Override
	public void onTick(Game m, GameData g)
	{
		if(tick == 20)
		{
			g.getMap().tick();
			
			if(g.getMap().getCallback() != null)
			{
				m.stop();
				m.start();
			}
			
			tick = 0;
		}
		
		tick++;
	}
}
