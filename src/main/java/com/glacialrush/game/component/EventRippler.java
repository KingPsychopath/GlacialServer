package com.glacialrush.game.component;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import com.glacialrush.composite.Capture;
import com.glacialrush.composite.Region;
import com.glacialrush.game.Game;
import com.glacialrush.game.GameComponent;
import com.glacialrush.game.Tickrement;
import com.glacialrush.game.Tickreval;
import com.glacialrush.game.event.FactionCaptureEvent;
import com.glacialrush.xapi.UList;
import com.glacialrush.xapi.UMap;

@Tickrement(Tickreval.SECOND)
public class EventRippler implements GameComponent, Listener
{
	private Game game;
	
	private UMap<Capture, UList<Player>> offensives;
	private UMap<Capture, FactionCaptureEvent> events;
	private UMap<Player, Location> respawns;
	
	public EventRippler()
	{
		offensives = new UMap<Capture, UList<Player>>();
		events = new UMap<Capture, FactionCaptureEvent>();
		respawns = new UMap<Player, Location>();
	}
	
	@Override
	public void onTick(Game g)
	{
		for(Region i : g.getState().getMap().getRegions())
		{
			for(Capture j : i.getCaptures())
			{
				if(!j.getPlayers().isEmpty())
				{
					j.tick(g);
				}
			}
		}
		
		for(Capture i : events.keySet())
		{
			g.pl().callEvent(events.get(i));
		}
		
		events.clear();
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
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		respawns.put(e.getEntity(), e.getEntity().getLocation());
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e)
	{
		if(respawns.containsKey(e.getPlayer()))
		{
			Location l = respawns.get(e.getPlayer()).clone();
			respawns.remove(e.getPlayer());
			e.setRespawnLocation(game.getRespawnNear(e.getPlayer(), l));
		}
		
		else
		{
			e.setRespawnLocation(game.getRespawn(e.getPlayer()));
		}
	}
	
	public UList<Player> getPlayers(Capture capture)
	{
		return offensives.get(capture);
	}
}
