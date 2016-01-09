package com.glacialrush.game;

import java.util.Collections;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.object.GList;
import com.glacialrush.composite.Hunk;
import com.glacialrush.composite.Map;
import com.glacialrush.composite.Region;
import com.glacialrush.game.GameState.Status;
import com.glacialrush.game.component.EventRippler;
import com.glacialrush.game.component.MapHandler;
import com.glacialrush.game.component.PlayerHandler;
import com.glacialrush.xapi.FastMath;

public class Game
{
	private GameState state;
	private GameRegistry registry;
	private GlacialServer pl;
	private GList<Map> maps;
	private Integer gameTask;
	
	private EventRippler eventRippler;
	private MapHandler mapHandler;
	private PlayerHandler playerHandler;
	private Boolean running;
	private Integer loadTask;
	
	public Game(GlacialServer pl)
	{
		this.pl = pl;
		this.state = new GameState(pl, null);
		this.registry = new GameRegistry(pl);
		this.maps = new GList<Map>();
		
		this.eventRippler = new EventRippler();
		this.mapHandler = new MapHandler();
		this.playerHandler = new PlayerHandler();
		this.running = false;
		this.loadTask = 0;
		
		registry.add(eventRippler);
		registry.add(mapHandler);
		registry.add(playerHandler);
	}
	
	public void load()
	{
		maps = pl.getDataComponent().loadAll();
		
		for(Map i : maps)
		{
			for(Region j : i.getRegions())
			{
				j.accentTask();
			}
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
		
		running = false;
	}
	
	public void startGame()
	{
		GList<Map> mMaps = getMapsReady();
		
		if(mMaps.isEmpty())
		{
			return;
		}
		
		Collections.shuffle(mMaps);
		state = new GameState(pl, mMaps.get(0));
		state.start();
		final Map map = state.getMap();
		map.build();
		
		loadTask = pl.scheduleSyncRepeatingTask(30, 0, new Runnable()
		{
			@Override
			public void run()
			{
				if(!state.getMap().isBuilding())
				{
					pl.cancelTask(loadTask);
					map.accentEvenley();
					
					for(Player i : pl.onlinePlayers())
					{
						respawn(i);
					}
					
					registry.start();
					
					for(Player i : pl.onlinePlayers())
					{
						i.removePotionEffect(PotionEffectType.BLINDNESS);
						i.removePotionEffect(PotionEffectType.SLOW);
						i.removePotionEffect(PotionEffectType.SLOW_DIGGING);
					}
					
					gameTask = pl.scheduleSyncRepeatingTask(60, 0, new Runnable()
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
					
					running = true;
				}
			}
		});
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public Location getRespawn(Player p)
	{
		return getState().getMap().getSpawn(getState().getFactionMap().getFaction(p));
	}
	
	public void respawn(Player p)
	{
		p.teleport(getRespawn(p));
	}
	
	public Location getRespawnNear(Player p, Location l)
	{
		double close = Double.MAX_VALUE;
		Region sp = null;
		
		for(Region i : state.getMap().getRegions())
		{
			if(i.getFaction().equals(state.getFactionMap().getFaction(p)))
			{
				double dst = FastMath.distance2D(i.getSpawn(), l);
				
				if(dst < close)
				{
					dst = close;
					sp = i;
				}
			}
		}
		
		return sp.getSpawn();
	}
	
	public void respawnNear(Player p, Location l)
	{
		p.teleport(getRespawnNear(p, l));
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
	
	public GList<Map> getMapsBuilding()
	{
		GList<Map> mapps = maps.copy();
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
	
	public GList<Map> getMapsReady()
	{
		GList<Map> mapps = maps.copy();
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
	
	public GList<Map> getMapsBuilt()
	{
		GList<Map> mapps = maps.copy();
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
	
	public GList<Map> getMapsUnbuilt()
	{
		GList<Map> mapps = maps.copy();
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

	public GList<Map> getMaps()
	{
		return maps;
	}

	public void setMaps(GList<Map> maps)
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

	public PlayerHandler getPlayerHandler()
	{
		return playerHandler;
	}
}
