package com.ulticraft.uapi;

import java.io.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;

public class UChunk implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int x;
	private int z;
	private String world;
	
	public UChunk(Chunk chunk)
	{
		x = chunk.getX();
		z = chunk.getZ();
		world = chunk.getWorld().getName();
	}
	
	public UChunk(int x, int z, String world)
	{
		this.x = x;
		this.z = z;
		this.world = world;
	}
	
	public Chunk toChunk()
	{
		if(Bukkit.getServer().getWorld(world) == null)
		{
			world = Bukkit.getServer().getWorlds().get(0).getName();
		}
		
		return Bukkit.getServer().getWorld(world).getChunkAt(x, z);
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getZ()
	{
		return z;
	}

	public void setZ(int z)
	{
		this.z = z;
	}

	public String getWorld()
	{
		return world;
	}

	public void setWorld(String world)
	{
		this.world = world;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
}
