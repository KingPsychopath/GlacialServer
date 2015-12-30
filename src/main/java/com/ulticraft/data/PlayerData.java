package com.ulticraft.data;

import java.io.Serializable;
import org.bukkit.entity.Player;

public class PlayerData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String uuid;
	private Integer totalXP;
	private Integer currentXP;
	private Integer skill;
	private Integer rank;
	private Integer modXP;
	
	public PlayerData(Player p)
	{
		this.name = p.getName();
		this.uuid = p.getUniqueId().toString();
		this.totalXP = 0;
		this.currentXP = 0;
		this.skill = 0;
		this.rank = 0;
		this.modXP = 0;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public Integer getTotalXP()
	{
		return totalXP;
	}

	public void setTotalXP(Integer totalXP)
	{
		this.totalXP = totalXP;
	}

	public Integer getCurrentXP()
	{
		return currentXP;
	}

	public void setCurrentXP(Integer currentXP)
	{
		this.currentXP = currentXP;
	}

	public Integer getSkill()
	{
		return skill;
	}

	public void setSkill(Integer skill)
	{
		this.skill = skill;
	}

	public Integer getRank()
	{
		return rank;
	}

	public void setRank(Integer rank)
	{
		this.rank = rank;
	}

	public Integer getModXP()
	{
		return modXP;
	}

	public void setModXP(Integer modXP)
	{
		this.modXP = modXP;
	}
}
