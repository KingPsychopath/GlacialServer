package com.ulticraft.composite;

import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import com.ulticraft.uapi.Title;

public class Notification
{
	private String message;
	private String title;
	private String subTitle;
	private String actionTitle;
	private Sound sound;
	
	public Notification()
	{
		
	}
	
	public void execute(Player p)
	{
		if(message != null)
		{
			p.sendMessage(message);
		}
		
		Title t = new Title();
		
		if(title != null)
		{
			t.setTitle(title);
		}
		
		if(subTitle != null)
		{
			t.setSubtitle(subTitle);
		}
		
		if(actionTitle != null)
		{
			t.setSubSubTitle(actionTitle);
		}
	}

	public String getMessage()
	{
		return message;
	}

	public Notification setMessage(String message)
	{
		this.message = message;
		return this;
	}

	public String getTitle()
	{
		return title;
	}

	public Notification setTitle(String title)
	{
		this.title = title;
		return this;
	}

	public String getSubTitle()
	{
		return subTitle;
	}

	public Notification setSubTitle(String subTitle)
	{
		this.subTitle = subTitle;
		return this;
	}

	public String getActionTitle()
	{
		return actionTitle;
	}

	public Notification setActionTitle(String actionTitle)
	{
		this.actionTitle = actionTitle;
		return this;
	}

	public Sound getSound()
	{
		return sound;
	}

	public Notification setSound(Sound sound)
	{
		this.sound = sound;
		return this;
	}
}
