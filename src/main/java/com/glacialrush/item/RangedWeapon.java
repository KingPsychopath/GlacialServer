package com.glacialrush.item;

public class RangedWeapon extends Weapon
{
	private static final long serialVersionUID = 1L;
	
	protected Double projectileDrop;
	protected Double roundsPerMinute;
	protected Double reloadSpeed;
	protected Integer clipSize;
	protected Integer ammunition;
	protected Boolean automatic;
	
	public RangedWeapon(String name, String description, String id, Integer cost)
	{
		super(name, description, id, cost);
		
		projectileDrop = 0.0;
		roundsPerMinute = 0.0;
		reloadSpeed = 0.0;
		clipSize = 0;
		ammunition = 0;
		automatic = false;
	}

	public Double getProjectileDrop()
	{
		return projectileDrop;
	}

	public void setProjectileDrop(Double projectileDrop)
	{
		this.projectileDrop = projectileDrop;
	}

	public Double getRoundsPerMinute()
	{
		return roundsPerMinute;
	}

	public void setRoundsPerMinute(Double roundsPerMinute)
	{
		this.roundsPerMinute = roundsPerMinute;
	}

	public Double getReloadSpeed()
	{
		return reloadSpeed;
	}

	public void setReloadSpeed(Double reloadSpeed)
	{
		this.reloadSpeed = reloadSpeed;
	}

	public Integer getClipSize()
	{
		return clipSize;
	}

	public void setClipSize(Integer clipSize)
	{
		this.clipSize = clipSize;
	}

	public Integer getAmmunition()
	{
		return ammunition;
	}

	public void setAmmunition(Integer ammunition)
	{
		this.ammunition = ammunition;
	}

	public Boolean isAutomatic()
	{
		return automatic;
	}

	public void setAutomatic(Boolean automatic)
	{
		this.automatic = automatic;
	}
}
