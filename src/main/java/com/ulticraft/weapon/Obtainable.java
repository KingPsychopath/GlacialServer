package com.ulticraft.weapon;

import java.io.Serializable;

public class Obtainable implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected String description;
	protected String id;
	protected Integer cost;
	
	public Obtainable(String name, String description, String id, Integer cost)
	{
		this.name = name;
		this.description = description;
		this.id = id;
		this.cost = cost;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public Integer getCost()
	{
		return cost;
	}
	
	public void setCost(Integer cost)
	{
		this.cost = cost;
	}
}
