package com.glacialrush.game.component;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import com.glacialrush.api.dispatch.Title;
import com.glacialrush.api.object.GMap;
import com.glacialrush.composite.Capture;
import com.glacialrush.game.Game;
import com.glacialrush.game.GameComponent;
import com.glacialrush.game.Tickrement;
import com.glacialrush.game.Tickreval;
import com.glacialrush.game.event.FactionCaptureEvent;
import com.glacialrush.game.event.PlayerFactionChangedEvent;
import net.md_5.bungee.api.ChatColor;

@Tickrement(Tickreval.SECOND)
public class PlayerHandler implements GameComponent, Listener
{
	private int task = 0;
	private GMap<Player, Integer> timings;
	
	@Override
	public void onTick(Game g)
	{
		
	}
	
	@Override
	public void onStart(Game g)
	{
		g.pl().register(this);
		
		timings = new GMap<Player, Integer>();
		
		task = g.pl().scheduleSyncRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				
			}
		});
	}
	
	@Override
	public void onStop(Game g)
	{
		g.pl().unRegister(this);
		g.pl().cancelTask(task);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onGlacialEvent(PlayerFactionChangedEvent e)
	{
		e.getPlayer().sendMessage(e.getFaction().getColor() + "You now fight with " + e.getFaction().getName());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onGlacialEvent(FactionCaptureEvent e)
	{
		Title t = new Title();
		Capture c = e.getCapturePoint();
		Integer ms = (c.getState() + 100) / 10;
		Double pc = new Double(c.getState() + 100) / 2;
		
		pc = (double)pc / (double)100;
		
		String m = "";
		
		m = m + c.getDefense().getColor() + ChatColor.STRIKETHROUGH;
		
		for(int i = 0; i < ms; i++)
		{
			m = m + "-";
		}
		
		m = m + c.getOffense().getColor() + ChatColor.STRIKETHROUGH;
				
		for(int i = 0; i < 20 - ms; i++)
		{
			m = m + "-";
		}
		
		t.setFadeInTime(0);
		t.setStayTime(10);
		t.setFadeOutTime(20);
		t.setSubtitle(m);
		
		for(Player i : c.getPlayers())
		{
			if(timings.containsKey(i))
			{
				timings.put(i, timings.get(i) + 1);
			}
			
			else
			{
				timings.put(i, 0);
			}
			
			if(timings.get(i) > 5)
			{
				timings.put(i, 0);
				e.getRegion().getGame().pl().getServer().dispatchCommand(e.getRegion().getGame().pl().getServer().getConsoleSender(), "playsound " + "g.event.capture.hum " + i.getName() + " " + e.getCapturePoint().getLocation().getBlockX() + " " + e.getCapturePoint().getLocation().getBlockY() + " " + e.getCapturePoint().getLocation().getBlockZ() + " 1.0 " + new Double(2.0 - ((2 * pc) + 0.1)));
			}
			
			c.getRegion().setPlayerCapturePane(i, t);
		}
	}
}
