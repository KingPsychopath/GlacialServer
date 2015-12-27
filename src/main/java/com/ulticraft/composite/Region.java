package com.ulticraft.composite;

import java.util.Iterator;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;
import com.ulticraft.GlacialRush;
import com.ulticraft.uapi.Cuboid;
import com.ulticraft.uapi.UList;

public class Region
{
	private Location br;
	private Location tl;
	private String name;
	private UList<Location> accents;
	private String buildStatus;
	private String accentStatus;
	private Integer buildTask;
	private Integer accentTask;
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
		
		tl.setY(0);
		br.setY(128);
	}
	
	public void setAccentsColor(final DyeColor dc)
	{
		if(!buildStatus.equals("finished") || !accentStatus.equals("unbuilt"))
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
}
