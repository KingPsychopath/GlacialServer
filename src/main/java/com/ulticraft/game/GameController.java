package com.ulticraft.game;

import com.ulticraft.GlacialRush;

public class GameController
{
	private GlacialRush pl;
	private Game g;
	private GameCapture gc;
	
	public GameController(GlacialRush pl)
	{
		this.pl = pl;
	}
	
	public void start()
	{
		g = new Game(pl);
		gc = new GameCapture(pl);
		
		g.register(gc);
		g.start();
	}
	
	public void stop()
	{
		g.stop();
		g.getRegistrants().clear();
	}
}
