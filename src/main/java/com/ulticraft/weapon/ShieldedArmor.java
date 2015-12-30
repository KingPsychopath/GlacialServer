package com.ulticraft.weapon;

public class ShieldedArmor extends Armor
{
	private static final long serialVersionUID = 1L;

	protected Double shieldCapacity;
	protected Double shieldRegenRate;
	protected Double shieldRechargeDelay;
	protected Double shieldDamageResistance;
	
	public ShieldedArmor(String name, String description, String id, Integer cost)
	{
		super(name, description, id, cost);
		
		shieldCapacity = 0.0;
		shieldRegenRate = 0.0;
		shieldRechargeDelay = 0.0;
		shieldDamageResistance = 0.0;
	}

	public Double getShieldCapacity()
	{
		return shieldCapacity;
	}

	public void setShieldCapacity(Double shieldCapacity)
	{
		this.shieldCapacity = shieldCapacity;
	}

	public Double getShieldRegenRate()
	{
		return shieldRegenRate;
	}

	public void setShieldRegenRate(Double shieldRegenRate)
	{
		this.shieldRegenRate = shieldRegenRate;
	}

	public Double getShieldRechargeDelay()
	{
		return shieldRechargeDelay;
	}

	public void setShieldRechargeDelay(Double shieldRechargeDelay)
	{
		this.shieldRechargeDelay = shieldRechargeDelay;
	}

	public Double getShieldDamageResistance()
	{
		return shieldDamageResistance;
	}

	public void setShieldDamageResistance(Double shieldDamageResistance)
	{
		this.shieldDamageResistance = shieldDamageResistance;
	}
}
