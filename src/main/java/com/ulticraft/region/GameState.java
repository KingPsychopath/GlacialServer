package com.ulticraft.region;

import com.ulticraft.GlacialRush;
import com.ulticraft.data.Faction;
import com.ulticraft.uapi.UList;

public class GameState
{
	private UList<Map> maps;
	private UList<Faction> factions;
	private GlacialRush pl;
	private int task;
	private int gameClock;
	
	public GameState(GlacialRush pl)
	{
		maps = new UList<Map>();
		factions = new UList<Faction>();
		gameClock = 1;
		
		this.pl = pl;
	}
	
	public void start()
	{
		task = pl.scheduleSyncRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				gameClock++;
				
				if(gameClock == 20)
				{
					gameClock = 1;
				}
			}
		});
	}
	
	public void stop()
	{
		pl.cancelTask(task);
	}
}
