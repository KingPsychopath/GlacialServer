package com.glacialrush;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.glacialrush.api.GlacialPlugin;
import com.glacialrush.component.CommandComponent;
import com.glacialrush.component.DataComponent;
import com.glacialrush.component.ManipulationComponent;
import com.glacialrush.composite.Faction;
import com.glacialrush.game.Game;

public class GlacialServer extends GlacialPlugin
{
	private CommandComponent commandComponent;
	private ManipulationComponent manipulationComponent;
	private DataComponent dataComponent;
	private Game game;
	
	public void onEnable()
	{
		commandComponent = new CommandComponent(this);
		manipulationComponent = new ManipulationComponent(this);
		dataComponent = new DataComponent(this);
		
		startComponentRegistry();
		
		getComponentManager().register(dataComponent);
		getComponentManager().register(manipulationComponent);
		getComponentManager().register(commandComponent);
		
		super.onEnable();
		
		getCommand(Info.COMMAND_GLACIAL_RUSH).setExecutor(commandComponent);
		
		game = new Game(this);
		game.load();
		
		scheduleSyncTask(10, new Runnable()
		{
			@Override
			public void run()
			{
				game.startGame();
				
				Location spawn = game.getState().getMap().getRegions().get(0).getSpawn();
				
				getServer().dispatchCommand(getServer().getConsoleSender(), "playsound " + "g.music.start @a " + spawn.getBlockX() + " " + spawn.getBlockY() + " " + spawn.getBlockZ() + " 10 1");
			}
		});
	}
	
	public void onDisable()
	{
		game.save();
		
		for(Player j : onlinePlayers())
		{
			j.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3600, 20));
			j.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3600, 20));
			j.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 3600, 20));
		}
		
		super.onDisable();
	}
	
	public void restart()
	{		
		final int[] tm = new int[] {0, 0};
		
		Location spawn = game.getState().getMap().getRegions().get(0).getSpawn();
		
		getServer().dispatchCommand(getServer().getConsoleSender(), "playsound " + "g.music.victory @a " + spawn.getBlockX() + " " + spawn.getBlockY() + " " + spawn.getBlockZ() + " 10 1");
		
		game.stopGame();
		
		tm[0] = scheduleSyncRepeatingTask(0, 20, new Runnable()
		{
			@Override
			public void run()
			{
				tm[1] = tm[1] + 1;
				
				if(tm[1] > 41)
				{
					cancelTask(tm[0]);
					getServer().dispatchCommand(getServer().getConsoleSender(), "plugman restart GlacialServer");
				}
			}
		});
	}

	public CommandComponent getCommandComponent()
	{
		return commandComponent;
	}

	public ManipulationComponent getManipulationComponent()
	{
		return manipulationComponent;
	}

	public DataComponent getDataComponent()
	{
		return dataComponent;
	}

	public Game getGame()
	{
		return game;
	}
}
