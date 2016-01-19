package com.glacialrush.market.item;

import org.bukkit.Material;
import com.glacialrush.component.MarketController;
import com.glacialrush.market.Knife;

public class ItemIceBlade extends Knife
{
	public ItemIceBlade(MarketController controller)
	{
		super("Ice Blade", controller);
		setMaterial(Material.BLAZE_ROD);
		setCost(100);
	}
}
