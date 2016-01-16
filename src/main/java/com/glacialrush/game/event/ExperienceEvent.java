package com.glacialrush.game.event;

import org.bukkit.entity.Player;
import com.glacialrush.component.GameController;

public class ExperienceEvent extends PlayerEvent
{
	private long amount;
	private ExperienceType type;
	
	public ExperienceEvent(GameController gameController, Player player, long amount)
	{
		super(gameController, player);
		
		this.amount = amount;
		this.type = ExperienceType.UNKNOWN;
	}
	
	public ExperienceEvent(GameController gameController, Player player, long amount, ExperienceType type)
	{
		super(gameController, player);
		
		this.amount = amount;
		this.type = type;
	}

	public long getAmount()
	{
		return amount;
	}

	public void setAmount(long amount)
	{
		this.amount = amount;
	}

	public ExperienceType getType()
	{
		return type;
	}

	public void setType(ExperienceType type)
	{
		this.type = type;
	}
}
