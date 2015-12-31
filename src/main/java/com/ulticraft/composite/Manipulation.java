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
	private DyeColor color;
	
	public Manipulation(Location location, DyeColor color)
	{
		this.location = location;
		this.color = color;
	}
	
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
	
	public Location getLocation()
	{
		return location;
	}
	
	public void setLocation(Location location)
	{
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
	
	public Player getPlayer()
	{
		return player;
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	public DyeColor getColor()
	{
		return color;
	}
	
	public void setColor(DyeColor color)
	{
		this.color = color;
	}
	
	@SuppressWarnings("deprecation")
	public void exec()
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
		
		else if(color != null)
		{
			if(location.getBlock().getType().equals(Material.BANNER) || location.getBlock().getType().equals(Material.WALL_BANNER) || location.getBlock().getType().equals(Material.STANDING_BANNER))
			{
				Banner banner = (Banner) location.getBlock().getState();
				banner.setBaseColor(color);
				banner.update();
				
				return;
			}
			
			location.getBlock().setData(color.getData());
		}
	}
}
