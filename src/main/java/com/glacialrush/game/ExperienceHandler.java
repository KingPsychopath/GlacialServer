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
import com.glacialrush.game.event.FactionRegionCaptureEvent;
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
		n.setFadeOut(20);
		n.setStayTime(5);
		n.setSound(new GSound("g.event.experience.earn", 0.5f, 1f));
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
		n.setFadeOut(40);
		n.setStayTime(30);
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
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEvent(FactionRegionCaptureEvent e)
	{
		for(Player i : e.getRegion().getPlayers())
		{
			if(g.getPlayerHandler().getFaction(i).equals(e.getFaction()))
			{
				pl.getExperienceController().giveExperience(i, Info.EXPERIENCE_REGION_CAPTURE, ExperienceType.TERRITORY_CONTROL);
				new GSound("g.event.region.capture", 1f, 1f).play(i);
			}
		}
	}
}
