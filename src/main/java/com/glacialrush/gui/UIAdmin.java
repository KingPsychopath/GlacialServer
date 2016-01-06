package com.glacialrush.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.glacialrush.GlacialServer;
import com.glacialrush.xapi.Gui;
import com.glacialrush.xapi.Gui.Pane;

public class UIAdmin
{
	private Gui gui;
	
	public UIAdmin(final GlacialServer pl, final Player player)
	{
		gui = new Gui(player, pl);
		
		Pane pane = gui.new Pane("Administration");
		pane.new Element("Maps", Material.MAP, 0, 0).setQuickRunnable(new Runnable()
		{
			@Override
			public void run()
			{
				gui.close();
				new UIAdminMaps(pl, player);
			}
		});
		
		pane.setDefault();
		gui.show();
	}
}
