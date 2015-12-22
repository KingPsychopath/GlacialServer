package com.ulticraft.map;

import java.io.Serializable;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.ulticraft.GlacialRush;
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
	protected UMap<Player, Faction> players;
	
	public Map(GlacialRush pl, String name, World world)
	{
		this.name = name;
		this.world = world.getName();
		this.pl = pl;
		
		regions = new UList<Region>();
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
		for(Region i : regions)
		{
			i.tick(this);
		}
	}
	
	public Faction getPlayerFaction(Player p)
	{
		return players.get(p);
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

	public void setPlayers(UMap<Player, Faction> players)
	{
		this.players = players;
	}
}
