package com.glacialrush.composite;

import java.util.Iterator;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.object.GMap;
import com.glacialrush.composite.Hunk.HunkFace;
import com.glacialrush.composite.data.WarpgateArrayData;
import com.glacialrush.composite.data.WarpgateData;
import com.glacialrush.xapi.Cuboid;

public class WarpgateArray
{
	private GMap<Faction, Warpgate> gates;
	private GlacialServer pl;
	
	public WarpgateArray(GlacialServer pl)
	{
		this.pl = pl;
		this.gates = new GMap<Faction, Warpgate>();
	}
	
	public WarpgateArray(GlacialServer pl, Location location)
	{
		this.pl = pl;
		this.gates = new GMap<Faction, Warpgate>();
		
		Hunk c = new Hunk(location);
		Hunk o = c.getRelative(HunkFace.EAST);
		Hunk e = c.getRelative(HunkFace.WEST);
		
		Iterator<Block> it = c.getCuboid().flatten(location.getBlockY()).iterator();
		Job job = new Job("Warpgate Builder: Cryptic", pl.getJobController());
		
		while(it.hasNext())
		{
			Block i = it.next();
			job.add(new Manipulation(i.getLocation(), Material.STAINED_CLAY));
			job.add(new Manipulation(i.getLocation(), DyeColor.YELLOW));
		}
		
		pl.getJobController().addJob(job);
		
		it = o.getCuboid().flatten(location.getBlockY()).iterator();
		Job job2 = new Job("Warpgate Builder: Omni", pl.getJobController());
		
		while(it.hasNext())
		{
			Block i = it.next();
			job2.add(new Manipulation(i.getLocation(), Material.STAINED_CLAY));
			job2.add(new Manipulation(i.getLocation(), DyeColor.PURPLE));
		}
		
		pl.getJobController().addJob(job2);
		
		it = e.getCuboid().flatten(location.getBlockY()).iterator();
		Job job3 = new Job("Warpgate Builder: Enigma", pl.getJobController());
		
		while(it.hasNext())
		{
			Block i = it.next();
			job3.add(new Manipulation(i.getLocation(), Material.STAINED_CLAY));
			job3.add(new Manipulation(i.getLocation(), DyeColor.RED));
		}
		
		pl.getJobController().addJob(job3);
		
		Warpgate cryptic = new Warpgate(pl, this, new Cuboid(c.getCuboid().flatten(location.getBlockY()).getLowerNE(), c.getCuboid().flatten(location.getBlockY()).getUpperSW().clone().add(new Location(location.getWorld(), 0, 64, 0))), location, Faction.cryptic());
		Warpgate omni = new Warpgate(pl, this, new Cuboid(o.getCuboid().flatten(location.getBlockY()).getLowerNE(), o.getCuboid().flatten(location.getBlockY()).getUpperSW().clone().add(new Location(location.getWorld(), 0, 64, 0))), location.clone().add(new Location(location.getWorld(), 96, 0, 0)), Faction.omni());
		Warpgate enigma = new Warpgate(pl, this, new Cuboid(e.getCuboid().flatten(location.getBlockY()).getLowerNE(), e.getCuboid().flatten(location.getBlockY()).getUpperSW().clone().add(new Location(location.getWorld(), 0, 64, 0))), location.clone().add(new Location(location.getWorld(), -96, 0, 0)), Faction.enigma());
		
		gates.put(Faction.cryptic(), cryptic);
		gates.put(Faction.omni(), omni);
		gates.put(Faction.enigma(), enigma);
	}
	
	public WarpgateArrayData getData()
	{
		WarpgateArrayData wd = new WarpgateArrayData(new GMap<Faction, WarpgateData>());
		
		for(Faction i : gates.keySet())
		{
			wd.getGates().put(i, gates.get(i).getData());
		}
		
		return wd;
	}
	
	public GMap<Faction, Warpgate> getGates()
	{
		return gates;
	}
	
	public void setGates(GMap<Faction, Warpgate> gates)
	{
		this.gates = gates;
	}
	
	public void respawn(Player p)
	{
		p.teleport(gates.get(pl.getGameController().getPlayerHandler().getFaction(p)).getSpawn());
	}
}
