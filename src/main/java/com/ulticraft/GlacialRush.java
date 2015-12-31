package com.ulticraft;

import java.util.Collection;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import com.ulticraft.component.AdministrationComponent;
import com.ulticraft.component.CommandComponent;
import com.ulticraft.component.DataComponent;
import com.ulticraft.component.ManipulationComponent;
import com.ulticraft.component.PlayerComponent;
import com.ulticraft.component.UIComponent;
import com.ulticraft.game.Game;
import com.ulticraft.uapi.ComponentManager;
import com.ulticraft.uapi.Dispatcher;

public class GlacialRush extends JavaPlugin
{
	private Dispatcher dispatcher;
	private ComponentManager componentManager;
	private CommandComponent commandComponent;
	private ManipulationComponent manipulationComponent;
	private DataComponent dataComponent;
	private PlayerComponent playerComponent;
	private UIComponent uiComponent;
	private AdministrationComponent administrationComponent;
	private Game game;
	
	public void onEnable()
	{
		dispatcher = new Dispatcher(this);
		componentManager = new ComponentManager(this);
		
		commandComponent = new CommandComponent(this);
		manipulationComponent = new ManipulationComponent(this);
		dataComponent = new DataComponent(this);
		playerComponent = new PlayerComponent(this);
		uiComponent = new UIComponent(this);
		administrationComponent = new AdministrationComponent(this);
		
		componentManager.register(commandComponent);
		componentManager.register(manipulationComponent);
		componentManager.register(dataComponent);
		componentManager.register(playerComponent);
		componentManager.register(uiComponent);
		componentManager.register(administrationComponent);
		
		getCommand(Info.COMMAND_GLACIAL_RUSH).setExecutor(commandComponent);
		
		componentManager.enable();
		
		game = new Game(this);
		game.load();
	}
	
	public void onDisable()
	{
		game.save();
		componentManager.disable();
	}
	
	public Game gg()
	{
		return game;
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
	
	public ManipulationComponent getManipulationComponent()
	{
		return manipulationComponent;
	}
	
	public CommandComponent getCommandComponent()
	{
		return commandComponent;
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
	
	public DataComponent getDataComponent()
	{
		return dataComponent;
	}
	
	public PlayerComponent getPlayerComponent()
	{
		return playerComponent;
	}

	public UIComponent getUiComponent()
	{
		return uiComponent;
	}

	public AdministrationComponent getAdministrationComponent()
	{
		return administrationComponent;
	}

	public Game getGame()
	{
		return game;
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
