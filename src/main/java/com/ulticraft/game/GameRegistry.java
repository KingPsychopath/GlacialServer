package com.ulticraft.game;

import com.ulticraft.GlacialRush;
import com.ulticraft.uapi.UList;

public class GameRegistry
{
	private GlacialRush pl;
	private UList<GameComponent> components;
	
	public GameRegistry(GlacialRush pl)
	{
		this.pl = pl;
		this.components = new UList<GameComponent>();
	}
}
