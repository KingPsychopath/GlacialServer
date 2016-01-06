package com.glacialrush.game;

import com.glacialrush.GlacialServer;
import com.glacialrush.composite.FactionMap;
import com.glacialrush.composite.Map;

public class GameState
{
	private GlacialServer pl;
	private Map map;
	private FactionMap factionMap;
	private Status status;
	
	public enum Status
	{
		RUNNING, OFFLINE
	}
	
	public GameState(GlacialServer pl, Map map)
	{
		this.pl = pl;
		this.factionMap = new FactionMap(pl);
		this.map = map;
		this.status = Status.OFFLINE;
	}
	
	public void start()
	{
		status = Status.RUNNING;
	}
	
	public void stop()
	{
		status = Status.OFFLINE;
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}

	public FactionMap getFactionMap()
	{
		return factionMap;
	}

	public void setFactionMap(FactionMap factionMap)
	{
		this.factionMap = factionMap;
	}

	public Status getStatus()
	{
		return status;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}
}
