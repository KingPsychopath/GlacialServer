package com.ulticraft.game.event;

import com.ulticraft.composite.Region;

public class RegionEvent extends MapEvent
{	
	private Region region;
	
	public RegionEvent(Region region)
	{
		super(region.getMap());
		
		this.region = region;
	}

	public Region getRegion()
	{
		return region;
	}

	public void setRegion(Region region)
	{
		this.region = region;
	}
}
