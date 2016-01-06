package com.glacialrush.game.component;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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
				Faction faf = null;
				int op = 0;
				int dp = 0;
				int opf = 0;
				boolean ok = true;
				
				for(Player j : offensives.get(i))
				{
					Faction f = game.getState().getFactionMap().getFaction(j);
					
					if(f.equals(def))
					{
						dp++;
					}
					
					else
					{
						if(fac == null)
						{
							fac = f;
							op++;
						}
						
						else
						{
							if(f.equals(fac))
							{
								op++;
							}
							
							else
							{
								if(faf == null)
								{
									faf = f;
								}
								
								opf++;
							}
						}
					}
				}
				
				if(op < opf)
				{
					fac = faf;
					int k = op;
					int l = opf;
					
					op = k + l;
					opf = op;
				}
				
				else if(op > opf)
				{
					int k = op;
					int l = opf;
					
					op = k + l;
				}
				
				else
				{
					ok = false;
				}
				
				if(ok)
				{
					FactionCaptureEvent fce = new FactionCaptureEvent(i, fac, op, dp, opf);
					events.put(i, fce);
				}
			}
		}
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
