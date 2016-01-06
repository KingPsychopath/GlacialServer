package com.glacialrush.game.event;

import com.glacialrush.composite.Capture;

public class CapturePointEvent extends RegionEvent
{
	private Capture capturePoint;
	
	public CapturePointEvent(Capture capturePoint)
	{
		super(capturePoint.getRegion());
		
		this.capturePoint = capturePoint;
	}
	
	public Capture getCapturePoint()
	{
		return capturePoint;
	}
	
	public void setCapturePoint(Capture capturePoint)
	{
		this.capturePoint = capturePoint;
	}
}
