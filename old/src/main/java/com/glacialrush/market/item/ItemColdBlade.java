package com.glacialrush.market.item;

import org.bukkit.Material;
import com.glacialrush.component.MarketController;
import com.glacialrush.market.Knife;

public class ItemColdBlade extends Knife
{
	public ItemColdBlade(MarketController controller)
	{
		super("Cold Blade", controller);
		
		setMaterial(Material.STICK);
		setCost(0);
		setStandard(true);
	}
}
