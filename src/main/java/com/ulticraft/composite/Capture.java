package com.ulticraft.composite;

public class Capture
{
	private CaptureMode mode;
	private Faction captured;
	
	public Capture()
	{
		
	}

	public CaptureMode getMode()
	{
		return mode;
	}

	public void setMode(CaptureMode mode)
	{
		this.mode = mode;
	}

	public Faction getCaptured()
	{
		return captured;
	}

	public void setCaptured(Faction captured)
	{
		this.captured = captured;
	}
}
