package com.ulticraft.composite;

import java.io.Serializable;
import com.ulticraft.map.Map;
import com.ulticraft.map.Region;
import com.ulticraft.uapi.UList;

public class MapData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String world;
	private int x;
	private int z;
	private int w;
	private int h;
	private UList<RegionData> regions;
	
	public MapData(Map map)
	{
		name = map.getName();
		world = map.getWorld();
		regions = new UList<RegionData>();
		x = map.getX();
		z = map.getZ();
		w = map.getW();
		h = map.getH();
		
		for(Region i : map.getRegions())
		{
			regions.add(new RegionData(i));
		}
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getWorld()
	{
		return world;
	}

	public void setWorld(String world)
	{
		this.world = world;
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

	public int getW()
	{
		return w;
	}

	public void setW(int w)
	{
		this.w = w;
	}

	public int getH()
	{
		return h;
	}

	public void setH(int h)
	{
		this.h = h;
	}

	public UList<RegionData> getRegions()
	{
		return regions;
	}

	public void setRegions(UList<RegionData> regions)
	{
		this.regions = regions;
	}
}
