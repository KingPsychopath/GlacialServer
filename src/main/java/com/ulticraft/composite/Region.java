package com.ulticraft.composite;

import org.bukkit.Location;
import org.bukkit.World;
import com.ulticraft.uapi.UList;

public class Region
{
	private Faction dominatingFaction;
	private CaptureArray captures;
	private Poly area;
	private UList<GridLocation> gridLocations;
	
	public Region()
	{
		
	}
	
	public void build()
	{
		gridLocations = new UList<GridLocation>();
		GridLocation gl = area.calculateCentroid();
		boolean complete = false;
		int rad = 1;
		
		if(!area.contains(gl))
		{
			return;
		}
		
		while(!complete)
		{
			boolean hit = false;
			GridLocation cursor = new GridLocation(gl.getX() + rad, gl.getY() + rad);
			
			if(area.contains(cursor))
			{
				hit = true;
				gridLocations.add(new GridLocation(cursor.getX(), cursor.getY()));
			}
			
			for(int i = 0; i < rad * 2; i++)
			{
				cursor.setY(cursor.getY() - 1);
				
				if(area.contains(cursor))
				{
					hit = true;
					gridLocations.add(new GridLocation(cursor.getX(), cursor.getY()));
				}
			}
			
			for(int i = 0; i < rad * 2; i++)
			{
				cursor.setX(cursor.getX() - 1);
				
				if(area.contains(cursor))
				{
					hit = true;
					gridLocations.add(new GridLocation(cursor.getX(), cursor.getY()));
				}
			}
			
			for(int i = 0; i < rad * 2; i++)
			{
				cursor.setY(cursor.getY() + 1);
				
				if(area.contains(cursor))
				{
					hit = true;
					gridLocations.add(new GridLocation(cursor.getX(), cursor.getY()));
				}
			}
			
			for(int i = 0; i < rad * 2; i++)
			{
				cursor.setX(cursor.getX() + 1);
				
				if(area.contains(cursor))
				{
					hit = true;
					gridLocations.add(new GridLocation(cursor.getX(), cursor.getY()));
				}
			}
			
			rad++;
			
			if(!hit)
			{
				gridLocations.removeDuplicates();
				
				break;
			}
		}
	}
	
	public Location getCenter(World world, int height)
	{
		return area.calculateCentroid().toLocation(world, height);
	}
	
	public boolean contains(Location location)
	{
		return area.contains(new GridLocation(location));
	}

	public Faction getDominatingFaction()
	{
		return dominatingFaction;
	}

	public void setDominatingFaction(Faction dominatingFaction)
	{
		this.dominatingFaction = dominatingFaction;
	}

	public CaptureArray getCaptures()
	{
		return captures;
	}

	public void setCaptures(CaptureArray captures)
	{
		this.captures = captures;
	}

	public Poly getLocation()
	{
		return area;
	}

	public void setLocation(Poly area)
	{
		this.area = area;
	}
}
