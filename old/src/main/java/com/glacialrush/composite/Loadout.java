package com.glacialrush.composite;

import com.glacialrush.market.Ability;
import com.glacialrush.market.MeleeWeapon;
import com.glacialrush.market.PrimaryWeapon;
import com.glacialrush.market.SecondaryWeapon;
import com.glacialrush.market.Utility;

public class Loadout
{
	private PrimaryWeapon primaryWeapon;
	private SecondaryWeapon secondaryWeapon;
	private MeleeWeapon meleeWeapon;
	private Utility utility;
	private Ability ability;
	
	public Loadout()
	{
		
	}

	public PrimaryWeapon getPrimaryWeapon()
	{
		return primaryWeapon;
	}

	public void setPrimaryWeapon(PrimaryWeapon primaryWeapon)
	{
		this.primaryWeapon = primaryWeapon;
	}

	public SecondaryWeapon getSecondaryWeapon()
	{
		return secondaryWeapon;
	}

	public void setSecondaryWeapon(SecondaryWeapon secondaryWeapon)
	{
		this.secondaryWeapon = secondaryWeapon;
	}

	public MeleeWeapon getMeleeWeapon()
	{
		return meleeWeapon;
	}

	public void setMeleeWeapon(MeleeWeapon meleeWeapon)
	{
		this.meleeWeapon = meleeWeapon;
	}

	public Utility getUtility()
	{
		return utility;
	}

	public void setUtility(Utility utility)
	{
		this.utility = utility;
	}

	public Ability getAbility()
	{
		return ability;
	}

	public void setAbility(Ability ability)
	{
		this.ability = ability;
	}
}
