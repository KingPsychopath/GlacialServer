package com.glacialrush.market;

import com.glacialrush.component.MarketController;

public class SecondaryWeapon extends Weapon
{
	public SecondaryWeapon(String name, MarketController controller)
	{
		super(name, controller);
		setWeaponType(WeaponType.SECONDARY);
	}
}
