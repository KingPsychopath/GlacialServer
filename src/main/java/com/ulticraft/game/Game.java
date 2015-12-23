package com.ulticraft.game;

import com.ulticraft.GlacialRush;
import com.ulticraft.map.Map;
import com.ulticraft.uapi.UList;

public class Game
{
	protected GameData gameData;
	protected GlacialRush pl;
	protected int task;
	protected long startMillis;
	protected UList<GameRegistrant> registrants;
	
	public Game(GlacialRush pl, Map map)
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
			i.onStart(this, gameData);
		}
	}
	
	public void eventStop()
	{
		for(GameRegistrant i : registrants)
		{
			i.onStop(this, gameData);
		}
	}
	
	public void eventTick()
	{
		for(GameRegistrant i : registrants)
		{
			i.onTick(this, gameData);
		}
	}
	
	public GameData getGameData()
	{
		return gameData;
	}
	
	public void setGameData(GameData gameData)
	{
		this.gameData = gameData;
	}
	
	public GlacialRush getPl()
	{
		return pl;
	}
	
	public void setPl(GlacialRush pl)
	{
		this.pl = pl;
	}
	
	public int getTask()
	{
		return task;
	}
	
	public void setTask(int task)
	{
		this.task = task;
	}
	
	public long getStartMillis()
	{
		return startMillis;
	}
	
	public void setStartMillis(long startMillis)
	{
		this.startMillis = startMillis;
	}
	
	public UList<GameRegistrant> getRegistrants()
	{
		return registrants;
	}
	
	public void setRegistrants(UList<GameRegistrant> registrants)
	{
		this.registrants = registrants;
	}
}
