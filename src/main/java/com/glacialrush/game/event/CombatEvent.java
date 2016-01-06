package com.glacialrush.game.event;

import org.bukkit.entity.Player;
import com.glacialrush.composite.Region;

public class CombatEvent extends RegionEvent
{
	private Player attacker;
	private Player attacked;
	private Double damage;
	
	public CombatEvent(Region region, Player attacker, Player attacked, Double damage)
	{
		super(region);
		this.attacker = attacker;
		this.attacked = attacked;
	}

	public Player getAttacker()
	{
		return attacker;
	}

	public void setAttacker(Player attacker)
	{
		this.attacker = attacker;
	}

	public Player getAttacked()
	{
		return attacked;
	}

	public void setAttacked(Player attacked)
	{
		this.attacked = attacked;
	}

	public Double getDamage()
	{
		return damage;
	}

	public void setDamage(Double damage)
	{
		this.damage = damage;
	}
}
