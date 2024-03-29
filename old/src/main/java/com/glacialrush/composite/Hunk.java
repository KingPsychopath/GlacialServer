package com.glacialrush.composite;

import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import com.glacialrush.api.object.GList;
import com.glacialrush.xapi.Cuboid;
import com.glacialrush.xapi.FastMath;

public class Hunk implements Cloneable
{
	public static final int div = 96;
	
	public enum HunkFace
	{
		NORTH, SOUTH, WEST, EAST
	}
	
	protected Integer x;
	protected Integer z;
	protected World world;
	protected Cuboid cuboid;
	
	public Hunk(Location location)
	{
		this.x = location.getBlockX() / div;
		this.z = location.getBlockZ() / div;
		
		this.world = location.getWorld();
		this.cuboid = new Cuboid(world, x * div, 0, z * div, ((x + 1) * div) - 1, 128, ((z + 1) * div) - 1);
	}
	
	public Hunk(Hunk hunk)
	{
		this.x = hunk.x;
		this.z = hunk.z;
		
		this.world = hunk.getWorld();
		this.cuboid = new Cuboid(world, x * div, 0, z * div, ((x + 1) * div) - 1, 128, ((z + 1) * div) - 1);
	}
	
	public Hunk(int x, int z, World world)
	{
		this.x = x;
		this.z = z;
		this.world = world;
		this.cuboid = new Cuboid(world, x * div, 0, z * div, ((x + 1) * div) - 1, 128, ((z + 1) * div) - 1);
	}
	
	public double distanceBlocks(Hunk hunk)
	{
		return FastMath.distance2D(this.getCenter(0), hunk.getCenter(0));
	}
	
	public double distanceHunks(Hunk hunk)
	{
		return distanceBlocks(hunk) / (double) div;
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
		return new Location(world, (x * div) - (div / 2), level, (z * div) - (div / 2));
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
	
	public GList<Hunk> connected()
	{
		return new GList<Hunk>().qadd(getRelative(HunkFace.NORTH)).qadd(getRelative(HunkFace.EAST)).qadd(getRelative(HunkFace.SOUTH)).qadd(getRelative(HunkFace.WEST));
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
