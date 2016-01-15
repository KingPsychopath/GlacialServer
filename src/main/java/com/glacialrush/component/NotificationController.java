package com.glacialrush.component;

import org.bukkit.entity.Player;
import com.glacialrush.api.GlacialPlugin;
import com.glacialrush.api.dispatch.notification.Notification;
import com.glacialrush.api.dispatch.notification.NotificationChannel;
import com.glacialrush.api.dispatch.notification.NotificationPriority;
import com.glacialrush.api.dispatch.notification.NotificationSystem;

public class NotificationController extends NotificationSystem
{
	protected NotificationChannel mapChannel;
	protected NotificationChannel capChannel;
	protected NotificationChannel devChannel;
	protected NotificationChannel gameChannel;
	protected NotificationChannel broadChannel;
	
	public NotificationController(GlacialPlugin pl)
	{
		super(pl);
		
		this.capChannel = new NotificationChannel("cap", NotificationPriority.MEDIUM);
		this.mapChannel = new NotificationChannel("map", NotificationPriority.LOW);
		this.devChannel = new NotificationChannel("dev", NotificationPriority.VERYLOW);
		this.gameChannel = new NotificationChannel("game", NotificationPriority.HIGH);
		this.broadChannel = new NotificationChannel("broad", NotificationPriority.VERYHIGH);
		
		channels.add(capChannel);
		channels.add(mapChannel);
		channels.add(devChannel);
		channels.add(gameChannel);
		channels.add(broadChannel);
	}
	
	public void dispatch(Notification n, Player p)
	{
		defaultChannel.dispatch(n, p);
	}
	
	public void dispatch(Notification n, NotificationChannel c, Player p)
	{
		if(n.getOngoing())
		{
			if(cChannel.containsKey(p))
			{
				if(cChannel.get(p).getLevel() > c.getPriority().getLevel())
				{
					return;
				}
			}
		}
		
		c.dispatch(n, p);
	}
	
	public void dispatch(Notification n)
	{
		for(Player i : pl.onlinePlayers())
		{
			dispatch(n, i);
		}
	}
	
	public void dispatch(Notification n, NotificationChannel c)
	{
		for(Player i : pl.onlinePlayers())
		{
			dispatch(n, c, i);
		}
	}

	public NotificationChannel getMapChannel()
	{
		return mapChannel;
	}

	public NotificationChannel getCapChannel()
	{
		return capChannel;
	}

	public NotificationChannel getDevChannel()
	{
		return devChannel;
	}

	public NotificationChannel getGameChannel()
	{
		return gameChannel;
	}

	public NotificationChannel getBroadChannel()
	{
		return broadChannel;
	}
}
