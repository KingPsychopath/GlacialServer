package com.ulticraft.component;

import org.bukkit.entity.Player;
import com.ulticraft.GlacialRush;
import com.ulticraft.composite.Notification;
import com.ulticraft.uapi.Component;
import com.ulticraft.uapi.UList;
import com.ulticraft.uapi.UMap;

public class NotificationComponent extends Component
{
	private UMap<Player, UList<Notification>> pending;
	
	public NotificationComponent(GlacialRush pl)
	{
		super(pl);
		pending = new UMap<Player, UList<Notification>>();
	}
	
	public void enable()
	{
		pl.scheduleSyncRepeatingTask(0, 30, new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : pending.keySet())
				{
					if(!pending.get(i).isEmpty())
					{
						pending.get(i).get(0).execute(i);
					}
				}
			}
		});
	}
	
	public void disable()
	{
		
	}
	
	public void addNotification(Player p, Notification notification)
	{
		pending.get(p).add(notification);
	}
}
