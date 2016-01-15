package com.glacialrush.composite;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.object.GList;
import com.glacialrush.composite.Job.JobStatus;
import com.glacialrush.xapi.FastMath;

public class Capture
{
	protected Region region;
	protected Map map;
	protected Faction secured;
	protected Faction offense;
	protected Integer progress;
	protected Location location;
	protected GlacialServer pl;
	protected Job accentJob;
	
	public Capture(Location location, Region region)
	{
		this.location = location;
		this.pl = region.pl;
		this.region = region;
		this.map = region.map;
		this.secured = Faction.neutral();
		this.offense = null;
		this.progress = 100;
		this.accentJob = new Job("Region [" + region.x + ", " + region.z + "]: " + region.getName() + " Capture Accent[" + Faction.neutral().getName() + "]", pl.getJobController());
	}
	
	public boolean isAccenting()
	{
		return accentJob.getStatus().equals(JobStatus.RUNNING) || accentJob.getStatus().equals(JobStatus.WAITING);
	}
	
	public void accent()
	{
		if(isAccenting())
		{
			return;
		}
		
		Block b = location.getBlock();
		DyeColor dye = getSecured().getDyeColor();
		accentJob = new Job("Region [" + region.x + ", " + region.z + "]: " + region.getName() + " Capture Accent[" + getSecured().getName() + "]", pl.getJobController());
		
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation(), dye));
		accentJob.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation(), dye));
		
		pl.getJobController().addJob(accentJob);
	}
	
	public void offend()
	{
		progress -= 5;
		
		if(progress > 100)
		{
			progress = 100;
		}
		
		if(progress < 0)
		{
			progress = 0;
		}
	}
	
	public void defend()
	{
		progress += 5;
		
		if(progress > 100)
		{
			progress = 100;
		}
		
		if(progress < 0)
		{
			progress = 0;
		}
	}
	
	public GList<Manipulation> buildTask()
	{
		GList<Manipulation> jobs = new GList<Manipulation>();
		Block block = location.getBlock();
		
		jobs.add(new Manipulation(block.getRelative(BlockFace.DOWN).getLocation(), Material.IRON_BLOCK));
		jobs.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getLocation(), Material.IRON_BLOCK));
		jobs.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), Material.IRON_BLOCK));
		jobs.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), Material.IRON_BLOCK));
		jobs.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), Material.IRON_BLOCK));
		jobs.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), Material.IRON_BLOCK));
		jobs.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getLocation(), Material.IRON_BLOCK));
		jobs.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getLocation(), Material.IRON_BLOCK));
		jobs.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getLocation(), Material.IRON_BLOCK));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), Material.STAINED_CLAY));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_CLAY));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_CLAY));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_CLAY));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_CLAY));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), Material.STAINED_CLAY));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_CLAY));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_CLAY));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), Material.STAINED_CLAY));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS));
		jobs.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS));
		
		return jobs;
	}
	
	public void reset()
	{
		reset(region.getFaction());
	}
	
	public void reset(Faction f)
	{
		setSecured(f);
		setOffense(null);
		setProgress(100);
		accent();
	}
	
	public GList<Player> getPlayers()
	{
		GList<Player> players = new GList<Player>();
		
		for(Player i : pl.onlinePlayers())
		{
			if(contains(i))
			{
				players.add(i);
			}
		}
		
		return players;
	}
	
	public boolean contains(Player p)
	{
		return FastMath.isInRadius(p.getLocation(), getLocation(), 6);
	}
	
	public Region getRegion()
	{
		return region;
	}
	
	public void setRegion(Region region)
	{
		this.region = region;
	}
	
	public Map getMap()
	{
		return map;
	}
	
	public void setMap(Map map)
	{
		this.map = map;
	}
	
	public Faction getSecured()
	{
		return secured;
	}
	
	public void setSecured(Faction secured)
	{
		this.secured = secured;
	}
	
	public Faction getOffense()
	{
		return offense;
	}
	
	public void setOffense(Faction offense)
	{
		this.offense = offense;
	}
	
	public Integer getProgress()
	{
		return progress;
	}
	
	public void setProgress(Integer progress)
	{
		this.progress = progress;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public void setLocation(Location location)
	{
		this.location = location;
	}
}
