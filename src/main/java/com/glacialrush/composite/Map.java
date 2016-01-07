package com.glacialrush.composite;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.dispatch.Title;
import com.glacialrush.composite.Hunk.HunkFace;
import com.glacialrush.xapi.FastMath;
import com.glacialrush.xapi.UList;
import com.glacialrush.xapi.UMap;
import net.md_5.bungee.api.ChatColor;

public class Map
{
	private String name;
	private World world;
	private UList<Region> regions;
	private GlacialServer pl;
	private Boolean buildServicing;
	private Integer buildService;
	private Boolean ready;
	private UMap<Faction, Region> spawns;
	
	public Map(GlacialServer pl, String name, World world)
	{
		this.pl = pl;
		this.name = name;
		this.world = world;
		this.regions = new UList<Region>();
		this.buildService = 0;
		this.buildServicing = false;
		this.ready = false;
		this.spawns = new UMap<Faction, Region>();
	}
	
	public boolean isBuilt()
	{
		for(Region i : regions)
		{
			if(!i.getBuildStatus().equals("built"))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isBuilding()
	{
		for(Region i : regions)
		{
			if(i.getBuildStatus().equals("building"))
			{
				return true;
			}
		}
		
		return false;
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
	
	public void rebuild()
	{
		for(Region i : regions)
		{
			i.setBuildStatus("unbuilt");
		}
		
		build();
	}
	
	public void setSpawn(Faction f, Region r)
	{
		spawns.put(f, r);
	}
	
	public Location getSpawn(Faction f)
	{
		if(f == null)
		{
			pl.f("INVALID SPAWN FOR FACTION: NULL");
		}
		
		return spawns.get(f).getSpawn();
	}
	
	public boolean isEmpty()
	{
		return getRegions().isEmpty();
	}
	
	public boolean ready(Player p)
	{
		if(getReady())
		{
			pl.getCommandComponent().err(p, "Map is already ready!");
			return false;
		}
		
		if(isEmpty())
		{
			pl.getCommandComponent().err(p, "Map is empty!");
			return false;
		}
		
		if(regions.size() < 9)
		{
			pl.getCommandComponent().err(p, "Map is too small!");
			return false;
		}
		
		if(spawns.size() != 3)
		{
			setMapSpawns();
		}
		
		if(!isBuilt())
		{
			pl.getCommandComponent().err(p, "Map is not built!");
			return false;
		}
		
		for(Region i : regions)
		{
			if(!i.ready(p))
			{
				return false;
			}
		}
		
		pl.getCommandComponent().suc(p, "Map is ready!");
		setReady(true);
		return true;
	}
	
	public boolean contains(Player player)
	{
		return contains(player.getLocation());
	}
	
	public boolean contains(LivingEntity entity)
	{
		return contains(entity.getLocation());
	}
	
	public void draw(Player p)
	{
		for(Region i : regions)
		{
			i.draw(p, p.getLocation().getBlockY());
		}
	}
	
	public Region getBottomLeftRegion()
	{
		Region zz = null;
		int x = Integer.MAX_VALUE;
		int z = Integer.MAX_VALUE;
		
		for(Region i : regions)
		{
			if(i.getHunk().getX() < x)
			{
				x = i.getHunk().getX();
			}
		}
		
		for(Region i : regions)
		{
			if(i.getHunk().getX() == x && i.getHunk().getZ() < z)
			{
				z = i.getHunk().getZ();
				zz = i;
			}
		}
		
		return zz;
	}
	
	public Region getTopRightRegion()
	{
		Region zz = null;
		int x = Integer.MIN_VALUE;
		int z = Integer.MIN_VALUE;
		
		for(Region i : regions)
		{
			if(i.getHunk().getX() > x)
			{
				x = i.getHunk().getX();
			}
		}
		
		for(Region i : regions)
		{
			if(i.getHunk().getX() == x && i.getHunk().getZ() > z)
			{
				z = i.getHunk().getZ();
				zz = i;
			}
		}
		
		return zz;
	}
	
	public void setMapSpawns()
	{
		UMap<Faction, Region> ms = new UMap<Faction, Region>();
		Double maxdist = Double.MIN_VALUE;
		
		Region a = null;
		Region b = null;
		Region c = null;
		
		UList<Faction> ffs = Faction.all().copy();
		Collections.shuffle(ffs);
		
		a = getTopRightRegion();
		b = getBottomLeftRegion();
		
		for(Region i : regions)
		{
			Double da = FastMath.distance2D(a.getSpawn(), i.getSpawn());
			Double db = FastMath.distance2D(b.getSpawn(), i.getSpawn());
			
			if(da > maxdist && db > maxdist)
			{
				maxdist = Double.max(da, db);
				c = i;
			}
		}
		
		ms.put(ffs.get(0), a);
		ms.put(ffs.get(1), b);
		ms.put(ffs.get(2), c);
		
		spawns = ms;
	}
	
	public void accentEvenley()
	{
		setMapSpawns();
		
		for(Region i : regions)
		{
			i.setFaction(Faction.neutral());
		}
		
		for(Faction i : spawns.keySet())
		{
			spawns.get(i).setFaction(i);
		}
		
		final Integer[] k = new Integer[1];
		
		k[0] = pl.scheduleSyncRepeatingTask(10, 0, new Runnable()
		{
			@Override
			public void run()
			{
				if(pl.getManipulationComponent().isIdle())
				{
					pl.cancelTask(k[0]);
					
					while(hasNeutralRegions())
					{
						for(Faction i : spawns.keySet())
						{
							for(Region j : regions)
							{
								boolean cycle = false;
								
								if(j.getFaction().equals(i))
								{
									for(HunkFace k : HunkFace.values())
									{
										Region t = getRegion(j.getHunk().getRelative(k));
										
										if(t != null && t.getFaction().equals(Faction.neutral()))
										{
											t.setFaction(i);
											cycle = true;
											break;
										}
									}
								}
								
								if(cycle)
								{
									break;
								}
							}
						}
					}
					
					while(hasNeutralRegions())
					{
						for(Region i : regions)
						{
							if(i.getFaction().equals(Faction.neutral()))
							{
								for(HunkFace j : HunkFace.values())
								{
									Region k = getRegion(i.getHunk().getRelative(j));
									
									if(k != null)
									{
										if(!k.getFaction().equals(Faction.neutral()))
										{
											i.setFaction(k.getFaction());
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		});
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
	
	public void build()
	{
		if(buildServicing)
		{
			return;
		}
		
		final Iterator<Region> it = regions.iterator();
		final Integer[] i = {0};
		
		buildServicing = true;
		
		setMapSpawns();
		
		buildService = pl.scheduleSyncRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				if(it.hasNext())
				{
					i[0] = i[0] + 1;
					Region r = it.next();
					
					if(r.getBuildStatus().equals("built"))
					{
						while(it.hasNext())
						{
							r = it.next();
							i[0] = i[0] + 1;
							
							if(r.getBuildStatus().equals("unbuilt"))
							{
								break;
							}
						}
					}
					
					else
					{
						r.build();
					}
					
					Title t = new Title();
					t.setFadeInTime(0);
					t.setFadeOutTime(0);
					
					String s = String.valueOf((int)(100 * ((double)i[0]) / (double)regions.size()));
					
					t.setSubSubTitle(ChatColor.GOLD + "Building " + name + ": " + ChatColor.AQUA +  s + "%");
					t.send();
				}
				
				else
				{
					pl.cancelTask(buildService);
					buildServicing = false;
				}
			}
		});
	}
	
	public void accent(Faction faction)
	{
		for(Region i : regions)
		{
			i.setFaction(faction);
		}
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
	
	@SuppressWarnings("deprecation")
	public boolean addRegionNear(Player p)
	{
		Location location = p.getTargetBlock((HashSet<Byte>)null, 256).getLocation();
		
		if(location.getWorld().equals(world))
		{
			if(regions.isEmpty())
			{
				regions.add(new Region(pl, this, name, location));
				regions.get(0).draw(p, location.getBlockY());
				return true;
			}
			
			Hunk hunk = new Hunk(location);
			
			if(getRegion(hunk) == null)
			{
				if(hasNeighbors(hunk))
				{
					regions.add(new Region(pl, this, name, hunk));
					
					if(maxDim() > 9)
					{
						regions.remove(getRegion(hunk));
						pl.getCommandComponent().err(p, "Map cannot span over 9 hunks");
						return false;
					}
					
					getRegion(hunk).draw(p, location.getBlockY());
					return true;
				}
				
				else
				{
					pl.getCommandComponent().err(p, "Must have a connected region");
					return false;
				}
			}
			
			else
			{
				pl.getCommandComponent().err(p, "Cannot be an existing region.");
				return false;
			}
		}
		
		else
		{
			pl.getCommandComponent().err(p, "Region must be in world: " + world.getName());
			return false;
		}
	}
	
	public void unbuild()
	{
		for(Region i : regions)
		{
			i.unbuild();
		}
	}
	
	public int maxDim()
	{
		return Math.max(maxWidth(), maxHeight());
	}
	
	public int maxWidth()
	{
		if(regions.isEmpty())
		{
			return 0;
		}
		
		int min = Integer.MIN_VALUE;
		int max = Integer.MAX_VALUE;
		
		for(Region i : regions)
		{
			int x = i.getHunk().getX();
			
			if(x > min)
			{
				min = x;
			}
			
			if(x < max)
			{
				max = x;
			}
		}
		
		return Math.abs(max - min) + 1;
	}
	
	public int maxHeight()
	{
		if(regions.isEmpty())
		{
			return 0;
		}
		
		int min = Integer.MIN_VALUE;
		int max = Integer.MAX_VALUE;
		
		for(Region i : regions)
		{
			int z = i.getHunk().getZ();
			
			if(z > min)
			{
				min = z;
			}
			
			if(z < max)
			{
				max = z;
			}
		}
		
		return Math.abs(max - min) + 1;
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public World getWorld()
	{
		return world;
	}

	public void setWorld(World world)
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

	public Boolean getBuildServicing()
	{
		return buildServicing;
	}

	public void setBuildServicing(Boolean buildServicing)
	{
		this.buildServicing = buildServicing;
	}

	public Integer getBuildService()
	{
		return buildService;
	}

	public void setBuildService(Integer buildService)
	{
		this.buildService = buildService;
	}

	public Boolean getReady()
	{
		return ready;
	}

	public void setReady(Boolean ready)
	{
		this.ready = ready;
	}
}
