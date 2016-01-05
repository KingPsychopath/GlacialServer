package com.ulticraft.game.event;

import com.ulticraft.composite.Map;

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
