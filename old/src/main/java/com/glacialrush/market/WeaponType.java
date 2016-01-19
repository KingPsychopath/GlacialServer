package com.glacialrush.market;

public enum WeaponType
{
	PRIMARY(PrimaryWeapon.class),
	SECONDARY(Bow.class),
	MELEE(Knife.class);
	
	private Class<? extends Product> type;
	
	private WeaponType(Class<? extends Product> type)
	{
		this.type = type;
	}
	
	public Class<? extends Product> getType()
	{
		return type;
	}
}
