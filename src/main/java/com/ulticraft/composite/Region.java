package com.ulticraft.composite;

import java.util.Iterator;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import com.ulticraft.GlacialRush;
import com.ulticraft.uapi.Cuboid;
import com.ulticraft.uapi.Cuboid.CuboidDirection;
import com.ulticraft.uapi.UList;

public class Region
{
	private Location br;
	private Location tl;
	private Location spawn;
	private String name;
	private UList<Location> accents;
	private UList<Capture> captures;
	private String buildStatus;
	private String accentStatus;
	private Integer buildTask;
	private Integer accentTask;
	private Faction dominantFaction;
	private GlacialRush pl;
	
	public Region(GlacialRush pl, Location br)
	{
		this.br = br;
		this.tl = br.clone().add(new Vector(48, 0, 48));
		this.name = "Unnamed";
		this.accents = new UList<Location>();
		this.buildStatus = "unbuilt";
		this.accentStatus = "unbuilt";
		this.pl = pl;
		this.dominantFaction = Faction.neutral();
		this.captures = new UList<Capture>();
		
		tl.setY(0);
		br.setY(128);
	}
	
	public void draw(Player p)
	{
		Location tt = tl.clone();
		Location bb = br.clone();
		
		tt.setY(p.getLocation().getY());
		bb.setY(p.getLocation().getY());
		
		Cuboid c = new Cuboid(tt, bb);
		
		hallucinate(p, c.getFace(CuboidDirection.North).iterator());
		hallucinate(p, c.getFace(CuboidDirection.South).iterator());
		hallucinate(p, c.getFace(CuboidDirection.East).iterator());
		hallucinate(p, c.getFace(CuboidDirection.West).iterator());
	}
	
	public void hallucinate(final Player p, final Iterator<Block> it)
	{
		final int[] task = {0};
		final Long ms = System.currentTimeMillis();
		
		task[0] = pl.scheduleSyncRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				while(it.hasNext() && System.currentTimeMillis() - ms < 5)
				{
					Location ll = it.next().getLocation();
					pl.getManipulationComponent().add(new Manipulation(ll, Material.SEA_LANTERN, p));
				}
			}
		});
	}
	
	public void setAccentsColor(final DyeColor dc)
	{
		if(!buildStatus.equals("finished"))
		{
			return;
		}
		
		if(!(accentStatus.equals("unbuilt") || accentStatus.equals("finished")))
		{
			return;
		}
		
		final Iterator<Location> it = accents.iterator();
		final Long ms = System.currentTimeMillis();
		accentStatus = "building";

		accentTask = pl.scheduleSyncRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				while(it.hasNext() && System.currentTimeMillis() - ms < 30)
				{
					Location l = it.next();
					
					pl.getManipulationComponent().add(new Manipulation(l, dc));
				}
				
				if(!it.hasNext())
				{
					pl.cancelTask(accentTask);
					accentStatus = "finished";
					return;
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
		
		if(accentStatus.equals("building"))
		{
			return;
		}
		
		Cuboid c = new Cuboid(tl, br);

		final Iterator<Block> it = c.iterator();
		final Long ms = System.currentTimeMillis();
		
		buildStatus = "building";
		accents.clear();
		buildTask = pl.scheduleSyncRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				while(it.hasNext() && System.currentTimeMillis() - ms < 30)
				{
					Block block = it.next();
					
					if(block.getType().equals(Material.WOOL) || block.getType().equals(Material.STAINED_GLASS) || block.getType().equals(Material.STAINED_GLASS_PANE) || block.getType().equals(Material.STAINED_CLAY) || block.getType().equals(Material.BANNER) || block.getType().equals(Material.STANDING_BANNER) || block.getType().equals(Material.WALL_BANNER) || block.getType().equals(Material.CARPET))
					{
						accents.add(block.getLocation());
					}
					
					if(block.getType().equals(Material.BEACON))
					{
						captures.add(new Capture(block.getLocation()));
					}
				}
				
				if(!it.hasNext())
				{
					buildStatus = "finished";
					pl.cancelTask(buildTask);
					return;
				}
			}
		});
	}
	
	public void setFactionCapture(Location l, Faction dominantFaction)
	{
		for(Capture i : captures)
		{
			if(i.getLocation().equals(l))
			{
				i.setDominantFaction(dominantFaction);
			}
		}
	}
	
	public void setDominantFaction(Faction dominantFaction)
	{
		setAccentsColor(dominantFaction.getDyeColor());
		this.dominantFaction = dominantFaction;
	}

	public UList<Capture> getCaptures()
	{
		return captures;
	}

	public void setCaptures(UList<Capture> captures)
	{
		this.captures = captures;
	}

	public Faction getDominantFaction()
	{
		return dominantFaction;
	}

	public Location getBr()
	{
		return br;
	}

	public void setBr(Location br)
	{
		this.br = br;
	}

	public Location getTl()
	{
		return tl;
	}

	public void setTl(Location tl)
	{
		this.tl = tl;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public UList<Location> getAccents()
	{
		return accents;
	}

	public void setAccents(UList<Location> accents)
	{
		this.accents = accents;
	}

	public String getBuildStatus()
	{
		return buildStatus;
	}

	public void setBuildStatus(String buildStatus)
	{
		this.buildStatus = buildStatus;
	}

	public String getAccentStatus()
	{
		return accentStatus;
	}

	public void setAccentStatus(String accentStatus)
	{
		this.accentStatus = accentStatus;
	}

	public Integer getBuildTask()
	{
		return buildTask;
	}

	public void setBuildTask(Integer buildTask)
	{
		this.buildTask = buildTask;
	}

	public Integer getAccentTask()
	{
		return accentTask;
	}

	public void setAccentTask(Integer accentTask)
	{
		this.accentTask = accentTask;
	}

	public Location getSpawn()
	{
		return spawn;
	}

	public void setSpawn(Location spawn)
	{
		this.spawn = spawn;
	}
}
