package com.ulticraft.region;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.ulticraft.data.Faction;
import com.ulticraft.exception.InvalidRegionPlacementException;
import com.ulticraft.uapi.UList;

public class Map
{
	private UList<Region> regions;
	private World world;
	private Location center;
	private String name;
	
	public Map(String name, Location center)
	{
		regions = new UList<Region>();
		this.name = name;
		this.center = center;
		this.world = center.getWorld();
	}
	
	public void showGrid(Player player, Material material, int height)
	{
		for(Region i : regions)
		{
			i.drawOutline(player, material, height);
		}
	}
	
	public boolean canCapture(Faction faction, Region region)
	{
		Region up = getRegion(world.getChunkAt(region.getCenterChunk().getX(), region.getCenterChunk().getZ() + 1));
		Region dn = getRegion(world.getChunkAt(region.getCenterChunk().getX(), region.getCenterChunk().getZ() - 1));
		Region ri = getRegion(world.getChunkAt(region.getCenterChunk().getX() + 1, region.getCenterChunk().getZ()));
		Region le = getRegion(world.getChunkAt(region.getCenterChunk().getX() - 1, region.getCenterChunk().getZ()));
		
		if(up != null && up.getFaction().equals(faction))
		{
			return true;
		}
		
		if(dn != null && dn.getFaction().equals(faction))
		{
			return true;
		}
		
		if(ri != null && ri.getFaction().equals(faction))
		{
			return true;
		}
		
		if(le != null && le.getFaction().equals(faction))
		{
			return true;
		}
		
		return false;
	}
	
	public void addRegions(Location location, int width, int depth)
	{
		for(int x = location.getChunk().getX(); x < (location.getChunk().getX()) + (width * 3); x += 3)
		{
			for(int y = location.getChunk().getZ(); y < (location.getChunk().getZ()) + (depth * 3); y += 3)
			{
				forceAddRegion(world.getChunkAt(x, y));
			}
		}
	}
	
	public Region getRegion(Location location)
	{
		for(Region i : regions)
		{
			if(i.contains(location))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public Region getRegion(Chunk chunk)
	{
		return getRegion(chunk.getBlock(7, 7, 7).getLocation());
	}
	
	public void neutralize()
	{
		for(Region i : regions)
		{
			i.neutralize();
		}
	}
	
	public boolean contains(Location location)
	{
		for(Region i : regions)
		{
			if(i.contains(location))
			{
				return true;
			}
		}
		
		return false;
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
	
	public void forceAddRegion(Chunk chunk)
	{
		Region r = new Region(chunk);
		
		regions.add(r);
	}
	
	public void addRegion(Chunk chunk) throws InvalidRegionPlacementException
	{
		addRegion(chunk.getBlock(7, 7, 7).getLocation());
	}

	public UList<Region> getRegions()
	{
		return regions;
	}

	public World getWorld()
	{
		return world;
	}

	public Location getCenter()
	{
		return center;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setRegions(UList<Region> regions)
	{
		this.regions = regions;
	}

	public void setWorld(World world)
	{
		this.world = world;
	}

	public void setCenter(Location center)
	{
		this.center = center;
	}
}
