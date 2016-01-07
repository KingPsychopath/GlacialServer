package com.glacialrush.item;

import com.glacialrush.api.object.GList;

public interface Upgradable
{
	void upgrade(Upgrade upgrade);
	GList<Upgrade> getCompatibleUpgrades();
}
