package com.glacialrush.composite;

import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.object.GList;
import com.glacialrush.composite.Job.JobStatus;
import com.glacialrush.xapi.Cuboid;
import com.glacialrush.xapi.Cuboid.CuboidDirection;

public class Region extends Hunk
{
	protected String name;
	protected Location spawn;
	protected Faction faction;
	protected Map map;
	protected GlacialServer pl;
	protected GList<Capture> captures;
	protected GList<Location> accents;
	protected Job buildJob;
	protected Job accentJob;
	
	public Region(Location spawn, Map map)
	{
		super(spawn);
		
		this.name = map.getName();
		this.spawn = spawn;
		this.map = map;
		this.pl = map.pl;
		this.faction = Faction.neutral();
		this.captures = new GList<Capture>();
		this.accents = new GList<Location>();
		this.buildJob = new Job("Region [" + x + ", " + z + "]: " + getName() + " Build", pl.getJobController());
		this.accentJob = new Job("Region [" + x + ", " + z + "]: " + getName() + " Accent[" + Faction.neutral().getName() + "]", pl.getJobController());
	}
	
	public boolean isBuilding()
	{
		return buildJob.getStatus().equals(JobStatus.RUNNING) || buildJob.getStatus().equals(JobStatus.WAITING);
	}
	
	public boolean isAccenting()
	{
		return accentJob.getStatus().equals(JobStatus.RUNNING) || accentJob.getStatus().equals(JobStatus.WAITING) || isCapAccenting();
	}
	
	public boolean isCapAccenting()
	{
		for(Capture i : captures)
		{
			if(i.isAccenting())
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void accent()
	{
		if(isBuilding() || isAccenting())
		{
			return;
		}
		
		accentJob = new Job("Region [" + x + ", " + z + "]: " + getName() + " Accent[" + getFaction().getName() + "]", pl.getJobController());
		
		Iterator<Location> it = accents.iterator();
		
		while(it.hasNext())
		{
			accentJob.add(it.next(), getFaction().getDyeColor());
		}
		
		for(Capture i : captures)
		{
			i.setSecured(getFaction());
			i.setOffense(null);
			i.setProgress(100);
			i.accent();
		}
		
		pl.getJobController().addJob(accentJob);
	}
	
	@SuppressWarnings("deprecation")
	public void draw(Player p)
	{
		Job drawJob = new Job("Region [" + x + ", " + z + "]: " + getName() + " Draw[" + p.getName() + "]", pl.getJobController());
		
		Iterator<Block> n = new Cuboid(cuboid).flatten(p.getTargetBlock((HashSet<Byte>)null, 256).getLocation().getBlockY()).getFace(CuboidDirection.North).iterator();
		Iterator<Block> s = new Cuboid(cuboid).flatten(p.getTargetBlock((HashSet<Byte>)null, 256).getLocation().getBlockY()).getFace(CuboidDirection.South).iterator();
		Iterator<Block> e = new Cuboid(cuboid).flatten(p.getTargetBlock((HashSet<Byte>)null, 256).getLocation().getBlockY()).getFace(CuboidDirection.East).iterator();
		Iterator<Block> w = new Cuboid(cuboid).flatten(p.getTargetBlock((HashSet<Byte>)null, 256).getLocation().getBlockY()).getFace(CuboidDirection.West).iterator();
		
		while(n.hasNext())
		{
			drawJob.add(n.next().getLocation(), Material.SEA_LANTERN, p);
		}
		
		while(s.hasNext())
		{
			drawJob.add(s.next().getLocation(), Material.SEA_LANTERN, p);
		}
		
		while(e.hasNext())
		{
			drawJob.add(e.next().getLocation(), Material.SEA_LANTERN, p);
		}
		
		while(w.hasNext())
		{
			drawJob.add(w.next().getLocation(), Material.SEA_LANTERN, p);
		}
		
		pl.getJobController().addJob(drawJob);
	}
	
	public void build()
	{
		if(isBuilding() || isAccenting())
		{
			return;
		}
		
		buildJob = new Job("Region [" + x + ", " + z + "]: " + getName() + " Build", pl.getJobController());
		
		Iterator<Block> it = iterator();
		accents.clear();
		captures.clear();
		
		while(it.hasNext())
		{
			Block block = it.next();
			
			if(block.getType().equals(Material.WOOL) || block.getType().equals(Material.STAINED_GLASS) || block.getType().equals(Material.STAINED_GLASS_PANE) || block.getType().equals(Material.STAINED_CLAY) || block.getType().equals(Material.BANNER) || block.getType().equals(Material.STANDING_BANNER) || block.getType().equals(Material.WALL_BANNER) || block.getType().equals(Material.CARPET))
			{
				accents.add(block.getLocation());
			}
			
			if(block.getType().equals(Material.BEACON))
			{
				Capture c = new Capture(block.getLocation(), this);
				
				for(Manipulation i : c.buildTask())
				{
					buildJob.add(i);
				}
				
				captures.add(c);
			}
		}
		
		pl.getJobController().addJob(buildJob);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
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
		
		for(Capture i : captures)
		{
			i.setSecured(faction);
			i.setProgress(100);
			i.setOffense(null);
		}
	}

	public Map getMap()
	{
		return map;
	}

	public void setMap(Map map)
	{
		this.map = map;
	}

	public GList<Capture> getCaptures()
	{
		return captures;
	}

	public void setCaptures(GList<Capture> captures)
	{
		this.captures = captures;
	}

	public GList<Location> getAccents()
	{
		return accents;
	}

	public void setAccents(GList<Location> accents)
	{
		this.accents = accents;
	}

	public Job getBuildJob()
	{
		return buildJob;
	}

	public void setBuildJob(Job buildJob)
	{
		this.buildJob = buildJob;
	}

	public Job getAccentJob()
	{
		return accentJob;
	}

	public void setAccentJob(Job accentJob)
	{
		this.accentJob = accentJob;
	}
}
