package com.glacialrush.market;

import com.glacialrush.component.MarketController;

public class Ability extends Item
{
	public Ability(String name, MarketController controller)
	{
		super(name, controller);
		setItemType(ItemType.ABILITY);
	}
}
