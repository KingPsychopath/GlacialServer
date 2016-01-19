package com.glacialrush.market;

import com.glacialrush.component.MarketController;

public class Upgrade extends Product
{
	private ItemType type;
	
	public Upgrade(String name, MarketController controller)
	{
		super(name, controller);
		setProductType(ProductType.UPGRADE);
	}
	
	public ItemType getType()
	{
		return type;
	}
	
	public void setType(ItemType type)
	{
		this.type = type;
	}
}
