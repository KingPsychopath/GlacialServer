package com.ulticraft.component;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.ulticraft.GlacialRush;
import com.ulticraft.composite.BlockManipulation;
import com.ulticraft.composite.BlockPacketManipulation;
import com.ulticraft.composite.ColorManipulation;
import com.ulticraft.composite.Manipulation;
import com.ulticraft.uapi.ColorUtils;
import com.ulticraft.uapi.Component;
import com.ulticraft.uapi.UList;
import net.md_5.bungee.api.ChatColor;

public class WorldComponent extends Component
{
	private UList<Manipulation> jobs;
	
	public WorldComponent(GlacialRush pl)
	{
		super(pl);
		
		jobs = new UList<Manipulation>();
	}
	
	public void enable()
	{
		pl.scheduleSyncRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				for(int i = 0; i < 19; i++)
				{
					if(jobs.isEmpty())
					{
						return;
					}
					
					jobs.get(0).manipulate();
					jobs.remove(0);
				}
			}
		});
	}
	
	public void disable()
	{
		
	}
	
	public void addJob(Location location, Material material)
	{
		jobs.add(new BlockManipulation(location, material));
	}
	
	public void addJob(Player player, Location location, Material material)
	{
		jobs.add(new BlockPacketManipulation(location, material, player));
	}
	
	public void addJob(Location location, DyeColor dye)
	{
		jobs.add(new ColorManipulation(location, dye));
	}
	
	public void addJob(Location location, ChatColor dye)
	{
		jobs.add(new ColorManipulation(location, ColorUtils.chatToDye(dye)));
	}
}
