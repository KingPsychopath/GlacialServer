package com.ulticraft.composite;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;

public class ColorManipulation extends Manipulation
{
	protected DyeColor dye;
	
	public ColorManipulation(Location location, DyeColor dye)
	{
		super(location);
		
		this.dye = dye;
	}

	public DyeColor getDye()
	{
		return dye;
	}

	public void setDye(DyeColor dye)
	{
		this.dye = dye;
	}
	
	@SuppressWarnings("deprecation")
	public void manipulate()
	{
		if(location.getBlock().getType().equals(Material.BANNER) || location.getBlock().getType().equals(Material.WALL_BANNER) || location.getBlock().getType().equals(Material.STANDING_BANNER))
		{
			Banner banner = (Banner) location.getBlock().getState();
			banner.setBaseColor(dye);
			banner.update();
			
			return;
		}
		
		location.getBlock().setData(dye.getData());
	}
}
