package com.ulticraft.composite;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BlockPacketManipulation extends Manipulation
{
	protected Material material;
	protected Player player;
	
	public BlockPacketManipulation(Location location, Material material, Player player)
	{
		super(location);
		
		this.material = material;
		this.player = player;
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

	@SuppressWarnings("deprecation")
	public void manipulate()
	{
		player.sendBlockChange(location, material, (byte) 0);
	}
}
