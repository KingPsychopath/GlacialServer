package com.ulticraft.faction;

import java.io.Serializable;
import org.bukkit.DyeColor;
import com.ulticraft.uapi.ColorUtils;
import net.md_5.bungee.api.ChatColor;

public class Faction implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final String name;
	private final ChatColor color;
	private final DyeColor dyeColor;
	
	private final static Faction neutral = new Faction("Neutral", ChatColor.BLACK);
	
	public Faction(String name, ChatColor color)
	{
		this.name = name;
		this.color = color;
		this.dyeColor = ColorUtils.chatToDye(color);
	}

	public String getName()
	{
		return name;
	}

	public ChatColor getColor()
	{
		return color;
	}
	
	public DyeColor getDyeColor()
	{
		return dyeColor;
	}
	
	public static Faction neutral()
	{
		return neutral;
	}
}
