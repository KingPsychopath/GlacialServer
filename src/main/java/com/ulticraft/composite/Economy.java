package com.ulticraft.composite;

import com.ulticraft.uapi.Table;

public class Economy implements Table
{
	private Integer xp;
	private Integer credits;
	private Integer percent;
	
	public Economy()
	{
		
	}

	public Integer getXp()
	{
		return xp;
	}

	public void setXp(Integer xp)
	{
		this.xp = xp;
	}

	public Integer getCredits()
	{
		return credits;
	}

	public void setCredits(Integer credits)
	{
		this.credits = credits;
	}

	public Integer getPercent()
	{
		return percent;
	}

	public void setPercent(Integer percent)
	{
		this.percent = percent;
	}
}