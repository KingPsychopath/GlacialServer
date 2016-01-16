package com.glacialrush.composite;

import org.bukkit.Location;
import com.glacialrush.GlacialServer;
import com.glacialrush.composite.data.WarpgateData;
import com.glacialrush.xapi.Cuboid;
import com.glacialrush.xapi.ULocation;

public class Warpgate
{
	private Cuboid cuboid;
	private Location spawn;
	private Faction faction;
	private WarpgateArray array;
	private GlacialServer pl;
	
	public Warpgate(GlacialServer pl, WarpgateArray array, Cuboid cuboid, Location spawn, Faction faction)
	{
		this.cuboid = cuboid;
		this.array = array;
		this.spawn = spawn;
		this.faction = faction;
		this.pl = pl;
	}
	
	public WarpgateData getData()
	{
		return new WarpgateData(new ULocation(cuboid.getLowerNE()), new ULocation(cuboid.getUpperSW()), new ULocation(spawn), faction);
	}
	
	public WarpgateArray getArray()
	{
		return array;
	}
	
	public void setArray(WarpgateArray array)
	{
		this.array = array;
	}
	
	public Cuboid getCuboid()
	{
		return cuboid;
	}
	
	public void setCuboid(Cuboid cuboid)
	{
		this.cuboid = cuboid;
	}
	
	public Location getSpawn()
	{
		return spawn;
	}
	
	public void setSpawn(Location spawn)
	{
		this.spawn = spawn;
	}
	
	public Faction getFaction()
	{
		return faction;
	}
	
	public void setFaction(Faction faction)
	{
		this.faction = faction;
	}
}
