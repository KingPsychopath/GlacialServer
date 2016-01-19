package com.glacialrush.market;

import com.glacialrush.component.MarketController;

public class Utility extends Item
{
	public Utility(String name, MarketController controller)
	{
		super(name, controller);
		setItemType(ItemType.UTILITY);
	}
}
