package com.glacialrush.item;

import com.glacialrush.xapi.UList;

public interface Upgradable
{
	void upgrade(Upgrade upgrade);
	UList<Upgrade> getCompatibleUpgrades();
}
