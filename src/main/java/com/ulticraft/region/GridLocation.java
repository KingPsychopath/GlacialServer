package com.ulticraft.region;

import org.bukkit.Location;
import org.bukkit.World;

public class GridLocation
{
	private int x;
	private int y;
	
	public GridLocation(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public GridLocation(Location location)
	{
		this.x = location.getBlockX();
		this.y = location.getBlockZ();
	}

	public Location toLocation(World world, int height)
	{
		return new Location(world, x, height, y);
	}
	
	public double distanceSquared(GridLocation from)
	{
		return Math.pow(x - from.getX(), 2) + Math.pow(y - from.getY(), 2);
	}
	
	public double distance(GridLocation from)
	{
		return Math.sqrt(distanceSquared(from));
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}
	
	public boolean equals(GridLocation gl)
	{
		return gl.getX() == x && gl.getY() == y;
	}
}
