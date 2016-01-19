package com.glacialrush.game.event;

import com.glacialrush.component.GameController;
import com.glacialrush.composite.Map;

public class MapEvent extends GlacialEvent
{
	private Map map;
	
	public MapEvent(GameController gameController)
	{
		super(gameController);
		
		this.map = gameController.getMap();
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}
}
