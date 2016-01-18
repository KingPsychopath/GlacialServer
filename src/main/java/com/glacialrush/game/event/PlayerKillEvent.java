package com.glacialrush.game.event;

import org.bukkit.entity.Player;
import com.glacialrush.component.GameController;

public class PlayerKillEvent extends PlayerEvent
{
	private Player killer;
	
	public PlayerKillEvent(GameController gameController, Player killed, Player killer)
	{
		super(gameController, killed);
		
		this.killer = killer;
	}

	public Player getKiller()
	{
		return killer;
	}

	public void setKiller(Player killer)
	{
		this.killer = killer;
	}
}
