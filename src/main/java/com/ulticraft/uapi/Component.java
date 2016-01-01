package com.ulticraft.uapi;

import com.ulticraft.GlacialServer;

public class Component
{
	protected final GlacialServer pl;
	private boolean enabled;
	
	public Component(GlacialServer pl)
	{
		this.pl = pl;
		enabled = false;
	}
	
	public void enable()
	{
		enabled = true;
	}
	
	public void disable()
	{
		enabled = false;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
}
