package com.glacialrush.composite;

import org.bukkit.World;
import org.bukkit.entity.Player;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.object.GList;

public class Map
{
	protected World world;
	protected Faction faction;
	protected String name;
	protected GList<Region> regions;
	protected GlacialServer pl;
	
	public Map(GlacialServer pl, World world, String name)
	{
		this.name = name;
		this.regions = new GList<Region>();
		this.world = world;
		this.pl = pl;
		this.faction = Faction.neutral();
	}
	
	public void accent(Faction faction)
	{
		for(Region i : regions)
		{
			i.setFaction(faction);
			i.accent();
		}
	}
	
	public void build()
	{
		for(Region i : regions)
		{
			i.build();
		}
	}
	
	public void draw(Player p)
	{
		for(Region i : regions)
		{
			i.draw(p);
		}
	}
	
	public Region getRegion(Hunk hunk)
	{
		for(Region i : regions)
		{
			if(i.x == hunk.x && i.z == hunk.z)
			{
				return i;
			}
		}
		
		return null;
	}
	
	public boolean hasRegion(Hunk hunk)
	{
		return getRegion(hunk) == null ? false : true;
	}
	
	public boolean isBuilding()
	{
		for(Region i : regions)
		{
			if(i.isBuilding())
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isAccenting()
	{
		for(Region i : regions)
		{
			if(i.isAccenting())
			{
				return true;
			}
		}
		
		return false;
	}

	public World getWorld()
	{
		return world;
	}

	public void setWorld(World world)
	{
		this.world = world;
	}

	public Faction getFaction()
	{
		return faction;
	}

	public void setFaction(Faction faction)
	{
		this.faction = faction;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public GList<Region> getRegions()
	{
		return regions;
	}

	public void setRegions(GList<Region> regions)
	{
		this.regions = regions;
	}
}
