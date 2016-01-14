package com.glacialrush.game.event;

import com.glacialrush.component.GameController;
import com.glacialrush.composite.Capture;

public class CaptureEvent extends RegionEvent
{
	private Capture capture;
	
	public CaptureEvent(GameController gameController, Capture capture)
	{
		super(gameController, capture.getRegion());
		
		this.capture = capture;
	}

	public Capture getCapture()
	{
		return capture;
	}

	public void setCapture(Capture capture)
	{
		this.capture = capture;
	}
}
