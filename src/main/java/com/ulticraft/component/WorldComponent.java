package com.ulticraft.component;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.ulticraft.GlacialRush;
import com.ulticraft.composite.Manipulation;
import com.ulticraft.uapi.ColorUtils;
import com.ulticraft.uapi.Component;
import com.ulticraft.uapi.Title;
import com.ulticraft.uapi.UList;
import net.md_5.bungee.api.ChatColor;

public class WorldComponent extends Component
{
	private UList<Manipulation> jobs;
	private int speed;
	
	public WorldComponent(GlacialRush pl)
	{
		super(pl);
		
		jobs = new UList<Manipulation>();
		speed = 50;
	}
	
	public void enable()
	{
		pl.scheduleSyncRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				long ms = 0;
				long sms = System.currentTimeMillis();
				
				if(jobs.size() > 30000)
				{
					speed = 40;
				}
				
				else if(jobs.size() > 10000)
				{
					speed = 30;
				}
				
				else if(jobs.size() > 5000)
				{
					speed = 20;
				}
				
				else if(jobs.size() > 3000)
				{
					speed = 10;
				}
				
				else if(jobs.size() > 1000)
				{
					speed = 4;
				}
				
				else
				{
					speed = 1;
				}
				
				for(int k = 0; k < speed; k++)
				{
					for(int i = 0; i < 50; i++)
					{
						if(jobs.isEmpty())
						{
							return;
						}
						
						jobs.get(0).manipulate();
						jobs.remove(0);
						
						Title t = new Title();
						t.setSubSubTitle(ChatColor.AQUA + "Working on Jobs: " + ChatColor.DARK_AQUA + jobs.size() + " Remaining.");
						
						if(speed > 1)
						{
							t.setSubtitle(ChatColor.RED + "" + ChatColor.UNDERLINE + "OVERCLOCK: " + speed + "X");
							
						}
						
						t.setFadeInTime(0);
						t.setFadeOutTime(30);
						t.setStayTime(30);
						
						t.send();
						
						ms = System.currentTimeMillis() - sms;
					}
				}
			}
		});
	}
	
	public void disable()
	{
		
	}
	
	public void addJob(Location location, Material material)
	{
		jobs.add(new Manipulation(location, material));
	}
	
	public void addJob(Player player, Location location, Material material)
	{
		jobs.add(new Manipulation(location, material, player));
	}
	
	public void addJob(Location location, DyeColor dye)
	{
		jobs.add(new Manipulation(location, dye));
	}
	
	public void addJob(Location location, ChatColor dye)
	{
		jobs.add(new Manipulation(location, ColorUtils.chatToDye(dye)));
	}
}
