package com.ulticraft.composite;

import org.bukkit.Location;

public class Manipulation
{
	protected Location location;
	
	public Manipulation(Location location)
	{
		this.location = location;
	}

	public Location getLocation()
	{
		return location;
	}

	public void setLocation(Location location)
	{
		this.location = location;
	}
	
	public void manipulate()
	{
		
	}
}
