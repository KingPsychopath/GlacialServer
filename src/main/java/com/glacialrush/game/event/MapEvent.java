package com.glacialrush.game.event;

import com.glacialrush.composite.Map;

public class MapEvent extends GlacialEvent
{
	private Map map;
	
	public MapEvent(Map map)
	{
		this.map = map;
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
