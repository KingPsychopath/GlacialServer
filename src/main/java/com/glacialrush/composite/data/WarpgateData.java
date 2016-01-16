package com.glacialrush.composite.data;

import java.io.Serializable;
import com.glacialrush.GlacialServer;
import com.glacialrush.composite.Faction;
import com.glacialrush.composite.Warpgate;
import com.glacialrush.composite.WarpgateArray;
import com.glacialrush.xapi.Cuboid;
import com.glacialrush.xapi.ULocation;

public class WarpgateData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private ULocation a;
	private ULocation b;
	private ULocation spawn;
	private Faction faction;
	
	public WarpgateData(ULocation a, ULocation b, ULocation spawn, Faction faction)
	{
		this.a = a;
		this.b = b;
		this.spawn = spawn;
		this.faction = faction;
	}
	
	public ULocation getA()
	{
		return a;
	}
	
	public void setA(ULocation a)
	{
		this.a = a;
	}
	
	public ULocation getB()
	{
		return b;
	}
	
	public void setB(ULocation b)
	{
		this.b = b;
	}
	
	public ULocation getSpawn()
	{
		return spawn;
	}
	
	public void setSpawn(ULocation spawn)
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
	
	public Warpgate toWarpgate(GlacialServer pl, WarpgateArray array)
	{
		Warpgate wg = new Warpgate(pl, array, new Cuboid(a.toLocation(), b.toLocation()), spawn.toLocation(), faction);
		
		return wg;
	}
}
