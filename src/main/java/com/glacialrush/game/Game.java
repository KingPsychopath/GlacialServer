package com.glacialrush.game;

import java.util.Collections;
import java.util.Iterator;
import com.glacialrush.GlacialServer;
import com.glacialrush.composite.Hunk;
import com.glacialrush.composite.Map;
import com.glacialrush.game.GameState.Status;
import com.glacialrush.game.component.EventRippler;
import com.glacialrush.game.component.MapHandler;
import com.glacialrush.xapi.UList;

public class Game
{
	private GameState state;
	private GameRegistry registry;
	private GlacialServer pl;
	private UList<Map> maps;
	private Integer gameTask;
	
	private EventRippler eventRippler;
	private MapHandler mapHandler;
	
	public Game(GlacialServer pl)
	{
		this.pl = pl;
		this.state = new GameState(pl, null);
		this.registry = new GameRegistry(pl);
		this.maps = new UList<Map>();
		
		this.eventRippler = new EventRippler();
		this.mapHandler = new MapHandler();
		
		registry.add(eventRippler);
		registry.add(mapHandler);
	}
	
	public void load()
	{
		maps = pl.getDataComponent().loadAll();
		
		for(Map i : maps)
		{
			i.build();
		}
	}
	
	public void save()
	{
		pl.getDataComponent().saveAll(maps);
	}
	
	public void stopGame()
	{
		pl.cancelTask(gameTask);
		registry.stop();
		state.stop();
	}
	
	public void startGame()
	{
		UList<Map> mMaps = getMapsReady();
		
		if(mMaps.isEmpty())
		{
			return;
		}
		
		Collections.shuffle(mMaps);
		state = new GameState(pl, mMaps.get(0));
		state.start();
		registry.start();
		
		gameTask = pl.scheduleSyncRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				if(state.getStatus().equals(Status.RUNNING))
				{
					registry.tick();
				}
			}
		});
	}
	
	public Map findMap(String name)
	{
		for(Map i : maps)
		{
			if(i.getName().equalsIgnoreCase(name))
			{
				return i;
			}
		}
		
		for(Map i : maps)
		{
			if(i.getName().toLowerCase().contains(name.toLowerCase()))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public Map getMap(String name)
	{
		for(Map i : maps)
		{
			if(i.getName().equalsIgnoreCase(name))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public Map getMap(Hunk hunk)
	{
		for(Map i : maps)
		{
			if(i.getWorld().equals(hunk.getWorld()))
			{
				if(i.getRegion(hunk) != null)
				{
					return i;
				}
			}
		}
		
		return null;
	}
	
	public UList<Map> getMapsBuilding()
	{
		UList<Map> mapps = maps.copy();
		Iterator<Map> it = maps.iterator();
		
		while(it.hasNext())
		{
			Map i = it.next();
			
			if(!i.isBuilding())
			{
				it.remove();
			}
		}
		
		return mapps;
	}
	
	public UList<Map> getMapsReady()
	{
		UList<Map> mapps = maps.copy();
		Iterator<Map> it = maps.iterator();
		
		while(it.hasNext())
		{
			Map i = it.next();
			
			if(!i.getReady())
			{
				it.remove();
			}
		}
		
		return mapps;
	}
	
	public UList<Map> getMapsBuilt()
	{
		UList<Map> mapps = maps.copy();
		Iterator<Map> it = maps.iterator();
		
		while(it.hasNext())
		{
			Map i = it.next();
			
			if(!i.isBuilt())
			{
				it.remove();
			}
		}
		
		return mapps;
	}
	
	public UList<Map> getMapsUnbuilt()
	{
		UList<Map> mapps = maps.copy();
		Iterator<Map> it = maps.iterator();
		
		while(it.hasNext())
		{
			Map i = it.next();
			
			if(!i.isBuilt() && !i.isBuilding())
			{
				it.remove();
			}
		}
		
		return mapps;
	}

	public GameState getState()
	{
		return state;
	}

	public GameRegistry getRegistry()
	{
		return registry;
	}

	public UList<Map> getMaps()
	{
		return maps;
	}

	public void setMaps(UList<Map> maps)
	{
		this.maps = maps;
	}
	
	public GlacialServer pl()
	{
		return pl;
	}

	public GlacialServer getPl()
	{
		return pl;
	}

	public Integer getGameTask()
	{
		return gameTask;
	}

	public EventRippler getEventRippler()
	{
		return eventRippler;
	}

	public MapHandler getMapHandler()
	{
		return mapHandler;
	}
}
