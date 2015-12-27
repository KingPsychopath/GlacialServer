package com.ulticraft.composite;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Region
{
	private Location br;
	private Location tl;
	private String name;
	
	public Region(Location br)
	{
		this.br = br;
		this.tl = br.clone().add(new Vector(48, 0, 48));
		this.name = "Unnamed";
	}
}
