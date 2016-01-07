package com.glacialrush.game.component;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import com.glacialrush.api.dispatch.Title;
import com.glacialrush.composite.Capture;
import com.glacialrush.game.Game;
import com.glacialrush.game.GameComponent;
import com.glacialrush.game.Tickrement;
import com.glacialrush.game.Tickreval;
import com.glacialrush.game.event.FactionCaptureEvent;
import com.glacialrush.game.event.PlayerFactionChangedEvent;
import net.md_5.bungee.api.ChatColor;

@Tickrement(Tickreval.MINUTE)
public class PlayerHandler implements GameComponent, Listener
{
	@Override
	public void onTick(Game g)
	{
		
	}

	@Override
	public void onStart(Game g)
	{
		g.pl().register(this);
	}

	@Override
	public void onStop(Game g)
	{
		g.pl().unRegister(this);
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
		t.setTitle(c.getSecured().getColor() + c.getSecured().getName() + " Controlled");
		
		for(Player i : c.getPlayers())
		{
			t.send(i);
		}
	}
}
