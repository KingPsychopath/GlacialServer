package com.glacialrush.component;

import org.bukkit.entity.Player;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.component.GlacialComponent;
import com.glacialrush.composite.PlayerData;
import com.glacialrush.xapi.UMap;

public class PlayerComponent extends GlacialComponent
{
	private UMap<Player, PlayerData> players;
	
	public PlayerComponent(GlacialServer pl)
	{
		super(pl);
	}
}
