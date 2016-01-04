package com.ulticraft.composite;

import java.util.Iterator;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import com.ulticraft.GlacialServer;
import com.ulticraft.component.ManipulationComponent;
import com.ulticraft.xapi.Cuboid;
import com.ulticraft.xapi.Title;
import com.ulticraft.xapi.UList;
import com.ulticraft.xapi.Cuboid.CuboidDirection;
import net.md_5.bungee.api.ChatColor;

public class Region implements Listener
{
	private GlacialServer pl;
	private Hunk hunk;
	private UList<Location> accents;
	private UList<Capture> captures;
	private Faction faction;
	private Map map;
	private String name;
	private Location spawn;
	private String buildStatus;
	private Boolean hasSpawn;
	
	public Region(GlacialServer pl, Map map, String name, Location location)
	{
		this.pl = pl;
		this.map = map;
		this.name = name;
		this.hunk = new Hunk(location);
		this.accents = new UList<Location>();
		this.captures = new UList<Capture>();
		this.faction = Faction.neutral();
		this.buildStatus = "unbuilt";
		this.spawn = hunk.getCenter(64);
		this.hasSpawn = false;
		
		pl.register(this);
	}
	
	public Region(GlacialServer pl, Map map, String name, Hunk hunk)
	{
		this.pl = pl;
		this.map = map;
		this.name = name;
		this.hunk = hunk;
		this.accents = new UList<Location>();
		this.captures = new UList<Capture>();
		this.faction = Faction.neutral();
		this.buildStatus = "unbuilt";
		this.spawn = hunk.getCenter(64);
		this.hasSpawn = false;
		
		pl.register(this);
	}

	public void draw(Player p, int level)
	{
		Cuboid c = hunk.getCuboid().flatten(level);
		
		hallucinate(p, c.getFace(CuboidDirection.North));
		hallucinate(p, c.getFace(CuboidDirection.South));
		hallucinate(p, c.getFace(CuboidDirection.East));
		hallucinate(p, c.getFace(CuboidDirection.West));
	}
	
	public boolean ready(Player p)
	{
		if(!hasSpawn)
		{
			pl.getCommandComponent().err(p, "No Spawn for this Region");
			p.teleport(getSpawn());
			return false;
		}
		
		if(getName().equals(map.getName()))
		{
			pl.getCommandComponent().err(p, "No Name for this Region");
			p.teleport(getSpawn());
			return false;
		}
		
		return true;
	}
	
	public void unbuild()
	{
		setBuildStatus("unbuilt");
	}
	
	public void hallucinate(final Player p, Cuboid c)
	{
		final int[] t = {0};
		final Iterator<Block> it = c.iterator();
		
		t[0] = pl.scheduleSyncRepeatingTask(0, 10, new Runnable()
		{
			@Override
			public void run()
			{
				long ms = System.currentTimeMillis();
				
				while(System.currentTimeMillis() - ms < 10 && it.hasNext())
				{
					Block b = it.next();
					pl.getManipulationComponent().add(new Manipulation(b.getLocation(), Material.SEA_LANTERN, p));
				}
				
				if(!it.hasNext())
				{
					pl.cancelTask(t[0]);
				}
			}
		});
	}
	
	public void accent(Faction f)
	{
		final int[] t = {0};
		final Iterator<Location> it = accents.iterator();
		final DyeColor dye = f.getDyeColor();
		
		for(Capture i : captures)
		{
			capture(i, f);
		}
		
		t[0] = pl.scheduleSyncRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				long ms = System.currentTimeMillis();
				
				while(System.currentTimeMillis() - ms < 30 && it.hasNext())
				{
					pl.getManipulationComponent().add(new Manipulation(it.next(), dye));
				}
				
				if(!it.hasNext())
				{
					pl.cancelTask(t[0]);
				}
			}
		});
	}
	
	public void build()
	{
		if(!buildStatus.equals("unbuilt"))
		{
			return;
		}
		
		final int[] t = {0};
		final Iterator<Block> it = hunk.iterator();
		final ManipulationComponent w = pl.getManipulationComponent();
		
		buildStatus = "building";
		captures.clear();
		accents.clear();
		
		t[0] = pl.scheduleSyncRepeatingTask(0, 5, new Runnable()
		{
			@Override
			public void run()
			{
				long ms = System.currentTimeMillis();
				
				while(System.currentTimeMillis() - ms < 5 && it.hasNext())
				{
					Block block = it.next();
					
					if(block.getType().equals(Material.BEACON))
					{
						captures.add(new Capture(block.getLocation()));
						
						w.add(new Manipulation(block.getRelative(BlockFace.DOWN).getLocation(), Material.IRON_BLOCK));
						w.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getLocation(), Material.IRON_BLOCK));
						w.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), Material.IRON_BLOCK));
						w.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), Material.IRON_BLOCK));
						w.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), Material.IRON_BLOCK));
						w.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), Material.IRON_BLOCK));
						w.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getLocation(), Material.IRON_BLOCK));
						w.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getLocation(), Material.IRON_BLOCK));
						w.add(new Manipulation(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getLocation(), Material.IRON_BLOCK));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), Material.STAINED_CLAY));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_CLAY));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_CLAY));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_CLAY));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_CLAY));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), Material.STAINED_CLAY));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_CLAY));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_CLAY));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), Material.STAINED_CLAY));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS));
						w.add(new Manipulation(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS));
					}
				}
				
				if(!it.hasNext())
				{
					pl.cancelTask(t[0]);
					
					final Iterator<Block> it = hunk.iterator();
					
					t[0] = pl.scheduleSyncRepeatingTask(0, 5, new Runnable()
					{
						@Override
						public void run()
						{
							long ms = System.currentTimeMillis();
							
							while(System.currentTimeMillis() - ms < 5 && it.hasNext())
							{
								Block block = it.next();
								
								if(block.getType().equals(Material.WOOL) || block.getType().equals(Material.STAINED_GLASS) || block.getType().equals(Material.STAINED_GLASS_PANE) || block.getType().equals(Material.STAINED_CLAY) || block.getType().equals(Material.BANNER) || block.getType().equals(Material.STANDING_BANNER) || block.getType().equals(Material.WALL_BANNER) || block.getType().equals(Material.CARPET))
								{
									accents.add(block.getLocation());
								}
							}
							
							if(!it.hasNext())
							{
								pl.cancelTask(t[0]);
								
								buildStatus = "built";
							}
						}
					});
				}
			}
		});
	}
	
	public void capture(Capture cap, Faction f)
	{
		cap.setDominantFaction(f);
		
		ManipulationComponent w = pl.getManipulationComponent();
		Block b = cap.getLocation().getBlock();
		DyeColor dye = f.getDyeColor();
		
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS));			
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation(), dye));
		w.add(new Manipulation(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation(), dye));
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onModification(BlockBreakEvent e)
	{
		if(!hunk.contains(e.getBlock()) || !buildStatus.equals("built"))
		{
			return;
		}
		
		if(map.getReady())
		{
			e.setCancelled(true);
			return;
		}
		
		buildStatus = "unbuilt";
		map.unbuild();
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.SHOOT_ARROW, 1f, 1.8f);
		new Title("  ", ChatColor.YELLOW + "Region Modified", ChatColor.GOLD + "Requires Rebuild").send(e.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onModification(BlockPlaceEvent e)
	{
		if(!hunk.contains(e.getBlock()) || !buildStatus.equals("built"))
		{
			return;
		}
		
		if(map.getReady())
		{
			e.setCancelled(true);
			return;
		}
		
		buildStatus = "unbuilt";
		map.unbuild();
		e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.SHOOT_ARROW, 1f, 1.8f);
		new Title("  ", ChatColor.YELLOW + "Region Modified", ChatColor.GOLD + "Requires Rebuild").send(e.getPlayer());
	}
	
	public boolean contains(Location location)
	{
		return hunk.contains(location);
	}
	
	public boolean contains(Player player)
	{
		return hunk.contains(player);
	}
	
	public boolean contains(LivingEntity entity)
	{
		return hunk.contains(entity);
	}
	
	public Hunk getHunk()
	{
		return hunk;
	}
	
	public void setHunk(Hunk hunk)
	{
		this.hunk = hunk;
	}
	
	public Map getMap()
	{
		return map;
	}
	
	public void setMap(Map map)
	{
		this.map = map;
	}
	
	public UList<Location> getAccents()
	{
		return accents;
	}
	
	public void setAccents(UList<Location> accents)
	{
		this.accents = accents;
	}
	
	public UList<Capture> getCaptures()
	{
		return captures;
	}
	
	public void setCaptures(UList<Capture> captures)
	{
		this.captures = captures;
	}
	
	public Faction getFaction()
	{
		return faction;
	}
	
	public void setFaction(Faction faction)
	{
		this.faction = faction;
		accent(faction);
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
		hasSpawn = true;
	}
	
	public boolean hasSpawn()
	{
		return hasSpawn();
	}
	
	public void setHasSpawn(boolean s)
	{
		hasSpawn = s;
	}

	public String getBuildStatus()
	{
		return buildStatus;
	}

	public void setBuildStatus(String buildStatus)
	{
		this.buildStatus = buildStatus;
	}
}
