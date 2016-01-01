package com.ulticraft.game;

import com.ulticraft.GlacialServer;
import com.ulticraft.composite.FactionMap;
import com.ulticraft.composite.Map;

public class GameState
{
	private GlacialServer pl;
	private Map map;
	private FactionMap factionMap;
	
	public GameState(GlacialServer pl, Map map)
	{
		this.pl = pl;
		this.factionMap = new FactionMap(pl);
		this.map = map;
	}

	public GlacialServer getPl()
	{
		return pl;
	}

	public void setPl(GlacialServer pl)
	{
		this.pl = pl;
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
}
