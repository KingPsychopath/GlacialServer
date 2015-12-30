package com.ulticraft.weapon;

public class RegenerationArmor extends Armor
{
	private static final long serialVersionUID = 1L;

	protected Double regenerationRate;
	protected Double regenerationDelay;
	
	public RegenerationArmor(String name, String description, String id, Integer cost)
	{
		super(name, description, id, cost);
		
		regenerationRate = 0.0;
		regenerationDelay = 0.0;
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
}
