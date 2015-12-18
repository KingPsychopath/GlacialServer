package com.ulticraft.data;

import org.bukkit.DyeColor;
import com.ulticraft.uapi.ColorUtils;
import net.md_5.bungee.api.ChatColor;

public class Faction
{
	private String name;
	private ChatColor color;
	private static Faction NEUTRAL = new Faction("Neutral", ChatColor.BLACK);
	
	public Faction(String name, ChatColor color)
	{
		this.name = name;
		this.color = color;
	}
	
	public static Faction neutral()
	{
		return NEUTRAL;
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
