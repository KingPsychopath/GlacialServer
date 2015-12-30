package com.ulticraft.weapon;

public class MeleeWeapon extends Weapon
{
	private static final long serialVersionUID = 1L;
	
	protected Double heaft;
	
	public MeleeWeapon(String name, String description, String id, Integer cost)
	{
		super(name, description, id, cost);
		
		this.heaft = 1.0;
	}

	public Double getHeaft()
	{
		return heaft;
	}

	public void setHeaft(Double heaft)
	{
		this.heaft = heaft;
	}
}
