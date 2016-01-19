package com.glacialrush.composite;

import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.thread.GlacialTask;
import com.glacialrush.composite.Job.JobStatus;
import com.glacialrush.composite.data.RegionData;
import com.glacialrush.xapi.Cuboid;
import com.glacialrush.xapi.Cuboid.CuboidDirection;
import com.glacialrush.xapi.FastMath;
import com.glacialrush.xapi.ULocation;
import net.md_5.bungee.api.ChatColor;

public class Region extends Hunk
{
	protected String name;
	protected Faction faction;
	protected Map map;
	protected GlacialServer pl;
	protected GList<Capture> captures;
	protected GList<Location> accents;
	protected Job buildJob;
	protected Job accentJob;
	protected Boolean building;
	protected Integer timer;
	protected Long lastTimer;
	protected GList<Location> spawns;
	protected Boolean captureable;
		
	public Region(Location spawn, Map map)
	{
		super(spawn);
		
		this.name = map.getName();
		this.map = map;
		this.pl = map.pl;
		this.faction = Faction.neutral();
		this.captures = new GList<Capture>();
		this.accents = new GList<Location>();
		this.buildJob = new Job("Region [" + x + ", " + z + "]: " + getName() + " Build", pl.getJobController());
		this.accentJob = new Job("Region [" + x + ", " + z + "]: " + getName() + " Accent[" + Faction.neutral().getName() + "]", pl.getJobController());
		this.building = false;
		this.spawns = new GList<Location>();
		this.lastTimer = (long) -1;
		this.timer = -1;
		this.captureable = false;
	}
	
	public Region(Hunk spawn, Map map)
	{
		super(spawn);
		
		this.name = map.getName();
		this.map = map;
		this.pl = map.pl;
		this.faction = Faction.neutral();
		this.captures = new GList<Capture>();
		this.accents = new GList<Location>();
		this.buildJob = new Job("Region [" + x + ", " + z + "]: " + getName() + " Build", pl.getJobController());
		this.accentJob = new Job("Region [" + x + ", " + z + "]: " + getName() + " Accent[" + Faction.neutral().getName() + "]", pl.getJobController());
		this.building = false;
		this.spawns = new GList<Location>();
		this.lastTimer = (long) -1;
		this.timer = -1;
		this.captureable = false;
	}
	
	public boolean isTimer()
	{
		return timer != -1;
	}
	
	public void resetTimer()
	{
		timer = -1;
	}
	
	public void startTimer(int t)
	{
		timer = (t * 20) * 25;
	}
	
	public void decTimer(long last)
	{
		long dif = 125;
		
		if(lastTimer == -1)
		{
			lastTimer = last - 125;
		}
		
		else
		{
			dif = last - lastTimer;
			lastTimer = last;
		}
		
		timer -= (int) dif;
		
		if(timer < 0)
		{
			timer = 0;
		}
	}
	
	public RegionData getData()
	{
		GList<ULocation> caps = new GList<ULocation>();
		
		for(Capture i : captures)
		{
			caps.add(new ULocation(i.getLocation()));
		}
		
		return new RegionData(name, world, x, z);
	}
	
	public boolean connected(Faction f)
	{
		if(getFaction().equals(f))
		{
			return true;
		}
		
		for(Region i : connectedRegions())
		{
			if(i.getFaction().equals(f))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean check(Player p)
	{
		if(name.equals(map.getName()))
		{
			p.sendMessage(ChatColor.RED + "Region " + x + ", " + z + " name is the same as map.");
			return false;
		}
		
		if(!isCaptureable())
		{
			return true;
		}
		
		if(!hasSpawns())
		{
			p.sendMessage(ChatColor.RED + "Region " + x + ", " + z + " no accurate spawn.");
			return false;
		}
		
		return true;
	}
	
	public boolean isBuilding()
	{
		return buildJob.getStatus().equals(JobStatus.RUNNING) || buildJob.getStatus().equals(JobStatus.WAITING) || building;
	}
	
	public boolean isAccenting()
	{
		return accentJob.getStatus().equals(JobStatus.RUNNING) || accentJob.getStatus().equals(JobStatus.WAITING) || isCapAccenting();
	}
	
	public Boolean isCaptureable()
	{
		return captureable;
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
	
	public GList<Region> connectedRegions()
	{
		GList<Region> regions = new GList<Region>();
		
		for(Hunk i : connected())
		{
			if(map.getRegion(i) != null)
			{
				regions.add(map.getRegion(i));
			}
		}
		
		return regions;
	}
	
	public boolean connectedTo(Region r)
	{
		return connectedRegions().contains(r);
	}
	
	public boolean connectedTo(Faction f)
	{
		for(Region i : connectedRegions())
		{
			if(i.getFaction().equals(f))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isNeutral()
	{
		return getFaction().equals(Faction.neutral());
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
	
	@SuppressWarnings("deprecation")
	public void drawReal(Player p, Material m)
	{
		Job drawJob = new Job("Region [" + x + ", " + z + "]: " + getName() + " RealDraw", pl.getJobController());
		
		Iterator<Block> n = new Cuboid(cuboid).flatten(p.getTargetBlock((HashSet<Byte>) null, 256).getLocation().getBlockY()).getFace(CuboidDirection.North).iterator();
		Iterator<Block> s = new Cuboid(cuboid).flatten(p.getTargetBlock((HashSet<Byte>) null, 256).getLocation().getBlockY()).getFace(CuboidDirection.South).iterator();
		Iterator<Block> e = new Cuboid(cuboid).flatten(p.getTargetBlock((HashSet<Byte>) null, 256).getLocation().getBlockY()).getFace(CuboidDirection.East).iterator();
		Iterator<Block> w = new Cuboid(cuboid).flatten(p.getTargetBlock((HashSet<Byte>) null, 256).getLocation().getBlockY()).getFace(CuboidDirection.West).iterator();
		
		while(n.hasNext())
		{
			drawJob.add(n.next().getLocation(), m);
		}
		
		while(s.hasNext())
		{
			drawJob.add(s.next().getLocation(), m);
		}
		
		while(e.hasNext())
		{
			drawJob.add(e.next().getLocation(), m);
		}
		
		while(w.hasNext())
		{
			drawJob.add(w.next().getLocation(), m);
		}
		
		pl.getJobController().addJob(drawJob);
	}
	
	@SuppressWarnings("deprecation")
	public void draw(Player p)
	{
		Job drawJob = new Job("Region [" + x + ", " + z + "]: " + getName() + " Draw[" + p.getName() + "]", pl.getJobController());
		
		Iterator<Block> n = new Cuboid(cuboid).flatten(p.getTargetBlock((HashSet<Byte>) null, 256).getLocation().getBlockY()).getFace(CuboidDirection.North).iterator();
		Iterator<Block> s = new Cuboid(cuboid).flatten(p.getTargetBlock((HashSet<Byte>) null, 256).getLocation().getBlockY()).getFace(CuboidDirection.South).iterator();
		Iterator<Block> e = new Cuboid(cuboid).flatten(p.getTargetBlock((HashSet<Byte>) null, 256).getLocation().getBlockY()).getFace(CuboidDirection.East).iterator();
		Iterator<Block> w = new Cuboid(cuboid).flatten(p.getTargetBlock((HashSet<Byte>) null, 256).getLocation().getBlockY()).getFace(CuboidDirection.West).iterator();
		
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
	
	public boolean hasSpawns()
	{
		return !spawns.isEmpty();
	}
	
	public boolean closeToEnemySpawn(Player p)
	{
		for(Location i : spawns)
		{
			if(FastMath.isInRadius(p.getLocation(), i, 9))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void build()
	{
		if(isBuilding() || isAccenting())
		{
			return;
		}
		
		building = true;
		
		buildJob = new Job("Region [" + x + ", " + z + "]: " + getName() + " Build", pl.getJobController());
		
		final Iterator<Block> it = iterator();
		accents.clear();
		captures.clear();
		spawns.clear();
		
		pl.o("Started Job: " + ChatColor.LIGHT_PURPLE + "Region[" + x + ", " + z + "]: " + getName() + " Build[ACCSC]");
		
		pl.newThread(new GlacialTask("Region Build Task " + "Region[" + x + ", " + z + "]: " + getName())
		{
			@Override
			public void run()
			{
				long ms = System.currentTimeMillis();
				
				while(it.hasNext() && System.currentTimeMillis() - ms < 30)
				{
					Block block = it.next();
					
					if(block.getType().equals(Material.WALL_SIGN) || block.getType().equals(Material.SIGN_POST))
					{
						Sign sign = (Sign) block.getState();
						
						if(sign.getLine(0).equalsIgnoreCase("name"))
						{
							String s = sign.getLine(1) + sign.getLine(2) + sign.getLine(3);
							
							if(s != null && !s.equals(""))
							{
								Region.this.setName(s);
								s("Set Region name: " + s);
								buildJob.add(new Manipulation(block.getLocation(), Material.AIR));
							}
						}
					}
					
					if(block.getType().equals(Material.COAL_BLOCK))
					{
						spawns.add(block.getLocation());
					}
					
					if(block.getType().equals(Material.WOOL) || block.getType().equals(Material.STAINED_GLASS) || block.getType().equals(Material.STAINED_GLASS_PANE) || block.getType().equals(Material.STAINED_CLAY) || block.getType().equals(Material.BANNER) || block.getType().equals(Material.STANDING_BANNER) || block.getType().equals(Material.WALL_BANNER) || block.getType().equals(Material.CARPET))
					{
						accents.add(block.getLocation());
						buildJob.add(new Manipulation(block.getLocation(), getFaction().getDyeColor()));
					}
					
					if(block.getType().equals(Material.BEACON))
					{
						Capture c = new Capture(block.getLocation(), Region.this);
						
						for(Manipulation i : c.buildTask())
						{
							buildJob.add(i);
						}
						
						captures.add(c);
					}
				}
				
				if(!it.hasNext())
				{
					pl.o("Finished Job: " + ChatColor.GREEN + "Region[" + x + ", " + z + "]: " + getName() + " Build[ACCSC] " + cycleTime + "ms");
					building = false;
					captureable = !captures.isEmpty();
					stop();
					pl.getJobController().addJob(buildJob);
					cycleTime += System.currentTimeMillis() - ms;
					return;
				}
				
				cycleTime += System.currentTimeMillis() - ms;
			}
		});
	}
	
	public Long getLastTimer()
	{
		return lastTimer;
	}
	
	public void setLastTimer(Long lastTimer)
	{
		this.lastTimer = lastTimer;
	}
	
	public void setBuilding(Boolean building)
	{
		this.building = building;
	}
	
	public void setTimer(Integer timer)
	{
		this.timer = timer;
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
		return spawns.pickRandom().clone().add(new Vector(0, 1 ,0));
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
			i.reset();
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
	
	public Boolean getBuilding()
	{
		return building;
	}
	
	public Integer getTimer()
	{
		return timer;
	}

	public GList<Location> getSpawns()
	{
		return spawns;
	}

	public void setSpawns(GList<Location> spawns)
	{
		this.spawns = spawns;
	}
}
