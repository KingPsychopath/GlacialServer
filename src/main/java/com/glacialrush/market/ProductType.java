package com.glacialrush.market;

public enum ProductType
{
	ITEM(Item.class),
	UPGRADE(Upgrade.class);
	
	private Class<? extends Product> type;
	
	private ProductType(Class<? extends Product> type)
	{
		this.type = type;
	}
	
	public Class<? extends Product> getType()
	{
		return type;
	}
}
