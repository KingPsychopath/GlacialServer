package com.ulticraft.game;

import com.ulticraft.GlacialServer;
import com.ulticraft.xapi.UList;

public class GameRegistry
{
	private GlacialServer pl;
	private UList<GameComponent> components;
	
	public GameRegistry(GlacialServer pl)
	{
		this.pl = pl;
		this.components = new UList<GameComponent>();
	}
}
