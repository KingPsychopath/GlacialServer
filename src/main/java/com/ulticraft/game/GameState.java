package com.ulticraft.game;

import com.ulticraft.GlacialRush;
import com.ulticraft.composite.FactionMap;
import com.ulticraft.composite.Map;

public class GameState
{
	private GlacialRush pl;
	private Map map;
	private FactionMap factionMap;
	
	public GameState(GlacialRush pl)
	{
		this.pl = pl;
		this.factionMap = new FactionMap(pl);
	}
}
