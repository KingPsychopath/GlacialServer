package com.ulticraft.composite;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import com.ulticraft.GlacialRush;
import com.ulticraft.uapi.UList;

public class Region
{
	private Location br;
	private Location tl;
	private String name;
	private UList<Location> accents;
	private String buildStatus;
	private Integer buildTask;
	private GlacialRush pl;
	
	public Region(GlacialRush pl, Location br)
	{
		this.br = br;
		this.tl = br.clone().add(new Vector(48, 0, 48));
		this.name = "Unnamed";
		this.accents = new UList<Location>();
		this.buildStatus = "Unbuilt";
		this.pl = pl;
	}
	
	public void build()
	{
		final int[] xyz = new int[3];
		
		xyz[0] = br.getBlockX();
		xyz[1] = 0;
		xyz[2] = br.getBlockZ();
		
		buildTask = pl.scheduleSyncRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				long ms = System.currentTimeMillis();
				
				
			}
		});
	}
}
