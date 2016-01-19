package com.glacialrush.composite.data;

import java.io.Serializable;
import org.bukkit.Bukkit;
import org.bukkit.World;
import com.glacialrush.composite.Hunk;
import com.glacialrush.composite.Map;
import com.glacialrush.composite.Region;

public class RegionData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String world;
	private int x;
	private int z;
	
	public RegionData(String name, World world, int x, int z)
	{
		this.name = name;
		this.world = world.getName();
		this.x = x;
		this.z = z;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
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
	
	public Region toRegion(Map map)
	{
		Region r = new Region(new Hunk(x, z, Bukkit.getServer().getWorld(world)), map);
		r.setName(name);
		
		return r;
	}
}
