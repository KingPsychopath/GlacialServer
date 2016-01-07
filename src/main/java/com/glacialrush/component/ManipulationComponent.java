package com.glacialrush.component;

import com.glacialrush.GlacialServer;
import com.glacialrush.api.component.GlacialComponent;
import com.glacialrush.api.dispatch.Title;
import com.glacialrush.api.object.GList;
import com.glacialrush.composite.Manipulation;
import com.glacialrush.game.GameState.Status;
import net.md_5.bungee.api.ChatColor;

public class ManipulationComponent extends GlacialComponent
{
	private GList<Manipulation> manipulations;
	private final String[] progress = new String[] {"v", "<", "^", ">"};
	private int prog = 0;
	private GlacialServer gs;
	
	public ManipulationComponent(final GlacialServer pl)
	{
		super(pl);
		gs = pl;
		manipulations = new GList<Manipulation>();
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
				
				if(gs.getGame().getState().getStatus().equals(Status.OFFLINE))
				{
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
				}
				
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
