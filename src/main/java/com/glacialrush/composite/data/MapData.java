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
	
	public MapData(String name, String world, GList<RegionData> regions)
	{
		this.name = name;
		this.regions = regions;
		this.world = world;
	}
	
	public Map toMap(GlacialServer pl)
	{
		Map map = new Map(pl, pl.getServer().getWorld(world), name);
		
		for(RegionData i : regions)
		{
			map.getRegions().add(i.toRegion(map));
		}
		
		return map;
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
