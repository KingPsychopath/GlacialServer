package com.ulticraft.game;

import com.ulticraft.map.Map;

public class GameData
{
	private Map map;
	private long millis;
	
	public GameData(Map map, long millis)
	{
		this.map = map;
		this.millis = millis;
	}
	
	public Map getMap()
	{
		return map;
	}
	
	public void setMap(Map map)
	{
		this.map = map;
	}
	
	public long getMillis()
	{
		return millis;
	}
	
	public void setMillis(long millis)
	{
		this.millis = millis;
	}
}
