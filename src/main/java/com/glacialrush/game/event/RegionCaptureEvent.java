package com.glacialrush.game.event;

import com.glacialrush.composite.Faction;
import com.glacialrush.composite.Region;

public class RegionCaptureEvent extends RegionEvent
{
	private Faction faction;
	
	public RegionCaptureEvent(Region region, Faction faction)
	{
		super(region);
		
		this.faction = faction;
	}

	public Faction getFaction()
	{
		return faction;
	}

	public void setFaction(Faction faction)
	{
		this.faction = faction;
	}
}
