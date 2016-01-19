package com.glacialrush.composite.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import com.glacialrush.api.object.GList;

public class PlayerData
{
	private String name;
	private String uuid;
	private Long experience;
	private Long nextSkill;
	private Long skill;
	private Boolean acceptedResourcePack;
	private Double experienceBonus;
	private GList<String> unlocks;
	private GList<String> equipped;
	
	public PlayerData(Player p)
	{
		name = p.getName();
		uuid = p.getUniqueId().toString();
		experience = (long) 0;
		skill = (long) 0;
		nextSkill = (long) 0;
		experienceBonus = 0.0;
		acceptedResourcePack = false;
		unlocks = new GList<String>();
		equipped = new GList<String>();
	}
	
	public PlayerData(FileConfiguration fc)
	{
		name = fc.getString("identity.name");
		uuid = fc.getString("identity.uuid");
		acceptedResourcePack = fc.getBoolean("meta.accepted-resource-pack");
		experience = fc.getLong("economy.experience");
		experienceBonus = fc.getDouble("economy.experience-bonus");
		skill = fc.getLong("economy.skill");
		nextSkill = fc.getLong("economy.next-skill");
		unlocks = new GList<String>(fc.getStringList("economy.unlocks"));
		equipped = new GList<String>(fc.getStringList("economy.equipped"));
	}
	
	public FileConfiguration toFileConfiguration()
	{
		FileConfiguration fc = new YamlConfiguration();
		
		fc.set("identity.name", name);
		fc.set("identity.uuid", uuid);
		fc.set("meta.accepted-resource-pack", acceptedResourcePack);
		fc.set("economy.experience", experience);
		fc.set("economy.experience-bonus", experienceBonus);
		fc.set("economy.skill", skill);
		fc.set("economy.next-skill", nextSkill);
		fc.set("economy.unlocks", unlocks);
		fc.set("economy.equipped", equipped);
		
		return fc;
	}
	
	public Boolean getAcceptedResourcePack()
	{
		return acceptedResourcePack;
	}

	public void setAcceptedResourcePack(Boolean acceptedResourcePack)
	{
		this.acceptedResourcePack = acceptedResourcePack;
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
	
	public Long getExperience()
	{
		return experience;
	}
	
	public void setExperience(Long experience)
	{
		this.experience = experience;
	}
	
	public Double getExperienceBonus()
	{
		return experienceBonus;
	}
	
	public void setExperienceBonus(Double experienceBonus)
	{
		this.experienceBonus = experienceBonus;
	}
	
	public Long getSkill()
	{
		return skill;
	}
	
	public void setSkill(Long skill)
	{
		this.skill = skill;
	}
	
	public Long getNextSkill()
	{
		return nextSkill;
	}
	
	public void setNextSkill(Long nextSkill)
	{
		this.nextSkill = nextSkill;
	}
	
	public GList<String> getUnlocks()
	{
		return unlocks;
	}
	
	public void setUnlocks(GList<String> unlocks)
	{
		this.unlocks = unlocks;
	}

	public GList<String> getEquipped()
	{
		return equipped;
	}

	public void setEquipped(GList<String> equipped)
	{
		this.equipped = equipped;
	}
}
