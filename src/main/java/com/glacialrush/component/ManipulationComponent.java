package com.glacialrush.component;

import com.glacialrush.GlacialServer;
import com.glacialrush.api.component.GlacialComponent;
import com.glacialrush.api.dispatch.Title;
import com.glacialrush.composite.Manipulation;
import com.glacialrush.xapi.UList;
import net.md_5.bungee.api.ChatColor;

public class ManipulationComponent extends GlacialComponent
{
	private UList<Manipulation> manipulations;
	private final String[] progress = new String[] {"|", "/", "-", "\\"};
	private int prog = 0;
	
	public ManipulationComponent(final GlacialServer pl)
	{
		super(pl);
		manipulations = new UList<Manipulation>();
	}
	
	public boolean isIdle()
	{
		return manipulations.isEmpty();
	}
	
	public void postEnable()
	{
		pl.scheduleSyncRepeatingTask(0, 0, new Runnable()
		{
			@Override
			public void run()
			{
				if(manipulations.isEmpty())
				{
					return;
				}
				
				Title t = new Title();
				t.setTitle(ChatColor.AQUA + progress[prog]);				
				
				t.setFadeInTime(0);
				t.setFadeOutTime(0);
				prog++;
				
				if(prog > 3)
				{
					prog = 0;
				}
				
				t.send();
				
				long ms = System.currentTimeMillis();
				
				while(System.currentTimeMillis() - ms < 45)
				{
					if(!manipulations.isEmpty())
					{
						manipulations.get(0).exec();
						manipulations.remove(0);
					}
					
					else
					{
						return;
					}
				}
			}
		});
	}
	
	public void add(Manipulation m)
	{
		manipulations.add(m);
	}
}
