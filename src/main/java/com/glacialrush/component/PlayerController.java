package com.glacialrush.component;

import java.util.Iterator;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.glacialrush.GlacialServer;
import com.glacialrush.Info;
import com.glacialrush.api.component.Controller;
import com.glacialrush.api.dispatch.notification.Notification;
import com.glacialrush.api.dispatch.notification.NotificationPriority;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.object.GSound;
import com.glacialrush.composite.Job;
import com.glacialrush.composite.Manipulation;
import com.glacialrush.game.event.PlayerControlEvent;
import com.glacialrush.packet.resourcepack.ResourcePackAcceptedEvent;
import com.glacialrush.packet.resourcepack.ResourcePackDeclinedEvent;
import com.glacialrush.packet.resourcepack.ResourcePackFailedEvent;
import com.glacialrush.packet.resourcepack.ResourcePackLoadedEvent;
import com.glacialrush.packet.resourcepack.ResourcePackSentEvent;
import com.glacialrush.xapi.Cuboid;
import com.glacialrush.xapi.Cuboid.CuboidDirection;
import net.md_5.bungee.api.ChatColor;

public class PlayerController extends Controller
{
	private GList<Player> disabled;
	
	public PlayerController(GlacialServer pl)
	{
		super(pl);
		
		this.disabled = new GList<Player>();
	}
	
	public void preEnable()
	{
		super.preEnable();
	}
	
	public void postEnable()
	{
		super.postEnable();
	}
	
	public void preDisable()
	{
		super.preDisable();
	}
	
	public void postDisable()
	{
		super.postDisable();
	}
	
	public void sendPack(Player p)
	{
		((GlacialServer) pl).getResourceController().send(p, Info.RESOURCE_PACK);
	}
	
	public void disableAll()
	{
		for(Player i : pl.onlinePlayers())
		{
			disable(i);
		}
	}
	
	public void enableAll()
	{
		for(Player i : pl.onlinePlayers())
		{
			enable(i);
		}
	}
	
	public void disable(Player p)
	{
		if(disabled.contains(p))
		{
			return;
		}
		
		disabled.add(p);
		p.setGameMode(GameMode.ADVENTURE);
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100000, 50));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100000, 50));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100000, 50));
		drawBox(p);
	}
	
	public void enable(Player p)
	{
		disabled.remove(p);
		p.setGameMode(GameMode.ADVENTURE);
		p.setHealth(p.getMaxHealth());
		p.removePotionEffect(PotionEffectType.BLINDNESS);
		p.removePotionEffect(PotionEffectType.SLOW);
		p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
		undrawBox(p);
	}
	
	public void drawBox(Player p)
	{
		Material m = Material.COAL_BLOCK;
		
		Job j = new Job("Box Draw", ((GlacialServer) pl).getJobController());
		
		Cuboid c = new Cuboid(p.getLocation().add(2, 2, 2), p.getLocation().add(-2, -2, -2));
		Iterator<Block> it = c.iterator();
		
		while(it.hasNext())
		{
			Block bb = it.next();
			
			j.add(new Manipulation(bb.getLocation(), Material.AIR, p));
		}
		
		Iterator<Block> u = c.getFace(CuboidDirection.Up).iterator();
		Iterator<Block> d = c.getFace(CuboidDirection.Down).iterator();
		Iterator<Block> l = c.getFace(CuboidDirection.West).iterator();
		Iterator<Block> r = c.getFace(CuboidDirection.East).iterator();
		Iterator<Block> f = c.getFace(CuboidDirection.North).iterator();
		Iterator<Block> b = c.getFace(CuboidDirection.South).iterator();
		
		while(u.hasNext())
		{
			Block block = u.next();
			
			j.add(new Manipulation(block.getLocation(), m, p));
		}
		
		while(d.hasNext())
		{
			Block block = d.next();
			
			j.add(new Manipulation(block.getLocation(), m, p));
		}
		
		while(l.hasNext())
		{
			Block block = l.next();
			
			j.add(new Manipulation(block.getLocation(), m, p));
		}
		
		while(r.hasNext())
		{
			Block block = r.next();
			
			j.add(new Manipulation(block.getLocation(), m, p));
		}
		
		while(f.hasNext())
		{
			Block block = f.next();
			
			j.add(new Manipulation(block.getLocation(), m, p));
		}
		
		while(b.hasNext())
		{
			Block block = b.next();
			
			j.add(new Manipulation(block.getLocation(), m, p));
		}
		
		((GlacialServer) pl).getJobController().addJob(j);
	}
	
	public void undrawBox(Player p)
	{
		Job j = new Job("Box Draw", ((GlacialServer) pl).getJobController());
		
		Cuboid c = new Cuboid(p.getLocation().add(2, 2, 2), p.getLocation().add(-2, -2, -2));
		Iterator<Block> it = c.iterator();
		
		while(it.hasNext())
		{
			Block bb = it.next();
			
			j.add(new Manipulation(bb.getLocation(), bb.getType(), p));
		}
		
		((GlacialServer) pl).getJobController().addJob(j);
	}
	
	@EventHandler
	public void onPlayerResource(final ResourcePackDeclinedEvent e)
	{
		Player p = e.getPlayer();
		
		Notification n = new Notification();
		n.setFadeIn(5);
		n.setStayTime(30);
		n.setFadeOut(40);
		n.setTitle(ChatColor.RED + "Hey There!");
		n.setSubTitle(ChatColor.RED + "" + ChatColor.UNDERLINE + "We only modify unused items and sounds!");
		
		Notification n2 = new Notification();
		n2.setFadeIn(5);
		n2.setStayTime(30);
		n2.setFadeOut(40);
		n2.setTitle(ChatColor.RED + "Simply Rejoin");
		n2.setSubTitle(ChatColor.RED + "If you have changed your mind :D");
		
		((GlacialServer) pl).getNotificationController().dispatch(n, ((GlacialServer) pl).getNotificationController().getBroadChannel(), p);
		((GlacialServer) pl).getNotificationController().dispatch(n2, ((GlacialServer) pl).getNotificationController().getBroadChannel(), p);
		
		pl.scheduleSyncTask(150, new Runnable()
		{
			@Override
			public void run()
			{
				e.getPlayer().kickPlayer(ChatColor.RED + "Resource Pack Declined." + "\n" + ChatColor.YELLOW + "Please Rejoin to Try Again!");
			}
		});
	}
	
	@EventHandler
	public void onPlayerResource(ResourcePackSentEvent e)
	{
		Player p = e.getPlayer();
	}
	
	@EventHandler
	public void onPlayerResource(ResourcePackAcceptedEvent e)
	{
		Player p = e.getPlayer();
		
		Notification n = new Notification();
		n.setFadeIn(5);
		n.setStayTime(20);
		n.setFadeOut(40);
		n.setTitle(ChatColor.AQUA + "Please Wait");
		n.setSubTitle(ChatColor.GREEN + "Loading Resource Pack...");
		n.setSound(new GSound(Sound.ANVIL_USE));
		
		((GlacialServer) pl).getNotificationController().dispatch(n, ((GlacialServer) pl).getNotificationController().getBroadChannel(), p);
	}
	
	@EventHandler
	public void onPlayerResource(ResourcePackLoadedEvent e)
	{
		final Player p = e.getPlayer();
		
		((GlacialServer) pl).getNotificationController().clearPlayer(p);
		
		Notification n = new Notification();
		n.setFadeIn(5);
		n.setStayTime(30);
		n.setFadeOut(40);
		n.setSubSubTitle(ChatColor.GREEN + "Resource Pack Loaded");
		
		((GlacialServer) pl).getNotificationController().dispatch(n, ((GlacialServer) pl).getNotificationController().getBroadChannel(), p);
		
		GameController g = ((GlacialServer) pl).getGameController();
		
		if(g.isRunning())
		{
			g.getPlayerHandler().loadedRSP(e.getPlayer());
		}
		
		enable(e.getPlayer());
		
		pl.scheduleSyncTask(100, new Runnable()
		{
			public void run()
			{
				((GlacialServer) pl).getNotificationController().clearPlayer(p);
			}
		});
	}
	
	@EventHandler
	public void onPlayerResource(ResourcePackFailedEvent e)
	{
		Player p = e.getPlayer();
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e)
	{
		if(!e.getEntityType().equals(EntityType.PLAYER))
		{
			return;
		}
		
		if(disabled.contains((Player) e.getEntity()))
		{
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerControl(PlayerControlEvent e)
	{
		if(!e.getRegion().connected(e.getFaction()))
		{
			e.setCancelled(true);
			((GlacialServer) pl).getNotificationController().dispatch(new Notification().setSubTitle(e.getRegion().getFaction().getColor() + e.getRegion().getFaction().getName() + " Secured").setSubSubTitle(e.getFaction().getColor() + "You're Faction, " + e.getFaction().getName() + " does not own a connected region").setPriority(NotificationPriority.VERYHIGH).setFadeIn(0).setFadeOut(10).setStayTime(20).setOngoing(true), ((GlacialServer) pl).getNotificationController().getCapChannel(), e.getPlayer());
		}
	}
}
