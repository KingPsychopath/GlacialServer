package com.ulticraft.weapon;

public class Armor extends Obtainable implements Timed, Defensive
{
	private static final long serialVersionUID = 1L;
	
	protected Double damageResistance;
	protected Double heaft;
	
	public Armor(String name, String description, String id, Integer cost)
	{
		super(name, description, id, cost);
		
		damageResistance = 0.0;
		heaft = 0.0;
	}

	public Double getDamageResistance()
	{
		return damageResistance;
	}

	public void setDamageResistance(Double damageResistance)
	{
		this.damageResistance = damageResistance;
	}

	public Double getHeaft()
	{
		return heaft;
	}

	public void setHeaft(Double heaft)
	{
		this.heaft = heaft;
	}

	@Override
	public double onDamage(double damage)
	{
		return damage - (damage * damageResistance);
	}

	@Override
	public void onInternalTick()
	{
		
	}
}
