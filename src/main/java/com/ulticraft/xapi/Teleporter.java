package com.ulticraft.xapi;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Teleporter
{
	public static boolean safeTeleport(Player p, Location l)
	{
		Location c = l.clone();
		
		for(int i = 128; i > 0; i--)
		{
			c.subtract(0, 1, 0);
			
			if(!c.getBlock().equals(Material.AIR))
			{
				p.teleport(c);
				return true;
			}
		}
		
		return false;
	}
}
