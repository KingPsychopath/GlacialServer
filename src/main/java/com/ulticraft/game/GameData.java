package com.ulticraft.game;

import com.ulticraft.map.DeployableMap;

public class GameData
{
	private DeployableMap map;
	private long millis;
	
	public GameData(DeployableMap map, long millis)
	{
		this.map = map;
		this.millis = millis;
	}
	
	public DeployableMap getMap()
	{
		return map;
	}
	
	public void setMap(DeployableMap map)
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
