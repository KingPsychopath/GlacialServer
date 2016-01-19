package com.glacialrush.market;

import com.glacialrush.component.MarketController;

public class Item extends Product
{
	private ItemType itemType;
	
	public Item(String name, MarketController controller)
	{
		super(name, controller);
		setProductType(ProductType.ITEM);
	}
	
	public ItemType getItemType()
	{
		return itemType;
	}
	
	public void setItemType(ItemType itemType)
	{
		this.itemType = itemType;
	}
}
