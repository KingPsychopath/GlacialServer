package com.glacialrush.composite;

import java.util.Collections;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.dispatch.notification.Notification;
import com.glacialrush.api.dispatch.notification.NotificationPriority;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.object.GMap;
import com.glacialrush.api.object.GSound;
import com.glacialrush.api.thread.GlacialTask;
import com.glacialrush.component.CommandController;
import com.glacialrush.composite.data.MapData;
import com.glacialrush.composite.data.RegionData;
import net.md_5.bungee.api.ChatColor;

public class Map implements Listener
{
	protected World world;
	protected Faction faction;
	protected String name;
	protected GList<Region> regions;
	protected GlacialServer pl;
	protected GMap<Faction, Region> spawns;
	protected Boolean needsBuilt;
	protected Boolean locked;
	
	public Map(GlacialServer pl, World world, String name)
	{
		this.name = name;
		this.regions = new GList<Region>();
		this.world = world;
		this.pl = pl;
		this.faction = Faction.neutral();
		this.needsBuilt = false;
		this.spawns = new GMap<Faction, Region>();
		this.locked = false;
		pl.register(this);
	}
	
	public MapData getData()
	{
		GList<RegionData> rgs = new GList<RegionData>();
		
		for(Region i : regions)
		{
			rgs.add(i.getData());
		}
		
		return new MapData(name, world.getName(), rgs, locked);
	}
	
	public GList<Region> getCapturableRegions()
	{
		GList<Region> rgs = new GList<Region>();
		
		for(Region i : regions)
		{
			if(i.isCaptureable())
			{
				rgs.add(i);
			}
		}
		
		return rgs;
	}
	
	public GList<Region> getUncapturableRegions()
	{
		GList<Region> rgs = new GList<Region>();
		
		for(Region i : regions)
		{
			if(!i.isCaptureable())
			{
				rgs.add(i);
			}
		}
		
		return rgs;
	}
	
	public void handleUncapturableRegions()
	{
		for(Region i : getUncapturableRegions())
		{
			Faction f = null;
			
			for(Region j : i.connectedRegions())
			{
				if(j.isCaptureable())
				{
					if(f == null)
					{
						f = j.getFaction();
					}
					
					else if(!f.equals(j.getFaction()))
					{
						return;
					}
				}
			}
			
			if(f != null)
			{
				if(!i.getFaction().equals(f))
				{
					Faction lost = i.getFaction();
					i.setFaction(f);
					i.accent();
					
					Notification n = new Notification().setPriority(NotificationPriority.HIGHEST);
					n.setTitle(i.getFaction().getColor() + "Village Captured");
					n.setSubTitle(i.getFaction().getColor() + i.getFaction().getName() + " <> " + i.getName());
					n.setFadeIn(5);
					n.setStayTime(30);
					n.setSound(new GSound("g.event.region.capture"));
					n.setFadeOut(20);
					
					Notification n2 = new Notification().setPriority(NotificationPriority.HIGHEST);
					n2.setTitle(i.getFaction().getColor() + "Village Lost");
					n2.setSubTitle(i.getFaction().getColor() + i.getFaction().getName() + " <> " + i.getName());
					n2.setFadeIn(5);
					n2.setStayTime(30);
					n2.setFadeOut(20);
					n2.setSound(new GSound("g.event.region.lost"));
					
					for(Player j : i.getPlayers())
					{
						if(pl.getGameController().getPlayerHandler().getFaction(j).equals(i.getFaction()))
						{
							pl.getNotificationController().dispatch(n, pl.getNotificationController().getMapChannel(), j);
						}
						
						if(pl.getGameController().getPlayerHandler().getFaction(j).equals(lost))
						{
							pl.getNotificationController().dispatch(n2, pl.getNotificationController().getMapChannel(), j);
						}
					}
					
				}
			}
		}
	}
	
	public void addRegion(Player p)
	{
		if(locked)
		{
			p.sendMessage(ChatColor.RED + "Map Locked, Unlock it to add region.");
			return;
		}
		
		CommandController c = pl.getCommandController();
		
		if(!world.equals(p.getWorld()))
		{
			c.f(p, "Cannot Create 4th dimensional maps.");
			return;
		}
		
		if(isBuilding() || isAccenting())
		{
			c.f(p, "The Map is currently building.");
			return;
		}
		
		Hunk hunk = new Hunk(pl.target(p));
		
		if(regions.isEmpty())
		{
			Region region = new Region(pl.target(p), this);
			region.draw(p);
			regions.add(region);
			return;
		}
		
		if(getRegion(hunk) != null)
		{
			c.f(p, "That is already a region.");
			return;
		}
		
		if(!hasNeighbors(hunk))
		{
			c.f(p, "Regions must have a connected region.");
			return;
		}
		
		Region region = new Region(pl.target(p), this);
		region.draw(p);
		regions.add(region);
	}
	
	public int buildProgress()
	{
		int b = 0;
		
		for(Region i : regions)
		{
			if(!i.isBuilding())
			{
				b++;
			}
		}
		
		if(b == 0)
		{
			return 0;
		}
		
		return (int) (100 * ((double) b / (double) (regions.size() - 1)));
	}
	
	public int accentProgress()
	{
		int b = 0;
		
		for(Region i : regions)
		{
			if(!i.isAccenting())
			{
				b++;
			}
		}
		
		if(b == 0)
		{
			return 100;
		}
		
		return (int) (100 * ((double) b / (double) (regions.size() - 1)));
	}
	
	public int buildAccentProgress()
	{
		int b = 0;
		
		for(Region i : regions)
		{
			if(!i.isAccenting() && !i.isBuilding())
			{
				b++;
			}
		}
		
		if(b == 0)
		{
			return 0;
		}
		
		return (int) (100 * ((double) b / (double) regions.size()));
	}
	
	public Region getRegion(Player p)
	{
		for(Region i : regions)
		{
			if(i.contains(p))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public boolean hasNeighbors(Hunk hunk)
	{
		for(Hunk i : hunk.connected())
		{
			if(getRegion(i) != null)
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
		
		for(Region i : regions)
		{
			i.accent();
		}
	}
	
	public void accent(Faction faction)
	{
		if(isBuilding() || isAccenting())
		{
			return;
		}
		
		for(Region i : regions)
		{
			i.setFaction(faction);
			i.accent();
		}
	}
	
	public GMap<MapCorner, Region> getCorners()
	{
		GMap<MapCorner, Region> corners = new GMap<MapCorner, Region>();
		
		for(MapCorner i : MapCorner.values())
		{
			Region r = getRegion(new Hunk(i.isX() ? maxX() : minX(), i.isZ() ? maxZ() : minZ(), getWorld()));
			
			if(r != null && r.isCaptureable())
			{
				corners.put(i, r);
			}
		}
		
		return corners;
	}
	
	public int maxX()
	{
		int m = Integer.MIN_VALUE;
		
		for(Region i : regions)
		{
			if(i.getX() > m)
			{
				m = i.getX();
			}
		}
		
		return m;
	}
	
	public int maxZ()
	{
		int m = Integer.MIN_VALUE;
		
		for(Region i : regions)
		{
			if(i.getZ() > m)
			{
				m = i.getZ();
			}
		}
		
		return m;
	}
	
	public int minX()
	{
		int m = Integer.MAX_VALUE;
		
		for(Region i : regions)
		{
			if(i.getX() < m)
			{
				m = i.getX();
			}
		}
		
		return m;
	}
	
	public int minZ()
	{
		int m = Integer.MAX_VALUE;
		
		for(Region i : regions)
		{
			if(i.getZ() < m)
			{
				m = i.getZ();
			}
		}
		
		return m;
	}
	
	public void randomizeSpawns()
	{
		GMap<MapCorner, Region> corners = getCorners();
		
		if(corners.size() < 3)
		{
			pl.f("Not enough regions to randomize spawns");
			return;
		}
		
		GList<Region> spawns = new GList<Region>();
		GList<Faction> factions = Faction.all();
		
		for(Region i : corners.values())
		{
			spawns.add(i);
		}
		
		Collections.shuffle(spawns);
		Collections.shuffle(factions);
		
		this.spawns.clear();
		
		for(int i = 0; i < 3; i++)
		{
			this.spawns.put(factions.get(i), spawns.get(i));
		}
	}
	
	public void neutralize()
	{
		for(Region i : regions)
		{
			i.setFaction(Faction.neutral());
		}
	}
	
	public boolean check(Player p)
	{
		if(needsBuilt)
		{
			p.sendMessage(ChatColor.RED + "Map needs built.");
			return false;
		}
		
		for(Region i : regions)
		{
			if(!i.check(p))
			{
				return false;
			}
		}
		
		return true;
	}
	
	public GMap<Faction, Region> getSpawns()
	{
		return spawns;
	}
	
	public void setSpawns(GMap<Faction, Region> spawns)
	{
		this.spawns = spawns;
	}
	
	public Boolean getNeedsBuilt()
	{
		return needsBuilt;
	}
	
	public void setNeedsBuilt(Boolean needsBuilt)
	{
		this.needsBuilt = needsBuilt;
	}
	
	public Boolean getLocked()
	{
		return locked;
	}
	
	public void setLocked(Boolean locked)
	{
		this.locked = locked;
	}
	
	public void accentEvenley()
	{
		if(isBuilding() || isAccenting())
		{
			return;
		}
		
		neutralize();
		randomizeSpawns();
		
		for(Faction i : spawns.keySet())
		{
			spawns.get(i).setFaction(i);
			spawns.get(i).accent();
		}
		
		while(hasNeutralRegions())
		{
			for(Faction i : spawns.keySet())
			{
				captureFromSpawn(i);
			}
		}
		
		accent();
	}
	
	public void captureFromSpawn(Faction faction)
	{
		if(!spawns.containsKey(faction) || !hasNeutralRegions())
		{
			return;
		}
		
		Region region = null;
		Double distance = Double.MAX_VALUE;
		
		for(Region i : getCapturableRegions())
		{
			if(i.isNeutral() && i.connectedTo(faction))
			{
				Double dm = i.distanceBlocks(spawns.get(faction));
				
				if(dm < distance)
				{
					region = i;
					distance = dm;
				}
			}
		}
		
		if(region != null)
		{
			region.setFaction(faction);
		}
	}
	
	public GList<Region> getRegions(Faction faction)
	{
		GList<Region> reg = new GList<Region>();
		
		for(Region i : regions)
		{
			if(i.getFaction().equals(faction))
			{
				reg.add(i);
			}
		}
		
		return reg;
	}
	
	public boolean hasNeutralRegions()
	{
		for(Region i : getCapturableRegions())
		{
			if(i.getFaction().equals(Faction.neutral()))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean contains(Player player)
	{
		return contains(player.getLocation());
	}
	
	public boolean contains(Location location)
	{
		for(Region i : regions)
		{
			if(i.contains(location))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public void setup()
	{
		build();
		
		pl.newThread(new GlacialTask("Map Reset Monitor")
		{
			public void run()
			{
				if(isBuilding())
				{
					setDelay(20);
				}
				
				else
				{
					accentEvenley();
					
					pl.newThread(new GlacialTask("Region Reset Monitor")
					{
						public void run()
						{
							if(!isAccenting())
							{
								stop();
								return;
							}
						}
					});
					
					stop();
					return;
				}
			}
		});
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onModification(BlockPlaceEvent e)
	{
		if(pl.getGameController().isRunning())
		{
			e.setCancelled(true);
			return;
		}
		
		if(!contains(e.getBlock().getLocation()))
		{
			return;
		}
		
		if(locked)
		{
			e.setCancelled(true);
			return;
		}
		
		if(!needsBuilt)
		{
			needsBuilt = true;
			
			e.getPlayer().sendMessage(ChatColor.RED + name + " Modified, needs built.");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onModification(BlockBreakEvent e)
	{
		if(pl.getGameController().isRunning())
		{
			e.setCancelled(true);
			return;
		}
		
		if(!contains(e.getBlock().getLocation()))
		{
			return;
		}
		
		if(locked)
		{
			e.setCancelled(true);
			return;
		}
		
		if(!needsBuilt)
		{
			needsBuilt = true;
			
			e.getPlayer().sendMessage(ChatColor.RED + name + " Modified, needs built.");
		}
	}
	
	public void build()
	{
		if(isBuilding())
		{
			return;
		}
		
		for(Region i : regions)
		{
			i.build();
		}
		
		needsBuilt = false;
	}
	
	public void draw(Player p)
	{
		for(Region i : regions)
		{
			i.draw(p);
		}
	}
	
	public Region getRegion(Hunk hunk)
	{
		for(Region i : regions)
		{
			if(i.x == hunk.x && i.z == hunk.z)
			{
				return i;
			}
		}
		
		return null;
	}
	
	public boolean hasRegion(Hunk hunk)
	{
		return getRegion(hunk) == null ? false : true;
	}
	
	public boolean isBuilding()
	{
		for(Region i : regions)
		{
			if(i.isBuilding())
			{
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isAccenting()
	{
		for(Region i : regions)
		{
			if(i.isAccenting())
			{
				return true;
			}
		}
		
		return false;
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
	
	public World getWorld()
	{
		return world;
	}
	
	public void setWorld(World world)
	{
		this.world = world;
	}
	
	public Faction getFaction()
	{
		return faction;
	}
	
	public void setFaction(Faction faction)
	{
		this.faction = faction;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public GList<Region> getRegions()
	{
		return regions;
	}
	
	public void setRegions(GList<Region> regions)
	{
		this.regions = regions;
	}
}
