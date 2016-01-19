package com.glacialrush.market;

import com.glacialrush.component.MarketController;

public class PrimaryWeapon extends Weapon
{
	public PrimaryWeapon(String name, MarketController controller)
	{
		super(name, controller);
		setWeaponType(WeaponType.PRIMARY);
	}
}
