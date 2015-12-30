package com.ulticraft.game;

import com.ulticraft.GlacialRush;
import com.ulticraft.composite.FactionMap;
import com.ulticraft.composite.Map;

public class GameState
{
	private GlacialRush pl;
	private Map map;
	private FactionMap factionMap;
	
	public GameState(GlacialRush pl, Map map)
	{
		this.pl = pl;
		this.factionMap = new FactionMap(pl);
		this.map = map;
	}

	public GlacialRush getPl()
	{
		return pl;
	}

	public void setPl(GlacialRush pl)
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
