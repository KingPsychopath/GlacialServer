package com.ulticraft.composite;

import org.bukkit.Location;

public class Capture
{
	private Location location;
	private Faction dominantFaction;
	
	public Capture(Location location)
	{
		this.location = location;
		this.dominantFaction = Faction.neutral();
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public void setLocation(Location location)
	{
		this.location = location;
	}
	
	public Faction getDominantFaction()
	{
		return dominantFaction;
	}
	
	public void setDominantFaction(Faction dominantFaction)
	{
		this.dominantFaction = dominantFaction;
	}
}
