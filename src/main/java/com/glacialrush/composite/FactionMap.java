package com.glacialrush.composite;

import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.glacialrush.GlacialServer;
import com.glacialrush.game.GameState;
import com.glacialrush.game.GameState.Status;
import com.glacialrush.game.event.PlayerFactionChangedEvent;
import com.glacialrush.xapi.UList;
import com.glacialrush.xapi.UMap;

public class FactionMap implements Listener
{
	private UMap<Faction, UList<Player>> players;
	private GlacialServer pl;
	
	public FactionMap(GlacialServer pl)
	{
		this.pl = pl;
		this.players = new UMap<Faction, UList<Player>>();
		
		players.put(Faction.omni(), new UList<Player>());
		players.put(Faction.cryptic(), new UList<Player>());
		players.put(Faction.enigma(), new UList<Player>());
		
		pl.register(this);
	}
	
	public UMap<Faction, UList<Player>> factionize(UList<Player> plrs)
	{
		UMap<Faction, UList<Player>> factions = players.copy();
		
		for(Faction i : factions.keySet())
		{
			Iterator<Player> it = factions.get(i).iterator();
			
			while(it.hasNext())
			{
				Player p = it.next();
				
				if(!plrs.contains(p))
				{
					it.remove();
				}
			}
		}
		
		return factions;
	}
	
	public UMap<Player, Faction> getFactions(UList<Player> plrs)
	{
		UMap<Player, Faction> factions = new UMap<Player, Faction>();
		
		for(Player i : plrs)
		{
			factions.put(i, getFaction(i));
		}
		
		return factions;
	}
	
	public void rebalance()
	{
		for(Faction i : players.keySet())
		{
			players.get(i).clear();
		}
		
		for(Player i : pl.onlinePlayers())
		{
			insert(i);
		}
	}
	
	public void insert(Player p)
	{
		Faction smallest = null;
		int size = Integer.MAX_VALUE;
		
		for(Faction i : players.keySet())
		{
			if(players.get(i).size() < size)
			{
				size = players.get(i).size();
				smallest = i;
			}
		}
		
		players.get(smallest).add(p);
		pl.callEvent(new PlayerFactionChangedEvent(p, smallest));
	}
	
	public Faction getFaction(Player player)
	{
		for(Faction i : players.keySet())
		{
			for(Player j : players.get(i))
			{
				if(player.equals(j))
				{
					return i;
				}
			}
		}
		
		insert(player);
		return getFaction(player);
	}
	
	@EventHandler
	public void onPlayer(PlayerJoinEvent e)
	{
		insert(e.getPlayer());
		
		if(pl.getGame().getState().getStatus().equals(Status.RUNNING))
		{
			pl.getGame().respawn(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onPlayer(PlayerQuitEvent e)
	{
		for(Faction i : players.keySet())
		{
			Iterator<Player> it = players.get(i).iterator();
			
			while(it.hasNext())
			{
				Player p = it.next();
				
				if(p.equals(e.getPlayer()))
				{
					it.remove();
					return;
				}
			}
		}
	}
}
