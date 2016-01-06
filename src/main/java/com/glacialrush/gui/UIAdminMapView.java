package com.glacialrush.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.glacialrush.GlacialServer;
import com.glacialrush.composite.Map;
import com.glacialrush.xapi.Gui;
import com.glacialrush.xapi.Gui.Pane;
import com.glacialrush.xapi.Gui.Pane.Element;

public class UIAdminMapView
{
	private Gui gui;
	
	public UIAdminMapView(GlacialServer pl, Player player, final Map map)
	{
		gui = new Gui(player, pl);
		Pane pane = gui.new Pane("Map: " + map.getName());
		
		pane.new Element("Build Map", Material.ANVIL, 0, -1).setQuickRunnable(new Runnable()
		{
			@Override
			public void run()
			{
				map.build();
			}
		});
		
		Element status = pane.new Element("Status", Material.NAME_TAG, 0, 0);
		
		if(map.isBuilding())
		{
			status.addFailedRequirement("Building...");
		}
		
		else if(map.isBuilt())
		{
			status.addRequirement("Built");
		}
		
		else
		{
			status.addFailedRequirement("Needs Built");
		}
		
		pane.setDefault();
		gui.show();
	}
}