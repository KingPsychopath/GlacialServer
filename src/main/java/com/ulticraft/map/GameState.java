package com.ulticraft.map;

import java.io.Serializable;
import com.ulticraft.uapi.UList;

public class GameState implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private UList<Map> maps;
	
	public GameState()
	{
		maps = new UList<Map>();
	}
	
	public Map searchLoose(String name)
	{
		for(Map i : maps)
		{
			if(i.getName().equalsIgnoreCase(name.replace('-', ' ').replace('_', ' ')))
			{
				return i;
			}
		}
		
		for(Map i : maps)
		{
			if(i.getName().toLowerCase().contains((name.replace('-', ' ').replace('_', ' ').toLowerCase())))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public Map search(String name)
	{
		for(Map i : maps)
		{
			if(i.getName().equalsIgnoreCase(name.replace('-', ' ').replace('_', ' ')))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public void addMap(Map map)
	{
		maps.add(map);
	}
	
	public void delMap(Map map)
	{
		maps.remove(map);
	}
	
	public UList<Map> getMaps()
	{
		return maps;
	}
}
