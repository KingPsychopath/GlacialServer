package com.ulticraft.data;

import org.bukkit.DyeColor;
import com.ulticraft.uapi.ColorUtils;
import net.md_5.bungee.api.ChatColor;

public class Faction
{
	private String name;
	private ChatColor color;
	
	public Faction(String name, ChatColor color)
	{
		this.name = name;
		this.color = color;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ChatColor getColor()
	{
		return color;
	}
	
	public DyeColor getDye()
	{
		return ColorUtils.chatToDye(color);
	}

	public void setColor(ChatColor color)
	{
		this.color = color;
	}
}
