package com.glacialrush.game.component;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import com.glacialrush.composite.Capture;
import com.glacialrush.composite.Faction;
import com.glacialrush.composite.Hunk;
import com.glacialrush.composite.Region;
import com.glacialrush.game.Game;
import com.glacialrush.game.GameComponent;
import com.glacialrush.game.Tickrement;
import com.glacialrush.game.Tickreval;
import com.glacialrush.game.event.EnterCapturePointEvent;
import com.glacialrush.game.event.ExitCapturePointEvent;
import com.glacialrush.xapi.UList;
import com.glacialrush.xapi.UMap;

@Tickrement(Tickreval.SECOND)
public class EventRippler implements GameComponent, Listener
{
	private Game game;
	
	private UMap<Capture, UList<Player>> offensives;
	
	public EventRippler()
	{
		offensives = new UMap<Capture, UList<Player>>();
	}
	
	@Override
	public void onTick(Game g)
	{
		
	}

	@Override
	public void onStart(Game g)
	{
		game = g;
		g.pl().register(this);
	}

	@Override
	public void onStop(Game g)
	{
		g.pl().unRegister(this);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e)
	{
		if(game.getState().getMap().contains(e.getFrom()) && !game.getState().getMap().contains(e.getTo()))
		{
			e.setCancelled(true);
		}
		
		if(!game.getState().getMap().contains(e.getFrom()) && game.getState().getMap().contains(e.getTo()))
		{
			e.setCancelled(true);
		}
		
		if(game.getState().getMap().contains(e.getTo()))
		{
			Region r = game.getState().getMap().getRegion(new Hunk(e.getTo()));
			
			for(Capture i : r.getCaptures())
			{
				if(e.getTo().distanceSquared(i.getLocation()) < 25)
				{
					if(!offensives.get(i).contains(e.getPlayer()))
					{
						offensives.get(i).add(e.getPlayer());
						game.pl().callEvent(new EnterCapturePointEvent(i, e.getPlayer()));
					}
				}
				
				else
				{
					if(offensives.get(i).contains(e.getPlayer()))
					{
						offensives.get(i).remove(e.getPlayer());
						game.pl().callEvent(new ExitCapturePointEvent(i, e.getPlayer()));
					}
				}
				
				Faction def = i.getDefense();
				Faction fac = null;
				int op = 0;
				int dp = 0;
				int opf = 0;
				
				for(Player j : offensives.get(i))
				{
					
				}
			}
		}
	}
}
