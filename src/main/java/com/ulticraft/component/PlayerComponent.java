package com.ulticraft.component;

import org.bukkit.entity.Player;
import com.ulticraft.GlacialServer;
import com.ulticraft.data.PlayerData;
import com.ulticraft.uapi.Component;
import com.ulticraft.uapi.UMap;

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
