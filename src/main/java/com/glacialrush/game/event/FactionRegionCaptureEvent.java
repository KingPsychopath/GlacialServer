package com.glacialrush.game.event;

import com.glacialrush.component.GameController;
import com.glacialrush.composite.Faction;
import com.glacialrush.composite.Region;

public class FactionRegionCaptureEvent extends RegionEvent
{
	private Faction faction;
	
	public FactionRegionCaptureEvent(GameController gameController, Region region, Faction faction)
	{
		super(gameController, region);
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
