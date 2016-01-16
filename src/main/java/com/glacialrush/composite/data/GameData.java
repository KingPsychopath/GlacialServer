package com.glacialrush.composite.data;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class GameData
{
	private boolean enabled;
	
	public GameData(FileConfiguration fc)
	{
		enabled = fc.getBoolean("game.enabled");
	}
	
	public GameData()
	{
		enabled = false;
	}
	
	public FileConfiguration toFileConfiguration()
	{
		FileConfiguration fc = new YamlConfiguration();
		
		fc.set("game.enabled", enabled);
		
		return fc;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
}
