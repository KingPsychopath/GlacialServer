package com.glacialrush.component;

import java.util.Collections;
import org.bukkit.Location;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.component.Controller;
import com.glacialrush.api.dispatch.notification.Notification;
import com.glacialrush.api.dispatch.notification.NotificationPriority;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.thread.GlacialTask;
import com.glacialrush.composite.Faction;
import com.glacialrush.composite.Map;
import com.glacialrush.game.GameHandler;
import com.glacialrush.game.MapHandler;
import com.glacialrush.game.MarketHandler;
import com.glacialrush.game.PlayerHandler;
import net.md_5.bungee.api.ChatColor;

public class GameController extends Controller
{
	protected boolean running;
	protected Map map;
	protected GList<Map> maps;
	protected GList<GameHandler> handlers;
	protected long cycleTime;
	protected long cycles;
	
	protected MapHandler mapHandler;
	protected MarketHandler marketHandler;
	protected PlayerHandler playerHandler;
	
	public GameController(GlacialServer pl)
	{
		super(pl);
		running = false;
		maps = new GList<Map>();
		handlers = new GList<GameHandler>();
		cycleTime = 0;
		cycles = 0;
	}
	
	public void add(GameHandler handler)
	{
		handlers.add(handler);
	}
	
	public void start()
	{
		s("Starting Game");
		
		if(maps.isEmpty())
		{
			f("Game Start: Failed: No maps!");
			return;
		}
		
		Collections.shuffle(maps);
		o("Picking Map");
		map = maps.get(0);
		s("Selected Map: " + map.getName());
		
		for(GameHandler i : handlers)
		{
			i.start();
			v("Started Handler: " + ChatColor.RED + i.getClass().getSimpleName());
		}
		
		v("Setting up map...");
		map.setup();
		
		pl.newThread(new GlacialTask()
		{
			public void run()
			{
				if(map.isAccenting() || map.isBuilding())
				{
					o("Waiting for Map to finish Setting up...");
					setDelay(20);
				}
				
				else
				{
					stop();
					begin();
					return;
				}
			}
		});
	}
	
	public void begin()
	{
		for(GameHandler i : handlers)
		{
			i.begin();
			v("Begin Handler: " + ChatColor.RED + i.getClass().getSimpleName());
		}
		
		running = true;
	}
	
	public void finish()
	{
		for(GameHandler i : handlers)
		{
			i.finish();
			v("Finish Handler: " + ChatColor.RED + i.getClass().getSimpleName());
		}
	}
	
	public void stop()
	{
		running = false;
		
		for(GameHandler i : handlers)
		{
			i.stop();
			v("Stop Handler: " + ChatColor.RED + i.getClass().getSimpleName());
		}
		
		map.accent(Faction.neutral());
		o("Post Accenting Map: " + map.getName());
		
		pl.newThread(new GlacialTask()
		{
			public void run()
			{
				if(map.isAccenting())
				{
					setDelay(20);
					o("Waiting for map to finish accenting...");
				}
				
				else
				{
					stop();
					finish();
					return;
				}
			}
		});
	}
	
	public void preEnable()
	{
		super.preEnable();
		
		o("Starting Progress Thread");
		
		pl.newThread(new GlacialTask()
		{
			@Override
			public void run()
			{
				if(isRunning())
				{
					setDelay(100);
					return;
				}
				
				String prog = "";
				
				for(Map i : maps)
				{
					if(i.isBuilding())
					{
						String build = ChatColor.BLUE + i.getName() + ": " + ChatColor.YELLOW + i.buildProgress() + "%" + " ";
						prog = prog + build;
					}
					
					if(i.isAccenting())
					{
						String accent = ChatColor.GREEN + i.getName() + ": " + ChatColor.YELLOW + i.accentProgress() + "%" + " ";
						prog = prog + accent;
					}
				}
				
				if(!prog.equals(""))
				{
					Notification n = new Notification().setSubSubTitle(prog).setDelay(false).setPriority(NotificationPriority.LOWEST);
					
					((GlacialServer) pl).getNotificationController().dispatch(n);
				}
			}
		});
	}
	
	public void postEnable()
	{
		super.postEnable();
		
		o("Registering Handlers...");
		
		mapHandler = new MapHandler((GlacialServer) pl);
		marketHandler = new MarketHandler((GlacialServer) pl);
		playerHandler = new PlayerHandler((GlacialServer) pl);
		
		pl.newThread(new GlacialTask()
		{
			@Override
			public void run()
			{
				if(isRunning())
				{
					long msa = System.currentTimeMillis();
					
					for(GameHandler i : handlers)
					{
						long msx = System.currentTimeMillis();
						i.start();
						i.setCycleTime(i.getCycleTime() + (System.currentTimeMillis() - msx));
						i.setCycles(i.getCycles() + 1);
					}
					
					cycleTime += (System.currentTimeMillis() - msa);
					cycles++;
				}
			}
		});
		
		start();
	}
	
	public void preDisable()
	{
		super.preDisable();
	}
	
	public void postDisable()
	{
		super.postDisable();
	}
	
	public Map getMap(String map)
	{
		for(Map i : maps)
		{
			if(i.getName().equals(map))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public Map getMap(Location map)
	{
		for(Map i : maps)
		{
			if(i.contains(map))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public Map findMap(String map)
	{
		for(Map i : maps)
		{
			if(i.getName().toLowerCase().contains(map.toLowerCase()))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public void setRunning(boolean running)
	{
		this.running = running;
	}
	
	public Map getMap()
	{
		return map;
	}
	
	public void setMap(Map map)
	{
		this.map = map;
	}
	
	public GList<Map> getMaps()
	{
		return maps;
	}
	
	public void setMaps(GList<Map> maps)
	{
		this.maps = maps;
	}
	
	public GList<GameHandler> getHandlers()
	{
		return handlers;
	}
	
	public void setHandlers(GList<GameHandler> handlers)
	{
		this.handlers = handlers;
	}
	
	public long getCycleTime()
	{
		return cycleTime;
	}
	
	public void setCycleTime(long cycleTime)
	{
		this.cycleTime = cycleTime;
	}
	
	public long getCycles()
	{
		return cycles;
	}
	
	public void setCycles(long cycles)
	{
		this.cycles = cycles;
	}
	
	public MapHandler getMapHandler()
	{
		return mapHandler;
	}
	
	public MarketHandler getMarketHandler()
	{
		return marketHandler;
	}
	
	public PlayerHandler getPlayerHandler()
	{
		return playerHandler;
	}
}
