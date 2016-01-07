package com.glacialrush.composite;

import java.io.Serializable;
import com.glacialrush.xapi.ULocation;

public class RegionData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private ULocation spawn;
	private Integer hx;
	private Integer hz;
	
	public RegionData(String name, ULocation spawn, Integer hx, Integer hz)
	{
		this.name = name;
		this.spawn = spawn;
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
	
	public ULocation getSpawn()
	{
		return spawn;
	}

	public void setSpawn(ULocation spawn)
	{
		this.spawn = spawn;
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
