package com.glacialrush.market.item;

import org.bukkit.Material;
import com.glacialrush.component.MarketController;
import com.glacialrush.market.Bow;

public class GlacialBow extends Bow
{
	public GlacialBow(String name, MarketController controller)
	{
		super(name, controller);
		setMaterial(Material.BOW);
		setCost(0);
		setStandard(true);
	}
}
