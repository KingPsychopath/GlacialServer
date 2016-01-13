package com.glacialrush.composite.data;

import java.io.Serializable;
import com.glacialrush.api.object.GList;
import com.glacialrush.composite.Map;
import com.glacialrush.composite.Region;
import com.glacialrush.xapi.ULocation;

public class RegionData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private ULocation spawn;
	private GList<ULocation> captures;
	private int x;
	private int z;
	
	public RegionData(String name, ULocation spawn, GList<ULocation> captures, int x, int z)
	{
		this.name = name;
		this.spawn = spawn;
		this.captures = captures;
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
	
	public ULocation getSpawn()
	{
		return spawn;
	}
	
	public void setSpawn(ULocation spawn)
	{
		this.spawn = spawn;
	}
	
	public GList<ULocation> getCaptures()
	{
		return captures;
	}
	
	public void setCaptures(GList<ULocation> captures)
	{
		this.captures = captures;
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
		return new Region(spawn.toLocation(), map);
	}
}
