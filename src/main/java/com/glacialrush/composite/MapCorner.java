package com.glacialrush.composite;

public enum MapCorner
{
	NORTH_EAST(true, true),
	NORTH_WEST(true, false),
	SOUTH_EAST(false, true),
	SOUTH_WEST(false, false);
	
	private boolean x;
	private boolean z;
	
	private MapCorner(boolean x, boolean z)
	{
		this.x = x;
		this.z = z;
	}

	public boolean isX()
	{
		return x;
	}
	
	public boolean isZ()
	{
		return z;
	}
}
