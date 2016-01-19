package com.glacialrush.market;

public enum ItemType
{
	WEAPON(Weapon.class),
	UTILITY(Utility.class),
	ABILITY(Ability.class);
	
	private Class<? extends Product> type;
	
	private ItemType(Class<? extends Product> type)
	{
		this.type = type;
	}
	
	public Class<? extends Product> getType()
	{
		return type;
	}
}
