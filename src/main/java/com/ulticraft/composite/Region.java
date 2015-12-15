package com.ulticraft.composite;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.Colorable;
import com.ulticraft.uapi.ColorUtils;
import com.ulticraft.uapi.UList;

public class Region
{
	private Faction dominatingFaction;
	private CaptureArray captures;
	private Poly area;
	private UList<GridLocation> gridLocations;
	private UList<Block> accents;
	
	public Region()
	{
		
	}
	
	public void build(World world)
	{
		gridLocations = new UList<GridLocation>();
		accents = new UList<Block>();
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
		
		for(GridLocation i : gridLocations)
		{
			for(int j = 0; j < 257; j++)
			{
				Block b = i.toLocation(world, j).getBlock();
				
				if(b.getType().equals(Material.WOOL) || b.getType().equals(Material.STAINED_GLASS) || b.getType().equals(Material.STAINED_GLASS_PANE) || b.getType().equals(Material.STAINED_CLAY) || b.getType().equals(Material.BANNER))
				{
					accents.add(b);
				}
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
		
		DyeColor c = ColorUtils.chatToDye(dominatingFaction.getPrimary());
		
		for(Block i : accents)
		{
			Colorable cl = ((Colorable) i.getState().getData());
            cl.setColor(c);
		}
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
