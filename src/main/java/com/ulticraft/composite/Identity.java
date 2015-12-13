package com.ulticraft.composite;

import com.ulticraft.uapi.Table;

public class Identity implements Table
{
	private String UUID;
	private String name;
	
	public Identity()
	{
		
	}

	public String getUUID()
	{
		return UUID;
	}

	public void setUUID(String uUID)
	{
		UUID = uUID;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
