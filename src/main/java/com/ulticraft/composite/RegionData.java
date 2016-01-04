package com.ulticraft.composite;

import java.io.Serializable;

public class RegionData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Integer hx;
	private Integer hz;
	
	public RegionData(String name, Integer hx, Integer hz)
	{
		this.name = name;
		this.hx = hx;
		this.hz = hz;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Integer getHx()
	{
		return hx;
	}
	
	public void setHx(Integer hx)
	{
		this.hx = hx;
	}
	
	public Integer getHz()
	{
		return hz;
	}
	
	public void setHz(Integer hz)
	{
		this.hz = hz;
	}
}
