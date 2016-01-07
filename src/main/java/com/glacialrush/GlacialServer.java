package com.glacialrush;

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
		
		scheduleSyncTask(200, new Runnable()
		{
			@Override
			public void run()
			{
				game.startGame();
			}
		});
	}
	
	public void onDisable()
	{
		game.save();
		
		super.onDisable();
	}
	
	public void restart()
	{
		game.stopGame();
		getServer().dispatchCommand(getServer().getConsoleSender(), "plugman restart GlacialServer");
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
