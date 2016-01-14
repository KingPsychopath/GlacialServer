package com.glacialrush.game;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
import com.glacialrush.game.event.PlayerControlEvent;
import com.glacialrush.xapi.Duration;
import net.md_5.bungee.api.ChatColor;

public class MapHandler extends GlacialHandler
{
	private int captureDelay;
	
	public MapHandler(GlacialServer pl)
	{
		super(pl);
		
		captureDelay = 0;
	}
	
	@Override
	public void start()
	{
	
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
		captureDelay++;
		
		if(captureDelay == 19)
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
	
	public void handleRegion(Region region)
	{
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
				region.startTimer(120);
			}
			
			region.decTimer();
			
			if(region.getTimer() == 0)
			{
				region.resetTimer();
				
				if(contested(region.getFaction(), controlCount))
				{
					region.setFaction(region.getFaction());
				}
				
				else
				{
					region.setFaction(strongest(controlCount));
					
					Notification n = new Notification().setPriority(NotificationPriority.HIGHEST);
					n.setTitle(region.getFaction().getColor() + "Territory Defended");
					n.setSubSubTitle(region.getFaction().getColor() + region.getFaction().getName() + " defended " + region.getName());
					n.setSound(new GSound(Sound.AMBIENCE_THUNDER, 1f, 0.5f));
					
					for(Player i : region.getPlayers())
					{
						pl.getNotificationController().dispatch(n, i);
					}
				}
			}
			
			else
			{
				Notification n = new Notification();
				n.setPriority(NotificationPriority.VERYHIGH);
				n.setDelay(false);
				
				String t = "";
				String cc = "";
				Duration d = new Duration(region.getTimer());
				
				t = d.getMinutes() + ":" + d.getSeconds();
				int cz = 0;
				
				for(Capture i : captures)
				{
					cz++;
					cc = i.getSecured().getColor() + "[" + Info.abc[cz] + "]  ";
				}
				
				String s = ChatColor.AQUA + region.getName() + " in " + ChatColor.GREEN + t + " " + cc;
				
				n.setSubSubTitle(s);
				
				for(Player i : region.getPlayers())
				{
					pl.getNotificationController().dispatch(n, i);
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
		
		GMap<Faction, GList<Player>> map = g.getPlayerHandler().getFaction(p).flip();
		Integer size = map.size();
		
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
				// TODO Contested
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
				// TODO Contested
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
						n.setSubSubTitle(g.getPlayerHandler().strongest(map).getColor() + "Enemy Secured");
					}
				}
				
				else if(g.getPlayerHandler().strongest(map).equals(i))
				{
					n.setSubSubTitle(i.getColor() + "Capturing...");
				}
				
				else
				{
					n.setSubSubTitle(g.getPlayerHandler().strongest(map).getColor() + "Losing Control...");
				}
			}
			
			n.setPriority(NotificationPriority.VERYHIGH);
			n.setDelay(false);
			
			for(Player j : map.get(i))
			{
				pl.getNotificationController().dispatch(n, j);
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
				s = s + "-";
			}
		}
		
		else
		{
			s = s + c.getOffense().getColor() + ChatColor.STRIKETHROUGH;
			
			for(int i = 0; i < 20 - (c.getProgress() / 5); i++)
			{
				s = s + "-";
			}
			
			s = s + c.getSecured().getColor() + ChatColor.STRIKETHROUGH;
			
			for(int i = 0; i < (c.getProgress() / 5); i++)
			{
				s = s + "-";
			}
		}
		
		return s;
	}
}
