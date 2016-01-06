package com.glacialrush.game.event;

import org.bukkit.entity.Player;
import com.glacialrush.composite.Capture;

public class EnterCapturePointEvent extends CapturePointEvent
{
	private Player player;
	
	public EnterCapturePointEvent(Capture capturePoint, Player player)
	{
		super(capturePoint);
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
