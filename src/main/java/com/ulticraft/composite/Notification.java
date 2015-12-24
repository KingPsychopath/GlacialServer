package com.ulticraft.composite;

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
	private Float volume;
	private Float pitch;
	
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
		
		if(title != null || subTitle != null || actionTitle != null)
		{
			t.send(p);
		}
		
		if(sound != null)
		{
			float vl = volume == null ? 1f : volume;
			float pt = pitch == null ? 1f : pitch;
			
			p.playSound(p.getLocation(), sound, vl, pt);
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

	public Float getVolume()
	{
		return volume;
	}

	public Notification setVolume(Float volume)
	{
		this.volume = volume;
		return this;
	}

	public Float getPitch()
	{
		return pitch;
	}

	public Notification setPitch(Float pitch)
	{
		this.pitch = pitch;
		return this;
	}
}
