package com.glacialrush.game.event;

import org.bukkit.entity.Player;
import com.glacialrush.component.GameController;
import com.glacialrush.composite.Capture;
import com.glacialrush.composite.Faction;

public class PlayerControlEvent extends CaptureEvent
{
	private Player player;
	private Faction faction;
	
	public PlayerControlEvent(GameController gameController, Capture capture, Player player)
	{
		super(gameController, capture);
		
		this.player = player;
		this.faction = gameController.getPlayerHandler().getFaction(player);
		
		if(faction == null)
		{
			getPlayer().sendMessage("err fac null");
		}
	}

	public Player getPlayer()
	{
		return player;
	}

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public Faction getFaction()
	{
		return faction;
	}

	public void setFaction(Faction faction)
	{
		this.faction = faction;
	}
}
