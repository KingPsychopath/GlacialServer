package com.ulticraft.component;

import java.util.Random;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.ulticraft.GlacialRush;
import com.ulticraft.faction.Faction;
import com.ulticraft.uapi.Component;
import com.ulticraft.uapi.UMap;

public class FactionComponent extends Component implements Listener
{
	private UMap<Player, Faction> players;
	
	public FactionComponent(GlacialRush pl)
	{
		super(pl);
		
		players = new UMap<Player, Faction>();
	}
	
	public void enable()
	{
		pl.register(this);
	}
	
	public void disable()
	{
		
	}
	
	public void insertPlayer(Player p)
	{
		int e = 0;
		int c = 0;
		int o = 0;
		
		for(Player i : players.keySet())
		{
			if(players.get(i).equals(Faction.enigma()))
			{
				e++;
			}
			
			else if(players.get(i).equals(Faction.cryptic()))
			{
				c++;
			}
			
			else if(players.get(i).equals(Faction.omni()))
			{
				o++;
			}
		}
		
		if(e < o && e < c)
		{
			players.put(p, Faction.enigma());
		}
		
		else if(c < e && c < o)
		{
			players.put(p, Faction.cryptic());
		}
		
		else if(o < c && o < e)
		{
			players.put(p, Faction.omni());
		}
		
		else if(e < o || e < c)
		{
			players.put(p, Faction.enigma());
		}
		
		else if(c < e || c < o)
		{
			players.put(p, Faction.cryptic());
		}
		
		else if(o < c || o < e)
		{
			players.put(p, Faction.omni());
		}
		
		else
		{
			int s = new Random().nextInt(3);
			
			if(s == 0)
			{
				players.put(p, Faction.enigma());
			}
			
			if(s == 1)
			{
				players.put(p, Faction.cryptic());
			}
			
			if(s == 2)
			{
				players.put(p, Faction.omni());
			}
		}
	}
	
	public void rebalance()
	{
		int sel = 1;
		
		players.clear();
		
		for(Player i : pl.onlinePlayers())
		{
			if(sel == 1)
			{
				players.put(i, Faction.enigma());
			}
			
			else if(sel == 2)
			{
				players.put(i, Faction.cryptic());
			}
			
			else if(sel == 3)
			{
				players.put(i, Faction.omni());
				sel = 0;
			}
			
			sel++;
		}
	}
	
	public UMap<Player, Faction> getPlayers()
	{
		return players;
	}

	public void setPlayers(UMap<Player, Faction> players)
	{
		this.players = players;
	}

	@EventHandler
	public void onPlayer(PlayerJoinEvent e)
	{
		insertPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayer(PlayerQuitEvent e)
	{
		players.remove(e.getPlayer());
	}
}
