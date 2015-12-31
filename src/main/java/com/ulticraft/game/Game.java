package com.ulticraft.game;

import java.util.Collections;
import com.ulticraft.GlacialRush;
import com.ulticraft.composite.Hunk;
import com.ulticraft.composite.Map;
import com.ulticraft.uapi.UList;

public class Game
{
	private GameState state;
	private GameRegistry registry;
	private GlacialRush pl;
	private UList<Map> maps;
	
	public Game(GlacialRush pl)
	{
		this.pl = pl;
		this.state = new GameState(pl, null);
		this.registry = new GameRegistry(pl);
		this.maps = new UList<Map>();
	}
	
	public void load()
	{
		maps = pl.getDataComponent().loadAll();
		
		for(Map i : maps)
		{
			i.build();
		}
	}
	
	public void save()
	{
		pl.getDataComponent().saveAll(maps);
	}
	
	public void startGame()
	{
		UList<Map> mMaps = maps.copy();
		Collections.shuffle(mMaps);
		state = new GameState(pl, mMaps.get(0));
	}
	
	public Map findMap(String name)
	{
		for(Map i : maps)
		{
			if(i.getName().equalsIgnoreCase(name))
			{
				return i;
			}
		}
		
		for(Map i : maps)
		{
			if(i.getName().toLowerCase().contains(name.toLowerCase()))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public Map getMap(String name)
	{
		for(Map i : maps)
		{
			if(i.getName().equalsIgnoreCase(name))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public Map getMap(Hunk hunk)
	{
		for(Map i : maps)
		{
			if(i.getWorld().equals(hunk.getWorld()))
			{
				if(i.getRegion(hunk) != null)
				{
					return i;
				}
			}
		}
		
		return null;
	}

	public GameState getState()
	{
		return state;
	}

	public GameRegistry getRegistry()
	{
		return registry;
	}

	public UList<Map> getMaps()
	{
		return maps;
	}

	public void setMaps(UList<Map> maps)
	{
		this.maps = maps;
	}
}
