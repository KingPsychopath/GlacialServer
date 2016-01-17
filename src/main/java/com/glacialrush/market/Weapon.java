package com.glacialrush.market;

import com.glacialrush.component.MarketController;

public class Weapon extends Item
{
	private WeaponType weaponType;
	
	public Weapon(String name, MarketController controller)
	{
		super(name, controller);
		setItemType(ItemType.WEAPON);
	}
	
	public WeaponType getWeaponType()
	{
		return weaponType;
	}
	
	public void setWeaponType(WeaponType weaponType)
	{
		this.weaponType = weaponType;
	}
}
