package com.ulticraft.component;

import org.bukkit.entity.Player;
import com.ulticraft.GlacialServer;
import com.ulticraft.composite.PlayerData;
import com.ulticraft.xapi.Component;
import com.ulticraft.xapi.UMap;

public class PlayerComponent extends Component
{
	private UMap<Player, PlayerData> players;
	
	public PlayerComponent(GlacialServer pl)
	{
		super(pl);
	}
	
	public void enable()
	{
		
	}
	
	public void disable()
	{
		
	}
}
