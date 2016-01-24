package com.glacialrush.server;

import com.glacialrush.api.GlacialPlugin;
import com.glacialrush.api.game.GameController;

public class GlacialServer extends GlacialPlugin
{
	private GameController gameController;
	private CommandController commandController;
	
	public void onEnable()
	{
		super.onEnable();
		
		gameController = new GameController(this);
		commandController = new CommandController(this);
		
		super.startComponents();
		
		getCommand(Info.CMD_GAME).setExecutor(commandController);
		getCommand(Info.CMD_REGION).setExecutor(commandController);
		getCommand(Info.CMD_MAP).setExecutor(commandController);
		getCommand(Info.CMD_BRUSH).setExecutor(commandController);
	}

	public GameController getGameController()
	{
		return gameController;
	}
}
