package com.glacialrush.item;

public class Weapon extends Obtainable
{
	private static final long serialVersionUID = 1L;
	
	protected Double damage;
	
	public Weapon(String name, String description, String id, Integer cost)
	{
		super(name, description, id, cost);

		damage = 0.0;
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
