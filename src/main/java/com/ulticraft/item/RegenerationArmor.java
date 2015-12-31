package com.ulticraft.item;

public class RegenerationArmor extends Armor
{
	private static final long serialVersionUID = 1L;

	protected Double regenerationRate;
	protected Double regenerationDelay;
	protected Double maxDamageResistance;
	protected Double degenerationPerDamage;
	protected Double currentDamageResistance;
	protected Double currentDelay;
	
	public RegenerationArmor(String name, String description, String id, Integer cost)
	{
		super(name, description, id, cost);
		
		regenerationRate = 0.0;
		regenerationDelay = 0.0;
		maxDamageResistance = 0.0;
		degenerationPerDamage = 0.0;
		currentDamageResistance = 0.0;
		currentDelay = 0.0;
	}

	public Double getRegenerationRate()
	{
		return regenerationRate;
	}

	public void setRegenerationRate(Double regenerationRate)
	{
		this.regenerationRate = regenerationRate;
	}

	public Double getRegenerationDelay()
	{
		return regenerationDelay;
	}

	public void setRegenerationDelay(Double regenerationDelay)
	{
		this.regenerationDelay = regenerationDelay;
	}

	public Double getMaxDamageResistance()
	{
		return maxDamageResistance;
	}

	public void setMaxDamageResistance(Double maxDamageResistance)
	{
		this.maxDamageResistance = maxDamageResistance;
	}

	public Double getCurrentDamageResistance()
	{
		return currentDamageResistance;
	}

	public void setCurrentDamageResistance(Double currentDamageResistance)
	{
		this.currentDamageResistance = currentDamageResistance;
	}
	
	public Double getDegenerationPerDamage()
	{
		return degenerationPerDamage;
	}

	public void setDegenerationPerDamage(Double degenerationPerDamage)
	{
		this.degenerationPerDamage = degenerationPerDamage;
	}

	public Double getCurrentDelay()
	{
		return currentDelay;
	}

	public void setCurrentDelay(Double currentDelay)
	{
		this.currentDelay = currentDelay;
	}

	@Override
	public double onDamage(double damage)
	{
		damage = damage - (damage * (damageResistance + currentDamageResistance));
		currentDamageResistance = currentDamageResistance - (damage * degenerationPerDamage);
		
		return 0;
	}
	
	@Override
	public void onInternalTick()
	{
		currentDelay = currentDelay - 0.05 < 0 ? 0 : currentDelay - 0.05;
		currentDamageResistance += currentDelay == 0 ? regenerationRate : 0;
		currentDamageResistance = currentDamageResistance > maxDamageResistance ? maxDamageResistance : currentDamageResistance;
	}
}
