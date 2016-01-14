package com.glacialrush.game.event;

import com.glacialrush.component.GameController;
import com.glacialrush.composite.Capture;
import com.glacialrush.composite.Faction;

public class FactionRegionCaptureEvent extends CaptureEvent
{
	private Faction faction;
	
	public FactionRegionCaptureEvent(GameController gameController, Capture capture, Faction faction)
	{
		super(gameController, capture);
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
