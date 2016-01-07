package com.glacialrush.composite;

import java.io.Serializable;
import com.glacialrush.GlacialServer;
import com.glacialrush.xapi.UList;
import com.glacialrush.xapi.ULocation;

public class MapData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String world;
	private boolean ready;
	private UList<RegionData> regionData;
	
	public MapData(String name, String world, boolean ready)
	{
		this.name = name;
		this.world = world;
		this.ready = ready;
		this.regionData = new UList<RegionData>();
	}
	
	public Map toMap(GlacialServer pl)
	{
		Map map = new Map(pl, name, pl.getServer().getWorld(world));
		
		for(RegionData i : regionData)
		{
			map.addRegion(new Hunk(i.getHx(), i.getHz(), pl.getServer().getWorld(world)));
			map.getRegion(new Hunk(i.getHx(), i.getHz(), pl.getServer().getWorld(world))).setName(i.getName());
			map.getRegion(new Hunk(i.getHx(), i.getHz(), pl.getServer().getWorld(world))).setSpawn(i.getSpawn().toLocation());
		}
		
		map.setReady(ready);
		
		return map;
	}
	
	public static MapData fromMap(Map map)
	{
		MapData md = new MapData(map.getName(), map.getWorld().getName(), map.getReady());
		
		for(Region i : map.getRegions())
		{
			md.addRegion(i);
		}
		
		return md;
	}
	
	public void addRegion(Region region)
	{
		regionData.add(new RegionData(region.getName(), new ULocation(region.getSpawn()), region.getHunk().getX(), region.getHunk().getZ()));
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
	
	public UList<RegionData> getRegionData()
	{
		return regionData;
	}
	
	public void setRegionData(UList<RegionData> regionData)
	{
		this.regionData = regionData;
	}

	public boolean isReady()
	{
		return ready;
	}

	public void setReady(boolean ready)
	{
		this.ready = ready;
	}
}
