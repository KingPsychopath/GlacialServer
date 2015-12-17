package com.ulticraft.region;

import org.bukkit.Chunk;
import org.bukkit.Location;
import com.ulticraft.exception.InvalidRegionPlacementException;
import com.ulticraft.uapi.UList;

public class Map
{
	private UList<Region> regions;
	
	public Map(Location center)
	{
		regions = new UList<Region>();		
	}
	
	public void addRegion(Location location) throws InvalidRegionPlacementException
	{
		Region r = new Region(location.getChunk());
		
		for(Region i : regions)
		{
			for(Chunk j : i.getChunks())
			{
				for(Chunk k : r.getChunks())
				{
					if(k.getX() == j.getX() && k.getZ() == j.getZ())
					{
						throw new InvalidRegionPlacementException("Region at " + location.getBlockX() + ", " + location.getBlockZ() + " Collision at " + k.getX() + ", " + k.getZ());
					}
				}
			}
		}
		
		regions.add(r);
	}
	
	public void addRegion(Chunk chunk) throws InvalidRegionPlacementException
	{
		addRegion(chunk.getBlock(7, 7, 7).getLocation());
	}
}
