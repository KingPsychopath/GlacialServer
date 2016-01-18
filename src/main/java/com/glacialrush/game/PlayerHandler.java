package com.glacialrush.game;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.dispatch.Title;
import com.glacialrush.api.dispatch.notification.Notification;
import com.glacialrush.api.dispatch.notification.NotificationPriority;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.object.GMap;
import com.glacialrush.api.object.GSound;
import com.glacialrush.composite.Faction;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class PlayerHandler extends GlacialHandler
{
	protected GMap<Player, Faction> factions;
	protected GList<Player> resourced;
	
	public PlayerHandler(GlacialServer pl)
	{
		super(pl);
		resourced = new GList<Player>();
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
	
	public void verifyResource(final Player p)
	{
		if(!resourced.contains(p))
		{
			pl.getPlayerController().disable(p);
			
			if(pl.gpd(p).getAcceptedResourcePack())
			{
				Notification n = new Notification();
				n.setFadeIn(20);
				n.setStayTime(20);
				n.setFadeOut(40);
				n.setTitle(ChatColor.AQUA + "Please Wait");
				n.setSubTitle(ChatColor.GREEN + "Loading Resource Pack...");
				n.setSound(new GSound(Sound.ANVIL_USE));
				
				pl.getNotificationController().dispatch(n, pl.getNotificationController().getBroadChannel(), p);
				
				pl.scheduleSyncTask(30, new Runnable()
				{
					@Override
					public void run()
					{
						pl.getPlayerController().sendPack(p);
					}
				});
			}
			
			else
			{
				Notification n = new Notification();
				n.setFadeIn(5);
				n.setStayTime(30);
				n.setFadeOut(40);
				n.setTitle(ChatColor.AQUA + "Hey There!");
				n.setSubTitle(ChatColor.GREEN + "Glacial Rush Requires a Resource Pack");
				
				Notification n2 = new Notification();
				n2.setFadeIn(5);
				n2.setStayTime(30);
				n2.setFadeOut(40);
				n2.setTitle(ChatColor.AQUA + "Dont Worry");
				n2.setSubTitle(ChatColor.GREEN + "Your Texture Pack will still work.");
				
				final Title t = new Title();
				t.setTitle(ChatColor.GREEN + "" + ChatColor.UNDERLINE + "Please Accept");
				t.setFadeInTime(100);
				t.setStayTime(1200);
				t.setFadeOutTime(1200);
				
				pl.getNotificationController().dispatch(n, pl.getNotificationController().getBroadChannel(), p);
				pl.getNotificationController().dispatch(n2, pl.getNotificationController().getBroadChannel(), p);
				
				pl.scheduleSyncTask(155, new Runnable()
				{
					@Override
					public void run()
					{
						t.send(p);
					}
				});
				
				pl.scheduleSyncTask(190, new Runnable()
				{
					@Override
					public void run()
					{
						pl.getPlayerController().sendPack(p);
					}
				});
			}
		}
	}
	
	public void sendResourceInfo()
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
	
	public void loadedRSP(Player p)
	{
		if(!resourced.contains(p))
		{
			resourced.add(p);
		}
		
		pl.gpd(p).setAcceptedResourcePack(true);
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
	public void onPlayer(PlayerChatEvent e)
	{
		e.setCancelled(true);
		
		for(Player i : pl.onlinePlayers())
		{
			i.sendMessage(ChatColor.AQUA + "" + pl.getExperienceController().getBattleRank(e.getPlayer()) + " " + g.getPlayerHandler().getFaction(e.getPlayer()).getColor() + e.getPlayer().getName() + ChatColor.WHITE + ": " + e.getMessage());
		}
	}
	
	@EventHandler
	public void onPlayer(final PlayerJoinEvent e)
	{
		if(!g.isRunning())
		{
			return;
		}
		
		insert(e.getPlayer());
		
		pl.scheduleSyncTask(50, new Runnable()
		{
			@Override
			public void run()
			{
				verifyResource(e.getPlayer());
			}
		});
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
