package com.glacialrush.composite;

import java.util.Iterator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.glacialrush.GlacialServer;
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
		
		return null;
	}
	
	@EventHandler
	public void onPlayer(PlayerJoinEvent e)
	{
		insert(e.getPlayer());
		pl.getGame().respawn(e.getPlayer());
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