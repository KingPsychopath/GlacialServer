package com.glacialrush.composite;

import java.util.Collections;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.object.GMap;
import com.glacialrush.api.thread.GlacialTask;
import com.glacialrush.component.CommandController;

public class Map
{
	protected World world;
	protected Faction faction;
	protected String name;
	protected GList<Region> regions;
	protected GlacialServer pl;
	protected GMap<Faction, Region> spawns;
	
	public Map(GlacialServer pl, World world, String name)
	{
		this.name = name;
		this.regions = new GList<Region>();
		this.world = world;
		this.pl = pl;
		this.faction = Faction.neutral();
		this.spawns = new GMap<Faction, Region>();
	}
	
	public void addRegion(Player p)
	{
		CommandController c = pl.getCommandController();
		
		if(!world.equals(p.getWorld()))
		{
			c.f(p, "Cannot Create 4th dimensional maps.");
			return;
		}
		
		if(isBuilding() || isAccenting())
		{
			c.f(p, "The Map is currently building.");
			return;
		}
		
		Hunk hunk = new Hunk(pl.target(p));
		
		if(regions.isEmpty())
		{
			Region region = new Region(pl.target(p), this);
			region.draw(p);
			regions.add(region);
			return;
		}
		
		if(getRegion(hunk) != null)
		{
			c.f(p, "That is already a region.");
			return;
		}
		
		if(!hasNeighbors(hunk))
		{
			c.f(p, "Regions must have a connected region.");
			return;
		}
		
		Region region = new Region(pl.target(p), this);
		region.draw(p);
		regions.add(region);
	}
	
	public Region getRegion(Player p)
	{
		for(Region i : regions)
		{
			if(i.contains(p))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public boolean hasNeighbors(Hunk hunk)
	{
		for(Hunk i : hunk.connected())
		{
			if(getRegion(i) != null)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void accent()
	{
		for(Region i : regions)
		{
			i.accent();
		}
	}
	
	public void accent(Faction faction)
	{
		for(Region i : regions)
		{
			i.setFaction(faction);
			i.accent();
		}
	}
	
	public GMap<MapCorner, Region> getCorners()
	{
		GMap<MapCorner, Region> corners = new GMap<MapCorner, Region>();
		
		for(MapCorner i : MapCorner.values())
		{
			int x = i.isX() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
			int z = i.isZ() ? Integer.MIN_VALUE : Integer.MAX_VALUE;
			Region c = null;
			
			for(Region j : regions)
			{
				if(i.isX())
				{
					if(j.getX() > x)
					{
						x = j.getX();
						
						for(Region k : regions)
						{
							if(j.getX() == x)
							{
								if(i.isZ())
								{
									if(k.getZ() > z)
									{
										z = k.getZ();
										c = k;
									}
								}
								
								else
								{
									if(k.getZ() < z)
									{
										z = k.getZ();
										c = k;
									}
								}
							}
						}
					}
				}
				
				else
				{
					if(j.getX() < x)
					{
						x = j.getX();
						
						for(Region k : regions)
						{
							if(j.getX() == x)
							{
								if(i.isZ())
								{
									if(k.getZ() > z)
									{
										z = k.getZ();
										c = k;
									}
								}
								
								else
								{
									if(k.getZ() < z)
									{
										z = k.getZ();
										c = k;
									}
								}
							}
						}
					}
				}
			}
			
			if(c != null)
			{
				corners.put(i, c);
			}
		}
		
		return corners;
	}
	
	public void randomizeSpawns()
	{
		GMap<MapCorner, Region> corners = getCorners();
		
		if(corners.size() < 3)
		{
			pl.f("Not enough regions to randomize spawns");
			return;
		}
		
		GList<Region> spawns = new GList<Region>();
		GList<Faction> factions = Faction.all();
		
		for(Region i : corners.values())
		{
			spawns.add(i);
		}
		
		Collections.shuffle(spawns);
		Collections.shuffle(factions);
		
		this.spawns.clear();
		
		for(Faction i : factions)
		{
			this.spawns.put(i, spawns.get(0));
			spawns.remove(0);
		}
	}
	
	public void neutralize()
	{
		for(Region i : regions)
		{
			i.setFaction(Faction.neutral());
		}
	}
	
	public void accentEvenley()
	{
		neutralize();
		randomizeSpawns();
		
		for(Faction i : spawns.keySet())
		{
			spawns.get(i).setFaction(i);
			spawns.get(i).accent();
		}
		
		//TODO Accent it bro
	}
	
	public GList<Region> getRegions(Faction faction)
	{
		GList<Region> reg = new GList<Region>();
		
		for(Region i : regions)
		{
			if(i.getFaction().equals(faction))
			{
				reg.add(i);
			}
		}
		
		return reg;
	}
	
	public boolean hasNeutralRegions()
	{
		for(Region i : regions)
		{
			if(i.getFaction().equals(Faction.neutral()))
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
	
	public void setup()
	{
		build();
		
		pl.newThread(new GlacialTask()
		{
			public void run()
			{
				if(isBuilding())
				{
					setDelay(20);
				}
				
				else
				{
					accentEvenley();
					
					pl.newThread(new GlacialTask()
					{
						public void run()
						{
							if(!isAccenting())
							{
								stop();
								return;
							}
						}
					});
					
					stop();
					return;
				}
			}
		});
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
