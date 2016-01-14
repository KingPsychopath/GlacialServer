package com.glacialrush.component;

import java.util.Iterator;
import org.bukkit.entity.Player;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.component.Controller;
import com.glacialrush.api.dispatch.notification.Notification;
import com.glacialrush.api.dispatch.notification.NotificationPriority;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.object.GMap;
import com.glacialrush.api.thread.GlacialTask;

public class NotificationController extends Controller
{
	private GMap<Player, GList<Notification>> pending;
	private GMap<Player, Integer> cooldown;
	
	public NotificationController(GlacialServer pl)
	{
		super(pl);
		
		pending = new GMap<Player, GList<Notification>>();
		cooldown = new GMap<Player, Integer>();
	}
	
	public void preEnable()
	{
		super.preEnable();
	}
	
	public void postEnable()
	{
		super.postEnable();
		
		pl.newThread(new GlacialTask()
		{
			@Override
			public void run()
			{
				for(Player i : pl.onlinePlayers())
				{
					if(!pending.containsKey(i))
					{
						pending.put(i, new GList<Notification>());
						cooldown.put(i, 100);
					}
					
					if(cooldown.get(i) > 0)
					{
						cooldown.put(i, cooldown.get(i) - 1);
						continue;
					}
					
					for(NotificationPriority j : NotificationPriority.topDown())
					{
						boolean sh = false;
						
						Iterator<Notification> it = pending.get(i).iterator();
						
						while(it.hasNext())
						{
							Notification n = it.next();
							
							if(n.getPriority().equals(j))
							{
								n.show(i);
								sh = true;
								
								if(n.isDelay())
								{
									cooldown.put(i, 50);
								}
								
								it.remove();
								
								break;
							}
						}
						
						if(sh)
						{
							break;
						}
					}
				}
			}
		});
	}
	
	public void dispatch(Notification n)
	{
		for(Player i : pl.onlinePlayers())
		{
			dispatch(n, i);
		}
	}
	
	public void dispatch(Notification n, Player p)
	{
		if(!pending.containsKey(p))
		{
			pending.put(p, new GList<Notification>());
			cooldown.put(p, 100);
		}
		
		pending.get(p).add(n);
	}
	
	public void preDisable()
	{
		super.preDisable();
	}
	
	public void postDisable()
	{
		super.postDisable();
	}
}
