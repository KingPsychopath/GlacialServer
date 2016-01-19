package com.glacialrush.game.event;

import org.bukkit.entity.Player;
import com.glacialrush.component.GameController;

public class PlayerEvent extends RegionEvent
{
	private Player player;
	
	public PlayerEvent(GameController gameController, Player player)
	{
		super(gameController, gameController.getMap().getRegion(player));
		
		this.player = player;
	}

	public Player getPlayer()
	{
		return player;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}
}
