package com.ulticraft.map;

import java.io.Serializable;
import java.util.Collections;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.ulticraft.GlacialRush;
import com.ulticraft.composite.MapData;
import com.ulticraft.composite.RegionData;
import com.ulticraft.faction.Faction;
import com.ulticraft.uapi.UChunk;
import com.ulticraft.uapi.UList;
import com.ulticraft.uapi.UMap;

public class Map implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected String world;
	protected UList<Region> regions;
	protected GlacialRush pl;
	protected UMap<Faction, Region> factions;
	protected Integer x;
	protected Integer z;
	protected Integer w;
	protected Integer h;
	protected Faction callback;
	
	public Map(GlacialRush pl, MapData md)
	{
		this.name = md.getName();
		this.world = md.getWorld();
		this.pl = pl;
		
		regions = new UList<Region>();
		
		for(RegionData i : md.getRegions())
		{
			regions.add(new Region(pl, i.getName(), pl.getServer().getWorld(world).getChunkAt(i.getX(), i.getZ())));
		}
		
		x = md.getX();
		z = md.getZ();
		w = md.getW();
		h = md.getH();
		
		factions = new UMap<Faction, Region>();
		
		UList<Region> spawns = new UList<Region>();
		
		int minz = Integer.MAX_VALUE;
		int minx = Integer.MAX_VALUE;
		int maxz = Integer.MIN_VALUE;
		int maxx = Integer.MIN_VALUE;
		
		for(Region i : regions)
		{
			if(i.getCenterChunk().getX() < minx)
			{
				minx = i.getCenterChunk().getX();
			}
			
			if(i.getCenterChunk().getZ() < minz)
			{
				minz = i.getCenterChunk().getZ();
			}
			
			if(i.getCenterChunk().getX() > maxx)
			{
				maxx = i.getCenterChunk().getX();
			}
			
			if(i.getCenterChunk().getZ() > maxz)
			{
				maxz = i.getCenterChunk().getZ();
			}
		}
		
		spawns.add(getRegion(new UChunk(minx, maxz, world)));
		spawns.add(getRegion(new UChunk(maxx, maxz, world)));
		spawns.add(getRegion(new UChunk(minx, minz, world)));
		spawns.add(getRegion(new UChunk(maxx, minz, world)));
		
		if(regions.size() > 4)
		{
			spawns.add(getRegion(new UChunk(minx + ((maxx - minx) / 2), maxz, world)));
			spawns.add(getRegion(new UChunk(minx + ((maxx - minx) / 2), minz, world)));
			spawns.add(getRegion(new UChunk(minx, minz + ((maxz - minz) / 2), world)));
			spawns.add(getRegion(new UChunk(maxx, minz + ((maxz - minz) / 2), world)));
		}
		
		Collections.shuffle(spawns);
		
		factions.put(Faction.omni(), spawns.get(0));
		factions.put(Faction.enigma(), spawns.get(1));
		factions.put(Faction.cryptic(), spawns.get(2));
	}
	
	public Map(GlacialRush pl, String name, World world)
	{
		this.name = name;
		this.world = world.getName();
		this.pl = pl;
		
		regions = new UList<Region>();
	}
	
	public boolean isCompleted()
	{
		if(x != null && regions.size() > 0)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean contains(Player player)
	{
		for(Region i : regions)
		{
			if(i.contains(player))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void tick()
	{
		Faction dominant = null;
		Boolean takeover = true;
		
		for(Region i : regions)
		{
			i.tick(this);
			
			if(dominant == null)
			{
				dominant = i.getFaction();
			}
			
			else if(!i.getFaction().equals(dominant))
			{
				takeover = false;
			}
		}
		
		if(takeover)
		{
			callback = dominant;
		}
	}
	
	public Faction getPlayerFaction(Player p)
	{
		return pl.getFactionComponent().getPlayers().get(p);
	}
	
	public boolean canCapture(Faction f, Region r)
	{
		Chunk c = r.getCenterChunk().toChunk();
		World w = c.getWorld();
		
		if(getRegion(new UChunk(w.getChunkAt(c.getX(), c.getZ() + 3))) != null)
		{
			if(getRegion(new UChunk(w.getChunkAt(c.getX(), c.getZ() + 3))).getFaction().equals(f))
			{
				return true;
			}
		}
		
		if(getRegion(new UChunk(w.getChunkAt(c.getX(), c.getZ() - 3))) != null)
		{
			if(getRegion(new UChunk(w.getChunkAt(c.getX(), c.getZ() - 3))).getFaction().equals(f))
			{
				return true;
			}
		}
		
		if(getRegion(new UChunk(w.getChunkAt(c.getX() + 3, c.getZ()))) != null)
		{
			if(getRegion(new UChunk(w.getChunkAt(c.getX() + 3, c.getZ()))).getFaction().equals(f))
			{
				return true;
			}
		}
		
		if(getRegion(new UChunk(w.getChunkAt(c.getX() - 3, c.getZ()))) != null)
		{
			if(getRegion(new UChunk(w.getChunkAt(c.getX() - 3, c.getZ()))).getFaction().equals(f))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public Region getRegion(Player player)
	{
		for(Region i : regions)
		{
			if(i.contains(player))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public void deploy(Player p)
	{
		p.teleport(getFactionSpawn(getPlayerFaction(p)).getSpawn().toLocation());
	}
	
	public void deploy(Player p, Region r)
	{
		p.teleport(r.getSpawn().toLocation());
	}
	
	public boolean canDeploy(Player p, Region r)
	{
		if(getPlayerFaction(p).equals(r.getFaction()))
		{
			return true;
		}
		
		return false;
	}
	
	public Region getFactionSpawn(Faction f)
	{
		return factions.get(f);
	}
	
	public Region getRegion(UChunk chunk)
	{
		for(Region i : regions)
		{
			if(i.getCenterChunk().equals(chunk))
			{
				return i;
			}
		}
		
		for(Region i : regions)
		{
			for(UChunk j : i.getChunks())
			{
				if(chunk.equals(j))
				{
					return i;
				}
			}
		}
		
		return null;
	}
	
	public UList<Player> getPlayers()
	{
		UList<Player> players = new UList<Player>();
		
		for(Region i : regions)
		{
			players.add(i.getPlayers());
		}
		
		return players;
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
		x = chunk.getX();
		z = chunk.getZ();
		w = xx;
		h = zz;
		
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
	
	public UMap<Faction, Region> getFactions()
	{
		return factions;
	}
	
	public void setFactions(UMap<Faction, Region> factions)
	{
		this.factions = factions;
	}
	
	public GlacialRush getPl()
	{
		return pl;
	}
	
	public void setPl(GlacialRush pl)
	{
		this.pl = pl;
	}
	
	public Integer getX()
	{
		return x;
	}
	
	public void setX(Integer x)
	{
		this.x = x;
	}
	
	public Integer getZ()
	{
		return z;
	}
	
	public void setZ(Integer z)
	{
		this.z = z;
	}
	
	public Integer getW()
	{
		return w;
	}
	
	public void setW(Integer w)
	{
		this.w = w;
	}
	
	public Integer getH()
	{
		return h;
	}
	
	public void setH(Integer h)
	{
		this.h = h;
	}

	public Faction getCallback()
	{
		return callback;
	}

	public void setCallback(Faction callback)
	{
		this.callback = callback;
	}
}
