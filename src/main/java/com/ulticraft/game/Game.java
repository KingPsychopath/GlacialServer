package com.ulticraft.game;

import com.ulticraft.GlacialRush;

public class Game
{
	private final GameState state;
	private final GameRegistry registry;
	private final GlacialRush pl;
	
	public Game(GlacialRush pl)
	{
		this.pl = pl;
		this.state = new GameState(pl);
		this.registry = new GameRegistry(pl);
	}

	public GameState getState()
	{
		return state;
	}

	public GameRegistry getRegistry()
	{
		return registry;
	}
}
