package com.ulticraft.composite;

import org.bukkit.entity.Player;
import com.ulticraft.uapi.Table;

public class PlayerData implements Table
{
	private Identity identity;
	private Economy economy;
	
	public PlayerData()
	{
		
	}
	
	public PlayerData(Player player)
	{
		identity = new Identity();
		identity.setName(player.getName());
		identity.setUUID(player.getUniqueId().toString());
		
		economy = new Economy();
		economy.setXp(0);
		economy.setCredits(0);
		economy.setPercent(0);
	}

	public Identity getIdentity()
	{
		return identity;
	}

	public void setIdentity(Identity identity)
	{
		this.identity = identity;
	}

	public Economy getEconomy()
	{
		return economy;
	}

	public void setEconomy(Economy economy)
	{
		this.economy = economy;
	}
}
