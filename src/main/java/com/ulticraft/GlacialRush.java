package com.ulticraft;

import java.util.Collection;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import com.ulticraft.component.CommandComponent;
import com.ulticraft.component.DataComponent;
import com.ulticraft.component.TestComponent;
import com.ulticraft.composite.PlayerData;
import com.ulticraft.region.GameState;
import com.ulticraft.uapi.ComponentManager;
import com.ulticraft.uapi.Dispatcher;

public class GlacialRush extends JavaPlugin
{
	private Dispatcher dispatcher;
	private ComponentManager componentManager;
	private TestComponent testComponent;
	private CommandComponent commandComponent;
	private DataComponent dataComponent;
	private GameState gameState;
	
	public void onEnable()
	{
		dispatcher = new Dispatcher(this);
		componentManager = new ComponentManager(this);
		dataComponent = new DataComponent(this);
		commandComponent = new CommandComponent(this);
		testComponent = new TestComponent(this);
		componentManager.register(dataComponent);
		componentManager.register(commandComponent);
		componentManager.register(testComponent);
		componentManager.enable();
		
		getCommand(Info.CMD_GR).setExecutor(commandComponent);
		register(commandComponent);
		
		gameState = new GameState(this);
		gameState.start();
	}
	
	public void onDisable()
	{
		gameState.stop();
	}
	
	public DataComponent getDataComponent()
	{
		return dataComponent;
	}
	
	public PlayerData gpd(Player p)
	{
		return dataComponent.get(p);
	}
	
	public void msg(CommandSender sender, String msg)
	{
		sender.sendMessage(msg);
	}
	
	public void msg(CommandSender sender, String[] msgs)
	{
		for(String i : msgs)
		{
			msg(sender, i);
		}
	}
	
	public int scheduleSyncRepeatingTask(int delay, int interval, Runnable runnable)
	{
		return getServer().getScheduler().scheduleSyncRepeatingTask(this, runnable, delay, interval);
	}
	
	public int scheduleSyncTask(int delay, Runnable runnable)
	{
		return getServer().getScheduler().scheduleSyncDelayedTask(this, runnable, delay);
	}
	
	public void cancelTask(int tid)
	{
		getServer().getScheduler().cancelTask(tid);
	}
	
	public Collection<? extends Player> onlinePlayers()
	{
		return getServer().getOnlinePlayers();
	}
	
	public boolean canFindPlayer(String search)
	{
		return findPlayer(search) == null ? false : true;
	}
	
	public Player findPlayer(String search)
	{
		for(Player i : onlinePlayers())
		{
			if(i.getName().equalsIgnoreCase(search))
			{
				return i;
			}
		}
		
		for(Player i : onlinePlayers())
		{
			if(i.getName().toLowerCase().contains(search.toLowerCase()))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public void register(Listener listener)
	{
		getServer().getPluginManager().registerEvents(listener, this);
	}
	
	public void unRegister(Listener listener)
	{
		HandlerList.unregisterAll(listener);
	}
	
	public ComponentManager getComponentManager()
	{
		return componentManager;
	}

	public Dispatcher getDispatcher()
	{
		return dispatcher;
	}
	
	public void i(String... o)
	{
		dispatcher.info(o);
	}
	
	public void s(String... o)
	{
		dispatcher.success(o);
	}
	
	public void f(String... o)
	{
		dispatcher.failure(o);
	}
	
	public void w(String... o)
	{
		dispatcher.warning(o);
	}
	
	public void v(String... o)
	{
		dispatcher.verbose(o);
	}
	
	public void o(String... o)
	{
		dispatcher.overbose(o);
	}
	
	public void si(String... o)
	{
		dispatcher.sinfo(o);
	}
	
	public void ss(String... o)
	{
		dispatcher.ssuccess(o);
	}
	
	public void sf(String... o)
	{
		dispatcher.sfailure(o);
	}
	
	public void sw(String... o)
	{
		dispatcher.swarning(o);
	}
	
	public void sv(String... o)
	{
		dispatcher.sverbose(o);
	}
	
	public void so(String... o)
	{
		dispatcher.soverbose(o);
	}
}
