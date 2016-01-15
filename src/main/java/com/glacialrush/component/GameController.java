package com.glacialrush.component;

import java.util.Collections;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.glacialrush.GlacialAPI;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.component.Controller;
import com.glacialrush.api.component.ThreadComponent;
import com.glacialrush.api.dispatch.notification.Notification;
import com.glacialrush.api.dispatch.notification.NotificationPriority;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.thread.GlacialTask;
import com.glacialrush.composite.Capture;
import com.glacialrush.composite.Faction;
import com.glacialrush.composite.Map;
import com.glacialrush.composite.Region;
import com.glacialrush.game.GameHandler;
import com.glacialrush.game.GlacialHandler;
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
	protected ThreadComponent t;
	protected long cycleTime;
	protected long cycles;
	protected boolean stopping;
	protected boolean starting;
	protected boolean restarting;
	
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
		starting = false;
		stopping = false;
		restarting = false;
		t = new ThreadComponent(pl);
	}
	
	public void add(GlacialHandler handler)
	{
		handlers.add(handler);
		pl.register(handler);
		o("Registered Listener for Handler: " + handler.getName());
	}
	
	public void restart()
	{
		final int[] c = new int[] {0};
		
		t.addTask(new GlacialTask("Restart Monitor")
		{
			@Override
			public void run()
			{
				c[0]++;
				setDelay(20);
				
				if(c[0] == 5)
				{
					GlacialAPI.instance().reloadGrush();
					stop();
					return;
				}
			}
		});
	}
	
	public void start()
	{
		if(starting || running || stopping)
		{
			return;
		}
		
		t.preEnable();
		t.postEnable();
		
		for(Player i : pl.onlinePlayers())
		{
			((GlacialServer) pl).getPlayerController().disable(i);
		}
		
		starting = true;
		
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
		
		((GlacialServer) pl).getNotificationController().dispatch(new Notification().setTitle(ChatColor.AQUA + "Starting Game").setSubTitle(ChatColor.AQUA + "Map: " + ChatColor.GREEN + map.getName()));
		
		for(GameHandler i : handlers)
		{
			i.start();
			v("Started Handler: " + ChatColor.RED + i.getClass().getSimpleName());
		}
		
		v("Setting up map...");
		map.setup();
		
		t.addTask(new GlacialTask("Game Start Callback")
		{
			public void run()
			{
				if(map.isAccenting() || map.isBuilding())
				{
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
	
	public boolean isStopping()
	{
		return stopping;
	}
	
	public boolean isStarting()
	{
		return starting;
	}
	
	public boolean isRestarting()
	{
		return restarting;
	}
	
	public void begin()
	{
		for(GameHandler i : handlers)
		{
			i.begin();
			v("Begin Handler: " + ChatColor.RED + i.getClass().getSimpleName());
		}
		
		running = true;
		starting = false;
		restarting = false;
		
		for(Player i : pl.onlinePlayers())
		{
			((GlacialServer) pl).getPlayerController().enable(i);
		}
		
		s("GAME IS RUNNING");
		
		o("Balancing Players");
		playerHandler.rebalance();
		o("Respawning Players");
		playerHandler.respawn();
		
		t.addTask(new GlacialTask("GameHandle Controller")
		{
			@Override
			public void run()
			{
				for(GameHandler i : handlers)
				{
					i.tick();
				}
			}
		});
	}
	
	public void finish()
	{
		for(GameHandler i : handlers)
		{
			i.finish();
			v("Finish Handler: " + ChatColor.RED + i.getClass().getSimpleName());
		}
		
		stopping = false;
	}
	
	public void stop()
	{
		if(starting || stopping)
		{
			return;
		}
		
		running = false;
		stopping = true;
		
		for(GameHandler i : handlers)
		{
			i.stop();
			v("Stop Handler: " + ChatColor.RED + i.getClass().getSimpleName());
		}
		
		map.accent(Faction.neutral());
		
		for(Region i : map.getRegions())
		{
			i.resetTimer();
			i.setFaction(Faction.neutral());
			
			for(Capture j : i.getCaptures())
			{
				j.reset();
			}
		}
		
		o("Post Accenting Map: " + map.getName());
		
		t.addTask(new GlacialTask("Game Stop Callback")
		{
			public void run()
			{
				if(map.isAccenting())
				{
					setDelay(20);
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
		
		((GlacialServer) pl).getNotificationController().start();
		
		t.addTask(new GlacialTask("Region Monitor")
		{
			@Override
			public void run()
			{
				if(isRunning() || isStarting() || isStopping() || isRestarting())
				{
					setDelay(20);
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
					Notification n = new Notification().setSubSubTitle(prog).setPriority(NotificationPriority.HIGH);
					
					((GlacialServer) pl).getNotificationController().dispatch(n, ((GlacialServer) pl).getNotificationController().getDevChannel());
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
