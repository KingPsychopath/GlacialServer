package com.ulticraft.composite;

import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import com.ulticraft.uapi.Cuboid;

public class Hunk implements Cloneable
{
	public static final int div = 45;
	
	public enum HunkFace
	{
		NORTH, SOUTH, WEST, EAST
	}
	
	private Integer x;
	private Integer z;
	private World world;
	private Cuboid cuboid;
	
	public Hunk(Location location)
	{
		this.x = location.getBlockX() / div;
		this.z = location.getBlockZ() / div;
		this.world = location.getWorld();
		this.cuboid = new Cuboid(world, x * div, 0, z * div, ((x + 1) * div) - 1, 128, ((z + 1) * div) - 1);
	}
	
	public Hunk(int x, int z, World world)
	{
		this.x = x;
		this.z = z;
		this.world = world;
		this.cuboid = new Cuboid(world, x * div, 0, z * div, ((x + 1) * div) - 1, 128, ((z + 1) * div) - 1);
	}
	
	public Cuboid getCuboid()
	{
		return cuboid;
	}

	public void setCuboid(Cuboid cuboid)
	{
		this.cuboid = cuboid;
	}

	public Location getCenter(int level)
	{
		return new Location(world, (x * div) - (div / 2), level, (x * div) - (div / 2));
	}
	
	public Hunk getRelative(HunkFace face)
	{
		if(face.equals(HunkFace.NORTH))
		{
			return new Hunk(x, z + 1, world);
		}
		
		else if(face.equals(HunkFace.SOUTH))
		{
			return new Hunk(x, z - 1, world);
		}
		
		else if(face.equals(HunkFace.EAST))
		{
			return new Hunk(x + 1, z, world);
		}
		
		else if(face.equals(HunkFace.WEST))
		{
			return new Hunk(x - 1, z, world);
		}
		
		else
		{
			return null;
		}
	}
	
	public Iterator<Block> iterator()
	{
		return cuboid.iterator();
	}
	
	public Hunk clone()
	{
		return new Hunk(x, z, world);
	}
	
	public boolean contains(Location location)
	{
		return this.equals(new Hunk(location));
	}
	
	public boolean contains(Block block)
	{
		return contains(block.getLocation());
	}
	
	public boolean contains(Player player)
	{
		return contains(player.getLocation());
	}
	
	public boolean contains(LivingEntity entity)
	{
		return contains(entity.getLocation());
	}
	
	public boolean equals(Object o)
	{
		if(o instanceof Hunk)
		{
			Hunk h = (Hunk) o;
			
			if(h.getX() == x && h.getZ() == z && h.getWorld().equals(world))
			{
				return true;
			}
		}
		
		return false;
	}

	public Integer getX()
	{
		return x;
	}

	public void setX(Integer x)
	{
		this.x = x;
	}

	public Integer getZ()
	{
		return z;
	}

	public void setZ(Integer z)
	{
		this.z = z;
	}

	public World getWorld()
	{
		return world;
	}

	public void setWorld(World world)
	{
		this.world = world;
	}
}
