package com.ulticraft.map;

import java.io.Serializable;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.ulticraft.GlacialRush;
import com.ulticraft.faction.Faction;
import com.ulticraft.uapi.UList;

public class Map implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected String world;
	protected UList<Region> regions;
	protected GlacialRush pl;
	
	public Map(GlacialRush pl, String name, World world)
	{
		this.name = name;
		this.world = world.getName();
		this.pl = pl;
		
		regions = new UList<Region>();
	}
	
	public void outline(Player player)
	{
		for(Region i : regions)
		{
			i.outline(player);
		}
	}
	
	public void addRegions(Chunk chunk, int xx, int zz)
	{
		World world = chunk.getWorld();
		
		for(int x = 0; x < 3 * xx; x += 3)
		{
			for(int z = 0; z < 3 * zz; z += 3)
			{
				addRegion(world.getChunkAt(chunk.getX() + x, chunk.getZ() + z));
			}
		}
	}
	
	public void addRegion(Chunk chunk)
	{
		regions.add(new Region(pl, "Unknown Name", chunk));
	}
	
	public void addRegion(Location location)
	{
		regions.add(new Region(pl, "Unknown Name", location.getChunk()));
	}
	
	public void reset()
	{
		for(Region i : regions)
		{
			i.reset();
		}
	}
	
	public void reset(Faction f)
	{
		for(Region i : regions)
		{
			i.reset(f);
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

	public UList<Region> getRegions()
	{
		return regions;
	}

	public void setRegions(UList<Region> regions)
	{
		this.regions = regions;
	}
}
