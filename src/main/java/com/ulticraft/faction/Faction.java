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
	private final static Faction omni = new Faction("Omni", ChatColor.DARK_PURPLE);
	private final static Faction enigma = new Faction("Enigma", ChatColor.RED);
	private final static Faction cryptic = new Faction("Cryptic", ChatColor.YELLOW);
	
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
	
	public static Faction omni()
	{
		return omni;
	}
	
	public static Faction enigma()
	{
		return enigma;
	}
	
	public static Faction cryptic()
	{
		return cryptic;
	}
}
