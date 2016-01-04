package com.ulticraft.xapi;

import org.bukkit.Location;

public class FastMath
{
	public static boolean isInRadius(Location a, Location b, float f)
	{
		return a.distanceSquared(b) <= f * f;
	}
	
	public static double distance2D(Location a, Location b)
	{
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow((a.getZ() - b.getZ()), 2));
	}
}
