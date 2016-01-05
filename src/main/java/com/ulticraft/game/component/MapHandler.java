package com.ulticraft.game.component;

import org.bukkit.event.Listener;
import com.ulticraft.game.Game;
import com.ulticraft.game.GameComponent;
import com.ulticraft.game.Tickrement;
import com.ulticraft.game.Tickreval;

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
