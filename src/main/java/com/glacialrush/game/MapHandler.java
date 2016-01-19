package com.glacialrush.game;

import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import com.glacialrush.GlacialServer;
import com.glacialrush.Info;
import com.glacialrush.api.dispatch.notification.Notification;
import com.glacialrush.api.dispatch.notification.NotificationPriority;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.object.GMap;
import com.glacialrush.api.object.GSound;
import com.glacialrush.composite.Capture;
import com.glacialrush.composite.Faction;
import com.glacialrush.composite.Region;
import com.glacialrush.game.event.ExperienceType;
import com.glacialrush.game.event.FactionRegionCaptureEvent;
import com.glacialrush.game.event.PlayerControlEvent;
import com.glacialrush.xapi.Duration;
import net.md_5.bungee.api.ChatColor;

public class MapHandler extends GlacialHandler
{
	private int captureDelay;
	private GList<Player> inCap;
	private GMap<Region, GMap<Player, Integer>> involved;
	private boolean check;
	
	public MapHandler(GlacialServer pl)
	{
		super(pl);
		
		captureDelay = 0;
		inCap = new GList<Player>();
		involved = new GMap<Region, GMap<Player, Integer>>();
		check = true;
	}
	
	@Override
	public void start()
	{
		inCap.clear();
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
		if(!check)
		{
			return;
		}
		
		captureDelay++;
		inCap.clear();
		
		if(captureDelay == 10)
		{
			captureDelay = 0;
			
			for(Region i : g.getMap().getRegions())
			{
				if(!i.getPlayers().isEmpty())
				{
					for(Capture j : i.getCaptures())
					{
						if(!j.getPlayers().isEmpty())
						{
							handleCapture(j);
						}
					}
				}
				
				handleRegion(i);
				handleMap();
			}
		}
	}
	
	public boolean contested(Faction sec, GMap<Faction, GList<Capture>> map)
	{
		if(map.size() < 3)
		{
			return false;
		}
		
		Integer h = null;
		
		for(Faction i : map.keySet())
		{
			if(!i.equals(sec))
			{
				if(h == null)
				{
					h = map.get(i).size();
				}
				
				else if(map.get(i).size() == h)
				{
					return true;
				}
				
				else
				{
					return false;
				}
			}
		}
		
		return false;
	}
	
	public Faction strongest(GMap<Faction, GList<Capture>> map)
	{
		int st = Integer.MIN_VALUE;
		Faction strongest = null;
		
		for(Faction i : map.keySet())
		{
			if(map.get(i).size() > st)
			{
				st = map.get(i).size();
				strongest = i;
			}
		}
		
		return strongest;
	}
	
	public void handleMap()
	{
		Faction cap = null;
		
		for(Region i : g.getMap().getRegions())
		{
			if(cap == null)
			{
				cap = i.getFaction();
			}
			
			else if(!cap.equals(i.getFaction()))
			{
				return;
			}
		}
		
		if(cap.equals(Faction.neutral()))
		{
			return;
		}
		
		pl.getPlayerController().disableAll();
		
		g.startCredits(cap);
		check = false;
	}
	
	public void handleRegion(Region region)
	{
		if(!region.isCaptureable())
		{
			Notification n = new Notification();
			n.setPriority(NotificationPriority.HIGH);
			
			String s = region.getFaction().getColor() + region.getName() + ":: " + ChatColor.DARK_GRAY + " <> ";
			
			for(Region i : region.connectedRegions())
			{
				if(i.isCaptureable())
				{
					s = s + i.getFaction().getColor() + i.getName() + ChatColor.DARK_GRAY + " <> ";
				}
			}
			
			n.setOngoing(true);
			n.setSubSubTitle(s);
			
			for(Player i : region.getPlayers())
			{
				pl.getNotificationController().dispatch(n, pl.getNotificationController().getMapChannel(), i);
			}
			
			return;
		}
		
		GList<Capture> captures = region.getCaptures();
		GMap<Capture, Faction> control = new GMap<Capture, Faction>();
		GMap<Faction, GList<Capture>> controlCount = new GMap<Faction, GList<Capture>>();
		Integer fr = 0;
		
		for(Capture i : captures)
		{
			control.put(i, i.getSecured());
			
			if(!i.getSecured().equals(region.getFaction()))
			{
				fr++;
			}
		}
		
		controlCount = control.flip();
		
		if(fr > captures.size() - fr)
		{
			if(!region.isTimer())
			{
				boolean st = false;
				
				for(Player i : pl.onlinePlayers())
				{
					if(pl.getCommandController().getGods().contains(i))
					{
						st = true;
						break;
					}
				}
				
				if(st)
				{
					region.startTimer(15);
				}
				
				else
				{
					region.startTimer(120);
				}
			}
			
			region.decTimer(System.currentTimeMillis());
			
			if(region.getTimer() == 0)
			{
				region.resetTimer();
				
				if(contested(region.getFaction(), controlCount))
				{
					region.setFaction(region.getFaction());
					region.getMap().handleUncapturableRegions();
				}
				
				else
				{
					region.setFaction(strongest(controlCount));
					
					pl.callEvent(new FactionRegionCaptureEvent(g, region, region.getFaction()));
					
					Notification n = new Notification().setPriority(NotificationPriority.HIGHEST);
					n.setTitle(region.getFaction().getColor() + "Territory Captured");
					n.setSubTitle(region.getFaction().getColor() + region.getFaction().getName() + " <> " + region.getName());
					n.setFadeIn(5);
					n.setStayTime(30);
					n.setSound(new GSound("g.event.region.capture"));
					n.setFadeOut(20);
					
					Notification n2 = new Notification().setPriority(NotificationPriority.HIGHEST);
					n2.setTitle(region.getFaction().getColor() + "Territory Lost");
					n2.setSubTitle(region.getFaction().getColor() + region.getFaction().getName() + " <> " + region.getName());
					n2.setFadeIn(5);
					n2.setStayTime(30);
					n2.setFadeOut(20);
					n2.setSound(new GSound("g.event.region.lost"));
					region.accent();
					
					for(Player i : g.getPlayerHandler().getPlayers(region.getFaction()))
					{
						pl.getExperienceController().giveExperience(i, Info.EXPERIENCE_REGION_CAPTURE, ExperienceType.TERRITORY_CONTROL);
					}
					
					if(involved.containsKey(region))
					{
						for(Player i : involved.get(region).keySet())
						{
							int inv = involved.get(region).get(i);
							
							if(g.getPlayerHandler().getFaction(i).equals(region.getFaction()))
							{
								pl.getNotificationController().dispatch(n, pl.getNotificationController().getMapChannel(), i);
								inv *= 3;
							}
							
							else
							{
								pl.getNotificationController().dispatch(n2, pl.getNotificationController().getMapChannel(), i);
								inv *= 1;
							}
							
							if(inv > 0)
							{
								pl.getExperienceController().giveExperience(i, inv, ExperienceType.TERRITORY_INFLUENCE);
							}
						}
						
						involved.remove(region);
					}
				}
				
				g.getMap().handleUncapturableRegions();
			}
			
			else
			{
				Notification n = new Notification();
				n.setPriority(NotificationPriority.HIGH);
				
				if(region.isCaptureable())
				{
					String t = "";
					String cc = "";
					Duration d = new Duration(region.getTimer());
					
					t = d.getMinutes() + ":" + d.getSeconds();
					int cz = -1;
					
					for(Capture i : captures)
					{
						cz++;
						cc = cc + i.getSecured().getColor() + "[" + Info.abc[cz] + "]  ";
					}
					
					String s = strongest(controlCount).getColor() + region.getName() + ChatColor.AQUA + " in " + ChatColor.GREEN + t + " " + cc;
					
					n.setSubSubTitle(s);
					
					for(Player i : region.getPlayers())
					{
						if(inCap.contains(i))
						{
							continue;
						}
						
						pl.getNotificationController().dispatch(n, pl.getNotificationController().getMapChannel(), i);
					}
				}
			}
		}
	}
	
	public void handleCapture(Capture capture)
	{
		GList<Player> p = capture.getPlayers().copy();
		
		for(Player i : capture.getPlayers())
		{
			PlayerControlEvent e = new PlayerControlEvent(g, capture, i);
						
			pl.callEvent(e);
			
			if(e.isCancelled())
			{
				p.remove(i);
			}
		}
		
		for(Player i : p)
		{
			if(!involved.containsKey(capture.getRegion()))
			{
				involved.put(capture.getRegion(), new GMap<Player, Integer>());
			}
			
			if(!involved.get(capture.getRegion()).containsKey(i))
			{
				involved.get(capture.getRegion()).put(i, 0);
			}
			
			involved.get(capture.getRegion()).put(i, involved.get(capture.getRegion()).get(i) + 1);
		}
		
		GMap<Faction, GList<Player>> map = g.getPlayerHandler().getFaction(p).flip();
		Integer size = map.size();
		
		for(Faction i : map.keySet())
		{
			for(Player j : map.get(i))
			{
				if(pl.getCommandController().getGods().contains(j))
				{
					capture.reset(i);
				}
			}
		}
		
		if(size == 1)
		{
			Faction f = map.ukeys().get(0);
			
			if(capture.getSecured().equals(f))
			{
				capture.defend();
				
				if(capture.getProgress() == 100)
				{
					capture.reset(f);
				}
			}
			
			else
			{
				if(capture.getOffense() == null)
				{
					capture.offend();
					capture.setOffense(f);
					
					if(capture.getProgress() == 0)
					{
						capture.reset(f);
					}
				}
				
				else if(!capture.getOffense().equals(f))
				{
					capture.defend();
					
					if(capture.getProgress() == 100)
					{
						capture.reset();
					}
				}
				
				else
				{
					capture.offend();
					capture.setOffense(f);
					
					if(capture.getProgress() == 0)
					{
						capture.reset(f);
					}
				}
			}
		}
		
		else if(size == 2)
		{
			if(g.getPlayerHandler().contested(map))
			{
				
			}
			
			else
			{
				if(capture.getSecured().equals(map.ukeys().get(0)) || capture.getSecured().equals(map.ukeys().get(1)))
				{
					Faction f = g.getPlayerHandler().strongest(map);
					
					if(capture.getSecured().equals(f))
					{
						capture.defend();
						
						if(capture.getProgress() == 100)
						{
							capture.reset(f);
						}
					}
					
					else
					{
						if(capture.getOffense() == null)
						{
							capture.offend();
							capture.setOffense(f);
							
							if(capture.getProgress() == 0)
							{
								capture.reset(f);
							}
						}
						
						else if(!capture.getOffense().equals(f))
						{
							capture.defend();
							
							if(capture.getProgress() == 100)
							{
								capture.reset();
							}
						}
						
						else
						{
							capture.offend();
							capture.setOffense(f);
							
							if(capture.getProgress() == 0)
							{
								capture.reset(f);
							}
						}
					}
				}
				
				else
				{
					Faction f = g.getPlayerHandler().strongest(map);
					
					if(capture.getOffense() == null)
					{
						capture.offend();
						capture.setOffense(f);
						
						if(capture.getProgress() == 0)
						{
							capture.reset(f);
						}
					}
					
					else if(!capture.getOffense().equals(f))
					{
						capture.defend();
						
						if(capture.getProgress() == 100)
						{
							capture.reset();
						}
					}
					
					else
					{
						capture.offend();
						capture.setOffense(f);
						
						if(capture.getProgress() == 0)
						{
							capture.reset(f);
						}
					}
				}
			}
		}
		
		else if(size == 3)
		{
			if(g.getPlayerHandler().contested(map))
			{
				
			}
			
			else
			{
				Faction f = g.getPlayerHandler().strongest(map);
				
				if(capture.getSecured().equals(f))
				{
					capture.defend();
					
					if(capture.getProgress() == 100)
					{
						capture.reset(f);
					}
				}
				
				else
				{
					if(capture.getOffense() == null)
					{
						capture.offend();
						capture.setOffense(f);
						
						if(capture.getProgress() == 0)
						{
							capture.reset(f);
						}
					}
					
					else if(!capture.getOffense().equals(f))
					{
						capture.defend();
						
						if(capture.getProgress() == 100)
						{
							capture.reset();
						}
					}
					
					else
					{
						capture.offend();
						capture.setOffense(f);
						
						if(capture.getProgress() == 0)
						{
							capture.reset(f);
						}
					}
				}
			}
		}
		
		captureNotify(capture);
	}
	
	public void captureNotify(Capture capture)
	{
		GMap<Faction, GList<Player>> map = g.getPlayerHandler().getFaction(capture.getPlayers()).flip();
		
		for(Faction i : map.keySet())
		{
			Notification n = new Notification().setSubTitle(captureGraph(capture));
			
			if(g.getPlayerHandler().contested(map))
			{
				n.setSubSubTitle(ChatColor.RED + "CONTESTED");
			}
			
			else
			{
				if(capture.getProgress() == 100)
				{
					if(capture.getSecured().equals(i))
					{
						n.setSubSubTitle(i.getColor() + "Secured");
					}
					
					else
					{
						continue;
					}
				}
				
				else if(g.getPlayerHandler().strongest(map).equals(i))
				{
					n.setSubSubTitle(i.getColor() + "Capturing");
				}
				
				else
				{
					n.setSubSubTitle(g.getPlayerHandler().strongest(map).getColor() + "Losing Control");
				}
			}
			
			n.setFadeIn(0);
			n.setFadeOut(10);
			n.setStayTime(20);
			n.setOngoing(true);
			n.setPriority(NotificationPriority.HIGH);
						
			for(Player j : g.getPlayerHandler().getFaction(capture.getPlayers()).keySet())
			{
				if(g.getPlayerHandler().getFaction(j).equals(i))
				{
					pl.getNotificationController().dispatch(n, pl.getNotificationController().getCapChannel(), j);
					
					if(!inCap.contains(j))
					{
						inCap.add(j);
					}
				}
			}
		}
	}
	
	public String captureGraph(Capture c)
	{
		String s = "";
		
		if(c.getOffense() == null)
		{
			s = s + c.getSecured().getColor() + ChatColor.STRIKETHROUGH;
			
			for(int i = 0; i < 20; i++)
			{
				s = s + " ";
			}
		}
		
		else
		{
			s = s + c.getOffense().getColor() + ChatColor.STRIKETHROUGH;
			
			for(int i = 0; i < 20 - (c.getProgress() / 5); i++)
			{
				s = s + " ";
			}
			
			s = s + c.getSecured().getColor() + ChatColor.STRIKETHROUGH;
			
			for(int i = 0; i < (c.getProgress() / 5); i++)
			{
				s = s + " ";
			}
		}
		
		return s;
	}
	
	@EventHandler
	public void onPlayer(EntityPortalEnterEvent e)
	{
		if(e.getEntityType().equals(EntityType.ARROW))
		{
			Arrow a = (Arrow) e.getEntity();
			Block b = a.getLocation().getBlock();
			
			for(Region i : g.getMap().getRegions())
			{
				if(i.contains(b))
				{
					if(a.getShooter() instanceof Player)
					{
						Player p = (Player) a.getShooter();
						
						if(!g.getPlayerHandler().getFaction(p).equals(i.getFaction()))
						{
							e.getLocation().getWorld().createExplosion(b.getLocation(), 0f);
							a.remove();
						}
					}
					
					return;
				}
			}
		}
	}
}
