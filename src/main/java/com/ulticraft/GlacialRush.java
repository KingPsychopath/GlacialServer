package com.ulticraft;

import java.util.Collection;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import com.ulticraft.component.CommandComponent;
import com.ulticraft.component.WorldComponent;
import com.ulticraft.game.Game;
import com.ulticraft.game.GameCapture;
import com.ulticraft.map.GameState;
import com.ulticraft.uapi.ComponentManager;
import com.ulticraft.uapi.Dispatcher;

public class GlacialRush extends JavaPlugin
{
	private Dispatcher dispatcher;
	private ComponentManager componentManager;
	private WorldComponent worldComponent;
	private CommandComponent commandComponent;
	private Game g;
	
	private GameState state;
	
	public void onEnable()
	{
		dispatcher = new Dispatcher(this);
		componentManager = new ComponentManager(this);
		state = new GameState();
		
		worldComponent = new WorldComponent(this);
		commandComponent = new CommandComponent(this);
		
		componentManager.register(worldComponent);
		componentManager.register(commandComponent);
		
		getCommand(Info.COMMAND_GLACIAL_RUSH).setExecutor(commandComponent);
		
		componentManager.enable();
		
		g = new Game(this, null);
		g.register(new GameCapture(this));
		
		g.start();
	}
	
	public void onDisable()
	{
		g.stop();
		componentManager.disable();
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
	
	public CommandComponent getCommandComponent()
	{
		return commandComponent;
	}
	
	public WorldComponent getWorldComponent()
	{
		return worldComponent;
	}
	
	public GameState getState()
	{
		return state;
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
