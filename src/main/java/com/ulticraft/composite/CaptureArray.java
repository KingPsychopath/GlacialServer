package com.ulticraft.composite;

import com.ulticraft.uapi.UList;

public class CaptureArray
{
	private UList<Capture> capturePoints;
	
	public CaptureArray()
	{
		
	}

	public UList<Capture> getCapturePoints()
	{
		return capturePoints;
	}

	public void setCapturePoints(UList<Capture> capturePoints)
	{
		this.capturePoints = capturePoints;
	}
}
