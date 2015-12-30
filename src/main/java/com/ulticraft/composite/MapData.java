package com.ulticraft.composite;

import java.io.Serializable;
import com.ulticraft.GlacialRush;
import com.ulticraft.uapi.UList;

public class MapData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String world;
	private UList<RegionData> regionData;
	
	public MapData(String name, String world)
	{
		this.name = name;
		this.world = world;
		this.regionData = new UList<RegionData>();
	}
	
	public Map toMap(GlacialRush pl)
	{
		Map map = new Map(pl, name, pl.getServer().getWorld(world));
		
		for(RegionData i : regionData)
		{
			map.addRegion(new Hunk(i.getHx(), i.getHz(), pl.getServer().getWorld(world)));
			map.getRegion(new Hunk(i.getHx(), i.getHz(), pl.getServer().getWorld(world))).setName(i.getName());
		}
		
		return map;
	}
	
	public static MapData fromMap(Map map)
	{
		MapData md = new MapData(map.getName(), map.getWorld().getName());
		
		for(Region i : map.getRegions())
		{
			md.addRegion(i);
		}
		
		return md;
	}
	
	public void addRegion(Region region)
	{
		regionData.add(new RegionData(region.getName(), region.getHunk().getX(), region.getHunk().getZ()));
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
}
