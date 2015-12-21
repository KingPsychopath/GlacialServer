package com.ulticraft.game;

import com.ulticraft.GlacialRush;
import com.ulticraft.map.DeployableMap;
import com.ulticraft.uapi.UList;

public class Game
{
	protected GameData gameData;
	protected GlacialRush pl;
	protected int task;
	protected long startMillis;
	protected UList<GameRegistrant> registrants;
	
	public Game(GlacialRush pl, DeployableMap map)
	{
		this.pl = pl;
		
		gameData = new GameData(map, 0);
		
		registrants = new UList<GameRegistrant>();
	}
	
	public void start()
	{
		startMillis = System.currentTimeMillis();
		gameData.setMillis(0);
		
		eventStart();
		
		task = pl.scheduleSyncRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				gameData.setMillis(System.currentTimeMillis() - startMillis);
				eventTick();
			}
		});
	}
	
	public void stop()
	{
		eventStop();
		pl.cancelTask(task);
	}
	
	public void register(GameRegistrant gameRegistrant)
	{
		registrants.add(gameRegistrant);
	}
	
	public void eventStart()
	{
		for(GameRegistrant i : registrants)
		{
			i.onStart(gameData);
		}
	}
	
	public void eventStop()
	{
		for(GameRegistrant i : registrants)
		{
			i.onStop(gameData);
		}
	}
	
	public void eventTick()
	{
		for(GameRegistrant i : registrants)
		{
			i.onTick(gameData);
		}
	}
}
