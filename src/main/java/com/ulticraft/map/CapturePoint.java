package com.ulticraft.map;

import java.io.Serializable;
import org.bukkit.block.Block;
import com.ulticraft.faction.Faction;
import com.ulticraft.uapi.ULocation;

public class CapturePoint implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected ULocation block;
	protected Faction defense;
	protected Faction offense;
	protected Integer progress;
	
	public CapturePoint(String name, Block block)
	{
		this.name = name;
		this.block = new ULocation(block.getLocation());
		
		defense = Faction.neutral();
		offense = null;
		progress = -100;
	}
	
	public void capture(int defensePoints, int offensePoints)
	{
		if(defensePoints > offensePoints)
		{
			progress -= 25;
		}
		
		if(defensePoints < offensePoints)
		{
			progress += 25;
		}
		
		if(progress < -100)
		{
			progress = -100;
		}
		
		if(progress > 100)
		{
			progress = 100;
		}
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public ULocation getBlock()
	{
		return block;
	}
	
	public void setBlock(ULocation block)
	{
		this.block = block;
	}
	
	public Faction getDefense()
	{
		return defense;
	}
	
	public void setDefense(Faction defense)
	{
		this.defense = defense;
	}
	
	public Faction getOffense()
	{
		return offense;
	}
	
	public void setOffense(Faction offense)
	{
		this.offense = offense;
	}
	
	public Integer getProgress()
	{
		return progress;
	}
	
	public void setProgress(Integer progress)
	{
		this.progress = progress;
	}
	
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
	public boolean offenseSecured()
	{
		if(progress == 100)
		{
			return true;
		}
		
		return false;
	}
	
	public Faction getFactionSecured()
	{
		if(offenseSecured())
		{
			return offense;
		}
		
		return defense;
	}
}
