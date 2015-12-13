package com.ulticraft.composite;

import net.md_5.bungee.api.ChatColor;

public class Faction
{
	protected final String name;
	protected final ChatColor primary;
	protected final ChatColor secondary;
	
	public Faction(String name, ChatColor primary, ChatColor secondary)
	{
		this.name = name;
		this.primary = primary;
		this.secondary = secondary;
	}
	
	public String getName()
	{
		return name;
	}

	public ChatColor getPrimary()
	{
		return primary;
	}

	public ChatColor getSecondary()
	{
		return secondary;
	}
}
