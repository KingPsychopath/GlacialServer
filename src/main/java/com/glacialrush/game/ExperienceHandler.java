package com.glacialrush.game;

import java.text.NumberFormat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import com.glacialrush.GlacialServer;
import com.glacialrush.Info;
import com.glacialrush.api.dispatch.notification.Notification;
import com.glacialrush.api.dispatch.notification.NotificationPriority;
import com.glacialrush.api.object.GSound;
import com.glacialrush.game.event.BattleRankUpEvent;
import com.glacialrush.game.event.ExperienceEvent;
import com.glacialrush.game.event.ExperienceType;
import com.glacialrush.game.event.FactionCaptureEvent;
import net.md_5.bungee.api.ChatColor;

public class ExperienceHandler extends GlacialHandler
{
	public ExperienceHandler(GlacialServer pl)
	{
		super(pl);
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
		
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEvent(ExperienceEvent e)
	{
		Notification n = new Notification();
		n.setFadeIn(2);
		n.setFadeOut(10);
		n.setStayTime(5);
		Float m = 0.5f;
		
		if(e.getAmount() > 15)
		{
			m = 0.6f;
		}
		
		if(e.getAmount() > 20)
		{
			m = 0.7f;
		}
		
		if(e.getAmount() > 45)
		{
			m = 0.9f;
		}
		
		if(e.getAmount() > 100)
		{
			m = 1f;
		}
		
		if(e.getAmount() > 150)
		{
			m = 1.2f;
		}
		
		if(e.getAmount() > 350)
		{
			m = 1.4f;
		}
		
		if(e.getAmount() > 999)
		{
			m = 1.7f;
		}
		
		if(e.getAmount() > 299)
		{
			m = 1.9f;
		}
		
		n.setSound(new GSound("g.event.experience.earn", 0.5f, m));
		n.setSubTitle(ChatColor.AQUA + "+ " + e.getAmount() + " XP");
		n.setSubSubTitle(ChatColor.AQUA + e.getType().toString().replace('_', ' ') + " " + ChatColor.GREEN + "[+ " + (int)(100 * pl.getExperienceController().getExperienceBonus(e.getPlayer())) + "%]");
		n.setPriority(NotificationPriority.VERYHIGH);
		
		pl.getNotificationController().dispatch(n, pl.getNotificationController().getMapChannel(), e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEvent(BattleRankUpEvent e)
	{
		Notification n = new Notification();
		n.setFadeIn(10);
		n.setFadeOut(32);
		n.setStayTime(10);
		n.setSound(new GSound("g.event.experience.rankup", 1f, 1.2f));
		n.setTitle(ChatColor.AQUA + "Rank " + e.getLevel());
		n.setSubTitle(ChatColor.AQUA + "Total XP Earned: " + ChatColor.DARK_AQUA + NumberFormat.getInstance().format(pl.getExperienceController().getExperience(e.getPlayer())));
		n.setPriority(NotificationPriority.VERYHIGH);
		
		pl.getNotificationController().dispatch(n, pl.getNotificationController().getMapChannel(), e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEvent(FactionCaptureEvent e)
	{
		for(final Player i : e.getCapture().getPlayers())
		{
			if(g.getPlayerHandler().getFaction(i).equals(e.getFaction()))
			{
				new GSound("g.event.capture.capture", 0.5f, 1.5f).play(i);
				
				pl.scheduleSyncTask(20, new Runnable()
				{
					@Override
					public void run()
					{
						pl.getExperienceController().giveExperience(i, Info.EXPERIENCE_FACTION_CAPTURE, ExperienceType.POINT_CONTROL);
					}
				});
			}
		}
	}
}
