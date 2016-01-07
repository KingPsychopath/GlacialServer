package com.glacialrush.game.event;

import org.bukkit.entity.Player;
import com.glacialrush.composite.Faction;

public class PlayerFactionChangedEvent extends GlacialEvent
{
	private Player player;
	private Faction faction;
	
	public PlayerFactionChangedEvent(Player player, Faction faction)
	{
		this.player = player;
		this.faction = faction;
	}

	public Player getPlayer()
	{
		return player;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public Faction getFaction()
	{
		return faction;
	}

	public void setFaction(Faction faction)
	{
		this.faction = faction;
	}
}
