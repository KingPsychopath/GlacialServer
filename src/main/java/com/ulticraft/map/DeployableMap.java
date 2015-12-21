package com.ulticraft.map;

import java.util.Collections;
import org.bukkit.World;
import org.bukkit.entity.Player;
import com.ulticraft.GlacialRush;
import com.ulticraft.faction.Faction;
import com.ulticraft.uapi.UChunk;
import com.ulticraft.uapi.UList;
import com.ulticraft.uapi.UMap;

public class DeployableMap extends Map
{
	private static final long serialVersionUID = 1L;
	
	
	
	public DeployableMap(GlacialRush pl, String name, World world)
	{
		super(pl, name, world);
		
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
		
		spawns.add(getRegion(new UChunk(minx, maxz, world.getName())));
		spawns.add(getRegion(new UChunk(maxx, maxz, world.getName())));
		spawns.add(getRegion(new UChunk(minx, minz, world.getName())));
		spawns.add(getRegion(new UChunk(maxx, minz, world.getName())));
		
		if(regions.size() > 4)
		{
			spawns.add(getRegion(new UChunk(minx + ((maxx - minx)/2), maxz, world.getName())));
			spawns.add(getRegion(new UChunk(minx + ((maxx - minx)/2), minz, world.getName())));
			spawns.add(getRegion(new UChunk(minx, minz + ((maxz - minz)/2), world.getName())));
			spawns.add(getRegion(new UChunk(maxx, minz + ((maxz - minz)/2), world.getName())));
		}
		
		Collections.shuffle(spawns);
		
		factions.put(Faction.omni(), spawns.get(0));
		factions.put(Faction.enigma(), spawns.get(1));
		factions.put(Faction.cryptic(), spawns.get(2));
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
}
