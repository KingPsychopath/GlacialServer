package com.glacialrush.game;

import com.glacialrush.GlacialServer;
import com.glacialrush.xapi.UList;
import com.glacialrush.xapi.UMap;

public class GameRegistry
{
	private GlacialServer pl;
	private UList<GameComponent> components;
	private UMap<Tickreval, Integer> timings;
	
	public GameRegistry(GlacialServer pl)
	{
		this.pl = pl;
		this.components = new UList<GameComponent>();
		this.timings = new UMap<Tickreval, Integer>();
	}
	
	public void start()
	{
		for(GameComponent i : components)
		{
			i.onStart(pl.getGame());
		}
	}
	
	public void stop()
	{
		for(GameComponent i : components)
		{
			i.onStop(pl.getGame());
		}
	}
	
	public void tick()
	{
		for(Tickreval i : timings.keySet())
		{
			timings.put(i, timings.get(i) + 1);
			
			if(timings.get(i) > i.getTicks())
			{
				timings.put(i, 1);
			}
		}
		
		for(GameComponent i : components)
		{
			if(i.getClass().getDeclaredAnnotationsByType(Tickrement.class).length > 0)
			{
				Tickrement t = i.getClass().getDeclaredAnnotation(Tickrement.class);
				
				if(t == null || timings.get(t.value()) == null || t.value() == null)
				{
					i.onTick(pl.getGame());
					continue;
				}
				
				if(timings.get(t.value()) == t.value().getTicks())
				{
					i.onTick(pl.getGame());
				}
			}
			
			else
			{
				i.onTick(pl.getGame());
			}
		}
	}
	
	public void add(GameComponent gc)
	{
		components.add(gc);
	}
	
	public UList<GameComponent> getComponents()
	{
		return components;
	}
	
	public void setComponents(UList<GameComponent> components)
	{
		this.components = components;
	}
}
