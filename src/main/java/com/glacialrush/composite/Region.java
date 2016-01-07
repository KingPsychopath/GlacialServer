package com.glacialrush.composite;

import java.time.Duration;
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
import com.glacialrush.GlacialServer;
import com.glacialrush.api.dispatch.Title;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.object.GMap;
import com.glacialrush.component.ManipulationComponent;
import com.glacialrush.game.Game;
import com.glacialrush.game.event.RegionCaptureEvent;
import com.glacialrush.xapi.Cuboid;
import com.glacialrush.xapi.Cuboid.CuboidDirection;
import net.md_5.bungee.api.ChatColor;

public class Region implements Listener
{
	private GlacialServer pl;
	private Hunk hunk;
	private GList<Location> accents;
	private GList<Capture> captures;
	private Faction faction;
	private Map map;
	private String name;
	private Location spawn;
	private String buildStatus;
	private Boolean hasSpawn;
	private Boolean needsAccented = false;
	private Integer timer = -1;
	private Faction taking = Faction.neutral();
	private char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	private GMap<Player, Title> capturePanes;
	private static final int capTime = 2400;
	
	public Region(GlacialServer pl, Map map, String name, Location location)
	{
		this.pl = pl;
		this.map = map;
		this.name = name;
		this.hunk = new Hunk(location);
		this.accents = new GList<Location>();
		this.captures = new GList<Capture>();
		this.faction = Faction.neutral();
		this.buildStatus = "unbuilt";
		this.capturePanes = new GMap<Player, Title>();
		this.spawn = hunk.getCenter(64);
		this.hasSpawn = false;
		this.needsAccented = false;
		
		pl.register(this);
	}
	
	public Region(GlacialServer pl, Map map, String name, Hunk hunk)
	{
		this.pl = pl;
		this.map = map;
		this.name = name;
		this.hunk = hunk;
		this.accents = new GList<Location>();
		this.captures = new GList<Capture>();
		this.faction = Faction.neutral();
		this.buildStatus = "unbuilt";
		this.capturePanes = new GMap<Player, Title>();
		this.spawn = hunk.getCenter(64);
		this.hasSpawn = false;
		
		pl.register(this);
	}
	
	public void setPlayerCapturePane(Player p, Title title)
	{
		capturePanes.put(p, title);
	}
	
	public void tick()
	{
		if(!getGame().isRunning())
		{
			return;
		}
		
		boolean allDefended = true;
		
		for(Capture i : captures)
		{
			if(!i.getSecured().equals(faction))
			{
				allDefended = false;
			}
		}
		
		if(allDefended && timer == -1)
		{
			for(Player i : getPlayers())
			{
				Title t = new Title();
				
				for(Capture j : captures)
				{
					if(j.getPlayers().contains(i))
					{
						if(capturePanes.containsKey(i))
						{
							t.setTitle(capturePanes.get(i).getTitle());
							t.setSubtitle(capturePanes.get(i).getSubtitle());
							break;
						}
					}
				}
				
				t.setFadeInTime(0);
				t.setStayTime(5);
				t.setFadeOutTime(10);
				t.send(i);
				
				capturePanes.remove(i);
			}
			
			return;
		}
		
		if(timer == -1)
		{
			for(Player i : getPlayers())
			{
				Title t = new Title();
				
				for(Capture j : captures)
				{
					if(j.getPlayers().contains(i))
					{
						if(capturePanes.containsKey(i))
						{
							t.setTitle(capturePanes.get(i).getTitle());
							t.setSubtitle(capturePanes.get(i).getSubtitle());
							break;
						}
					}
				}
				
				t.setFadeInTime(0);
				t.setStayTime(5);
				t.setFadeOutTime(10);
				t.send(i);
				
				capturePanes.remove(i);
			}
		}
		
		GMap<Faction, Integer> cps = new GMap<Faction, Integer>();
		cps.put(Faction.omni(), 0);
		cps.put(Faction.enigma(), 0);
		cps.put(Faction.cryptic(), 0);
		cps.put(Faction.neutral(), 0);
		
		for(Capture i : captures)
		{
			cps.put(i.getSecured(), cps.get(i.getSecured()) + 1);
		}
		
		GList<Faction> fmf = Faction.all();
		fmf.remove(faction);
		
		if(cps.get(faction) > cps.get(fmf.get(0)) && cps.get(faction) > cps.get(fmf.get(1)))
		{
			taking = faction;
		}
		
		else if(cps.get(fmf.get(0)) > cps.get(fmf.get(1)))
		{
			taking = fmf.get(0);
		}
		
		else if(cps.get(fmf.get(0)) < cps.get(fmf.get(1)))
		{
			taking = fmf.get(1);
		}
		
		else
		{
			taking = Faction.neutral();
		}
		
		if(!taking.equals(Faction.neutral()))
		{
			if(timer == 0)
			{
				setFaction(taking);
				timer = -1;
				return;
			}
			
			if(timer == -1)
			{
				timer = capTime;
				return;
			}
			
			timer--;
		}
		
		else if(timer == 0)
		{
			setFaction(faction);
		}
		
		else
		{
			timer = capTime;
		}
		
		String m = "";
		int c = 0;
		Duration d = Duration.ofSeconds(timer / 20);
		
		m = m + ChatColor.AQUA + "Capture In" + ChatColor.DARK_AQUA + d.toString().replace('P', ':').replace('P', ':').replace('T', ' ').replace('M', ':').replace('S', ' ');
		
		for(Capture i : captures)
		{
			m = m + i.getSecured().getColor() + "[" + (alphabet[c] + "").toUpperCase() + "] ";
			
			c++;
		}
		
		if(timer == -1)
		{
			return;
		}
		
		for(Player i : getPlayers())
		{
			Title t = new Title();
			
			for(Capture j : captures)
			{
				if(j.getPlayers().contains(i))
				{
					if(capturePanes.containsKey(i))
					{
						t.setTitle(capturePanes.get(i).getTitle());
						t.setSubtitle(capturePanes.get(i).getSubtitle());
						break;
					}
				}
			}
			
			t.setFadeInTime(0);
			t.setStayTime(5);
			t.setFadeOutTime(10);
			t.setSubSubTitle(m);
			
			t.send(i);
			capturePanes.remove(i);
		}
	}
	
	public GList<Player> getPlayers()
	{
		GList<Player> pls = new GList<Player>();
		
		for(Player i : pl.onlinePlayers())
		{
			if(contains(i))
			{
				pls.add(i);
			}
		}
		
		return pls;
	}
	
	public Game getGame()
	{
		return pl.getGame();
	}
	
	public void draw(Player p, int level)
	{
		Cuboid c = hunk.getCuboid().flatten(level);
		
		hallucinate(p, c.getFace(CuboidDirection.North));
		hallucinate(p, c.getFace(CuboidDirection.South));
		hallucinate(p, c.getFace(CuboidDirection.East));
		hallucinate(p, c.getFace(CuboidDirection.West));
	}
	
	public void accentTask()
	{
		pl.scheduleSyncRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				if(needsAccented)
				{
					needsAccented = false;
					accent(getFaction());
				}
			}
		});
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
					Location m = it.next();
					
					pl.getManipulationComponent().add(new Manipulation(m, dye));
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
						captures.add(new Capture(Region.this, block.getLocation()));
						
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
	
	public GList<Location> getAccents()
	{
		return accents;
	}
	
	public void setAccents(GList<Location> accents)
	{
		this.accents = accents;
	}
	
	public GList<Capture> getCaptures()
	{
		return captures;
	}
	
	public void setCaptures(GList<Capture> captures)
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
		this.needsAccented = true;
		
		for(Capture i : captures)
		{
			i.setDefense(faction);
			i.setOffense(null);
			i.setState(100);
			i.setSecured(faction);
		}
		
		if(pl.getGame().isRunning() && !faction.equals(Faction.neutral()))
		{
			pl.callEvent(new RegionCaptureEvent(this, faction));
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
