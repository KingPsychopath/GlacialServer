package com.glacialrush;

import com.glacialrush.api.GlacialPlugin;
import com.glacialrush.api.game.GameController;

public class GlacialServer extends GlacialPlugin
{
	private GameController gameController;
	
	public void onEnable()
	{
		super.onEnable();
		
		gameController = new GameController(this);
		
		super.startComponents();
	}

	public GameController getGameController()
	{
		return gameController;
	}
}
