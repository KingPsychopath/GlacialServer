package com.ulticraft.composite;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.entity.Player;

public class Manipulation
{
	private Location location;
	private Material material;
	private Player player;
	private DyeColor dye;
	
	public Manipulation(Location location, Material material)
	{
		this.location = location;
		this.material = material;
	}

	public Manipulation(Location location, Material material, Player player)
	{
		this.location = location;
		this.material = material;
		this.player = player;
	}

	public Manipulation(Location location, DyeColor dye)
	{
		this.location = location;
		this.dye = dye;
	}

	@SuppressWarnings("deprecation")
	public void manipulate()
	{
		if(material != null)
		{
			if(player != null)
			{
				player.sendBlockChange(location, material, (byte) 0);
			}
			
			else
			{
				location.getBlock().setType(material);
			}
		}
		
		if(dye != null)
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
}
