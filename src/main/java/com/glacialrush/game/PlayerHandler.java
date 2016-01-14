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
		o("Balancing Players");
		rebalance();
		o("Respawning Players");
		respawn();
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
		if(factions.containsKey(p))
		{
			return;
		}
		
		factions.put(p, smallest());
		
		pl.getNotificationController().dispatch(new Notification().setSubTitle(getFaction(p).getColor() + "You Fight with " + getFaction(p).getName()).setPriority(NotificationPriority.HIGH).setSound(new GSound(Sound.AMBIENCE_THUNDER, 1f, 1.7f)), p);
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
