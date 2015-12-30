package com.ulticraft.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.ulticraft.GlacialRush;
import com.ulticraft.composite.Map;
import com.ulticraft.uapi.Gui;
import com.ulticraft.uapi.Gui.Pane;
import com.ulticraft.uapi.Gui.Pane.Element;

public class UIAdminMaps
{
	private Gui gui;
	
	public UIAdminMaps(final GlacialRush pl, final Player player)
	{
		gui = new Gui(player, pl);
		
		Pane pane = gui.new Pane("Maps");
		
		int m = 0;
		
		for(final Map i : pl.gg().getMaps())
		{
			Element e = pane.new Element(i.getName(), Material.MAP, m).setQuickRunnable(new Runnable()
			{
				@Override
				public void run()
				{
					gui.close();
					new UIAdminMapView(pl, player, i);
				}
			}).addBullet(i.getRegions().size() + "Regions");
			
			if(i.isBuilding())
			{
				e.addFailedRequirement("Building...");
			}
			
			else if(i.isBuilt())
			{
				e.addRequirement("Built");
			}
			
			else
			{
				e.addFailedRequirement("Needs Built");
			}
			
			m++;
		}
		
		pane.setDefault();
		gui.show();
	}
}
