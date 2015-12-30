package com.ulticraft.weapon;

public class Ability extends Obtainable
{
	private static final long serialVersionUID = 1L;
	
	protected Double energyConsomption;
	
	public Ability(String name, String description, String id, Integer cost)
	{
		super(name, description, id, cost);
	}
}
