package com.ulticraft.item;

import com.ulticraft.xapi.UList;

public interface Upgradable
{
	void upgrade(Upgrade upgrade);
	UList<Upgrade> getCompatibleUpgrades();
}
