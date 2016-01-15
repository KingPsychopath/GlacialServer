package com.glacialrush.game;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.dispatch.notification.Notification;
import com.glacialrush.api.dispatch.notification.NotificationPriority;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.object.GMap;
import com.glacialrush.api.object.GSound;
import com.glacialrush.composite.Faction;

public class PlayerHandler extends GlacialHandler
{
	protected GMap<Player, Faction> factions;
	
	public PlayerHandler(GlacialServer pl)
	{
		super(pl);
	}
	
	@Override
	public void start()
	{
		factions = new GMap<Player, Faction>();
	}
	
	@Override
	public void begin()
	{
	
	}
	
	@Override
	public void finish()
	{
	
	}
	
	@Override
	public void stop()
	{
	
	}
	
	@Override
	public void tick()
	{
	
	}
	
	public GMap<Player, Faction> getFactions()
	{
		return factions;
	}
	
	public boolean contested(GMap<Faction, GList<Player>> map)
	{
		if(map.size() == 1)
		{
			return false;
		}
		
		if(map.size() == 2)
		{
			return map.get(map.ukeys().get(0)).size() == map.get(map.ukeys().get(1)).size();
		}
		
		if(map.size() == 3)
		{
			if(map.get(map.ukeys().get(2)).size() == map.get(map.ukeys().get(0)).size() && map.get(map.ukeys().get(0)).size() > map.get(map.ukeys().get(1)).size())
			{
				return true;
			}
			
			if(map.get(map.ukeys().get(1)).size() == map.get(map.ukeys().get(2)).size() && map.get(map.ukeys().get(2)).size() > map.get(map.ukeys().get(0)).size())
			{
				return true;
			}
			
			if(map.get(map.ukeys().get(1)).size() == map.get(map.ukeys().get(0)).size() && map.get(map.ukeys().get(0)).size() > map.get(map.ukeys().get(2)).size())
			{
				return true;
			}
			
			if(map.get(map.ukeys().get(0)).size() == map.get(map.ukeys().get(2)).size() && map.get(map.ukeys().get(2)).size() == map.get(map.ukeys().get(1)).size())
			{
				return true;
			}
		}
		
		return false;
	}
	
	public Faction strongest(GMap<Faction, GList<Player>> map)
	{
		int max = Integer.MIN_VALUE;
		Faction s = null;
		
		for(Faction i : map.keySet())
		{
			if(map.get(i).size() > max)
			{
				max = map.get(i).size();
				s = i;
			}
		}
		
		return s;
	}
	
	public Faction weakest(GMap<Faction, GList<Player>> map)
	{
		int min = Integer.MAX_VALUE;
		Faction s = null;
		
		for(Faction i : map.keySet())
		{
			if(map.get(i).size() < min)
			{
				min = map.get(i).size();
				s = i;
			}
		}
		
		return s;
	}
	
	public GMap<Player, Faction> getFaction(GList<Player> players)
	{
		GMap<Player, Faction> fx = new GMap<Player, Faction>();
		
		for(Player i : players)
		{
			fx.put(i, getFaction(i));
		}
		
		return fx;
	}
	
	public void respawn()
	{
		for(Player i : pl.onlinePlayers())
		{
			respawn(i);
		}
	}
	
	public void respawn(Player p)
	{
		p.teleport(pl.getGameController().getMap().getSpawns().get(getFaction(p)).getSpawn());
	}
	
	public GList<Player> getPlayers(Faction f)
	{
		return factions.flip().get(f);
	}
	
	public int factionCount(Faction f)
	{
		return getPlayers(f) == null ? 0 : getPlayers(f).size();
	}
	
	public Faction getFaction(Player p)
	{
		return factions.get(p);
	}
	
	public Faction smallest()
	{
		int max = Integer.MAX_VALUE;
		Faction d = null;
		
		for(Faction i : Faction.all())
		{
			if(factionCount(i) < max)
			{
				d = i;
				max = factionCount(i);
			}
		}
		
		return d;
	}
	
	public void rebalance()
	{
		factions.clear();
		
		for(Player i : pl.onlinePlayers())
		{
			insert(i);
		}
	}
	
	public void insert(Player p)
	{
		Faction f = smallest();
		
		factions.put(p, f);
		
		pl.getNotificationController().dispatch(new Notification().setSubTitle(getFaction(p).getColor() + "You Fight with " + getFaction(p).getName()).setPriority(NotificationPriority.HIGH).setFadeIn(5).setStayTime(20).setFadeOut(30).setSound(new GSound(Sound.AMBIENCE_THUNDER, 1f, 1.7f)), pl.getNotificationController().getGameChannel(), p);
	}
	
	public void remove(Player p)
	{
		factions.remove(p);
	}
	
	@EventHandler
	public void onPlayer(PlayerJoinEvent e)
	{
		if(!g.isRunning())
		{
			return;
		}
		
		insert(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayer(PlayerQuitEvent e)
	{
		if(!g.isRunning())
		{
			return;
		}
		
		remove(e.getPlayer());
	}
}
