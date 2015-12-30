package com.ulticraft.weapon;

import com.ulticraft.uapi.UList;

public interface Upgradable
{
	void upgrade(Upgrade upgrade);
	UList<Upgrade> getCompatibleUpgrades();
}
