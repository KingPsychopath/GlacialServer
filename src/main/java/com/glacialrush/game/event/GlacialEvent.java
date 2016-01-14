package com.glacialrush.game.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import com.glacialrush.component.GameController;

public class GlacialEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private GameController gameController;
	
	public GlacialEvent(GameController gameController)
	{
		this.gameController = gameController;
	}
	
	public GameController getGameController()
	{
		return gameController;
	}
	
	public void setGameController(GameController gameController)
	{
		this.gameController = gameController;
	}
	
	public boolean isCancelled()
	{
		return cancelled;
	}
	
	public void setCancelled(boolean cancel)
	{
		this.cancelled = cancel;
	}
	
	public HandlerList getHandlers()
	{
		return handlers;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}
}
