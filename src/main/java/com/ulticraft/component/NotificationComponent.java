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
	}
	
	public void enable()
	{

	}
	
	public void disable()
	{
		
	}
}
