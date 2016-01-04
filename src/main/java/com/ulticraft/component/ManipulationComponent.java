package com.ulticraft.component;

import com.ulticraft.GlacialServer;
import com.ulticraft.composite.Manipulation;
import com.ulticraft.xapi.Component;
import com.ulticraft.xapi.Title;
import com.ulticraft.xapi.UList;
import net.md_5.bungee.api.ChatColor;

public class ManipulationComponent extends Component
{
	private UList<Manipulation> manipulations;
	private final String[] progress = new String[] {"|", "/", "-", "\\"};
	private int prog = 0;
	
	public ManipulationComponent(final GlacialServer pl)
	{
		super(pl);
		manipulations = new UList<Manipulation>();
	}
	
	public void enable()
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
	
	public void disable()
	{
		
	}
	
	public void add(Manipulation m)
	{
		manipulations.add(m);
	}
}
