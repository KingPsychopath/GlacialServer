package com.glacialrush.composite.data;

import java.io.Serializable;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.object.GList;
import com.glacialrush.composite.Map;

public class MapData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private GList<RegionData> regions;
	private String world;
	private Boolean locked;
	
	public MapData(String name, String world, GList<RegionData> regions, Boolean locked)
	{
		this.name = name;
		this.regions = regions;
		this.world = world;
		this.locked = locked;
	}
	
	public Map toMap(GlacialServer pl)
	{
		Map map = new Map(pl, pl.getServer().getWorld(world), name);
		
		for(RegionData i : regions)
		{
			map.getRegions().add(i.toRegion(map));
		}
		
		if(locked)
		{
			map.setLocked(true);
		}
		
		return map;
	}
	
	public String getWorld()
	{
		return world;
	}

	public void setWorld(String world)
	{
		this.world = world;
	}

	public Boolean getLocked()
	{
		return locked;
	}

	public void setLocked(Boolean locked)
	{
		this.locked = locked;
	}

	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public GList<RegionData> getRegions()
	{
		return regions;
	}
	
	public void setRegions(GList<RegionData> regions)
	{
		this.regions = regions;
	}
}
