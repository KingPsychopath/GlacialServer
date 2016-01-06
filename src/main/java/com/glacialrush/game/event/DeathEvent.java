package com.glacialrush.game.event;

import org.bukkit.entity.Player;
import com.glacialrush.composite.Region;

public class DeathEvent extends RegionEvent
{
	private Player killer;
	private Player dead;
	
	public DeathEvent(Region region, Player killer, Player dead)
	{
		super(region);
		this.killer = killer;
		this.dead = dead;
	}

	public Player getKiller()
	{
		return killer;
	}

	public void setKiller(Player killer)
	{
		this.killer = killer;
	}

	public Player getDead()
	{
		return dead;
	}

	public void setDead(Player dead)
	{
		this.dead = dead;
	}
}
