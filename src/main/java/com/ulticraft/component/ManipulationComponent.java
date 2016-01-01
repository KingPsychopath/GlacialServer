package com.ulticraft.component;

import com.ulticraft.GlacialServer;
import com.ulticraft.composite.Manipulation;
import com.ulticraft.uapi.Component;
import com.ulticraft.uapi.UList;

public class ManipulationComponent extends Component
{
	private UList<Manipulation> manipulations;
	
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
				
				long ms = System.currentTimeMillis();
				
				while(System.currentTimeMillis() - ms > 30)
				{
					if(!manipulations.isEmpty())
					{
						manipulations.get(0).exec();
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
