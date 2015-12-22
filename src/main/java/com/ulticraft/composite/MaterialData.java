package com.ulticraft.composite;

import org.bukkit.Location;
import org.bukkit.Material;

public class MaterialData
{
	private Material material;
	private Byte data;
	private Location location;
	
	public MaterialData(Material material, Byte data, Location location)
	{
		this.material = material;
		this.data = data;
		this.location = location;
	}
	
	public Material getMaterial()
	{
		return material;
	}
	
	public void setMaterial(Material material)
	{
		this.material = material;
	}
	
	public Byte getData()
	{
		return data;
	}
	
	public void setData(Byte data)
	{
		this.data = data;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public void setLocation(Location location)
	{
		this.location = location;
	}
}
