package com.glacialrush.market;

import com.glacialrush.component.MarketController;

public class MeleeWeapon extends Weapon
{
	public MeleeWeapon(String name, MarketController controller)
	{
		super(name, controller);
		setWeaponType(WeaponType.MELEE);
	}
}
