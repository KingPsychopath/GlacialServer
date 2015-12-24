package com.ulticraft.composite;

import java.io.Serializable;
import com.ulticraft.map.Region;

public class RegionData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int x;
	private int z;
	
	public RegionData(Region region)
	{
		name = region.getName();
		x = region.getCenterChunk().getX();
		z = region.getCenterChunk().getZ();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getZ()
	{
		return z;
	}

	public void setZ(int z)
	{
		this.z = z;
	}
}
