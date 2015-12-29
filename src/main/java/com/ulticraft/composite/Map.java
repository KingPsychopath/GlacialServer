package com.ulticraft.composite;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import com.ulticraft.GlacialRush;
import com.ulticraft.composite.Hunk.HunkFace;
import com.ulticraft.uapi.FastMath;
import com.ulticraft.uapi.UList;
import com.ulticraft.uapi.UMap;

public class Map
{
	private String name;
	private World world;
	private UList<Region> regions;
	private GlacialRush pl;
	
	public Map(GlacialRush pl, String name, World world)
	{
		this.pl = pl;
		this.name = name;
		this.world = world;
		this.regions = new UList<Region>();
	}
	
	public boolean contains(Hunk hunk)
	{
		for(Region i : regions)
		{
			if(i.getHunk().equals(hunk))
			{
				return true;
			}
		}
		
		return false;
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
	
	public boolean contains(Player player)
	{
		return contains(player.getLocation());
	}
	
	public boolean contains(LivingEntity entity)
	{
		return contains(entity.getLocation());
	}
	
	public boolean addRegion(Hunk hunk)
	{
		if(hunk.getWorld().equals(world) && (regions.isEmpty() || hasNeighbors(hunk)))
		{
			regions.add(new Region(pl, this, name, hunk));
			return true;
		}
		
		return false;
	}
	
	public boolean addRegionNear(Player p, Location location)
	{
		if(location.getWorld().equals(world))
		{
			if(regions.isEmpty())
			{
				regions.add(new Region(pl, this, name, location));
				return true;
			}
			
			else
			{
				Region closest = getRegionNearby(location);
				
				if(closest != null)
				{
					UMap<HunkFace, Double> distances = new UMap<HunkFace, Double>();
					
					distances.put(HunkFace.NORTH, FastMath.distance2D(closest.getHunk().getRelative(HunkFace.NORTH).getCenter(0), location));
					distances.put(HunkFace.SOUTH, FastMath.distance2D(closest.getHunk().getRelative(HunkFace.SOUTH).getCenter(0), location));
					distances.put(HunkFace.EAST, FastMath.distance2D(closest.getHunk().getRelative(HunkFace.EAST).getCenter(0), location));
					distances.put(HunkFace.WEST, FastMath.distance2D(closest.getHunk().getRelative(HunkFace.WEST).getCenter(0), location));
					
					double smallest = Double.MAX_VALUE;
					Hunk hunk = null;
					
					for(HunkFace i : distances.keySet())
					{
						if(distances.get(i) < smallest)
						{
							smallest = distances.get(i);
							hunk = closest.getHunk().getRelative(i);
						}
					}
					
					if(getRegion(hunk) == null)
					{
						addRegion(hunk);
						getRegion(hunk).draw(p);
					}
				}
			}
		}
		
		return false;
	}
	
	public Region getRegionNearby(Location location)
	{
		double closest = Double.MAX_VALUE;
		Region cc = null;
		
		if(!location.getWorld().equals(world) || regions.isEmpty())
		{
			return null;
		}
		
		for(Region i : regions)
		{
			double dist = FastMath.distance2D(i.getHunk().getCenter(0), location);
			
			if(dist < closest)
			{
				closest = dist;
				cc = i;
			}
		}
		
		return cc;
	}
	
	public boolean hasNeighbors(Region region)
	{
		return hasNeighbors(region.getHunk());
	}
	
	public boolean hasNeighbors(Hunk hunk)
	{
		if(getRegion(hunk.getRelative(HunkFace.NORTH)) != null)
		{
			return true;
		}
		
		if(getRegion(hunk.getRelative(HunkFace.SOUTH)) != null)
		{
			return true;
		}
		
		if(getRegion(hunk.getRelative(HunkFace.EAST)) != null)
		{
			return true;
		}
		
		if(getRegion(hunk.getRelative(HunkFace.WEST)) != null)
		{
			return true;
		}
		
		return false;
	}
	
	public Region getRegion(Hunk hunk)
	{
		for(Region i : regions)
		{
			if(i.getHunk().equals(hunk))
			{
				return i;
			}
		}
		
		return null;
	}
}
