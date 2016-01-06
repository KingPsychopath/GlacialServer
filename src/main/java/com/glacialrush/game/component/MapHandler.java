package com.glacialrush.game.component;

import org.bukkit.event.Listener;
import com.glacialrush.game.Game;
import com.glacialrush.game.GameComponent;
import com.glacialrush.game.Tickrement;
import com.glacialrush.game.Tickreval;

@Tickrement(Tickreval.HALFSECOND)
public class MapHandler implements GameComponent, Listener
{
	@Override
	public void onTick(Game g)
	{
		
	}

	@Override
	public void onStart(Game g)
	{
		g.pl().register(this);
	}

	@Override
	public void onStop(Game g)
	{
		g.pl().unRegister(this);
	}
}
