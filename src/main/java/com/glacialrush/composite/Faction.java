package com.glacialrush.composite;

import java.io.Serializable;
import org.bukkit.DyeColor;
import com.glacialrush.xapi.ColorUtils;
import com.glacialrush.xapi.UList;
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
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
		{
			return false;
		}
		
		if(obj instanceof Faction)
		{
			Faction f = (Faction) obj;
			
			if(f.getColor().equals(getColor()) && f.getName().equals(getName()))
			{
				return true;
			}
		}
		
		return false;
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
	
	public static Faction random()
	{
		return new UList<Faction>().qadd(cryptic).qadd(enigma).qadd(omni).pickRandom();
	}

	public static UList<Faction> all()
	{
		return new UList<Faction>().qadd(cryptic).qadd(enigma).qadd(omni);
	}
}
