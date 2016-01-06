package com.glacialrush.game.event;

import com.glacialrush.composite.Region;

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
