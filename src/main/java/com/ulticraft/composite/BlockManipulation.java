package com.ulticraft.composite;

import org.bukkit.Location;
import org.bukkit.Material;

public class BlockManipulation extends Manipulation
{
	protected Material material;
	
	public BlockManipulation(Location location, Material material)
	{
		super(location);
		
		this.material = material;
	}

	public Material getMaterial()
	{
		return material;
	}

	public void setMaterial(Material material)
	{
		this.material = material;
	}
	
	public void manipulate()
	{
		location.getBlock().setType(material);
	}
}
