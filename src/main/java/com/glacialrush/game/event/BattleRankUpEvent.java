package com.glacialrush.game.event;

import org.bukkit.entity.Player;
import com.glacialrush.component.GameController;

public class BattleRankUpEvent extends PlayerEvent
{
	private int level;
	
	public BattleRankUpEvent(GameController gameController, Player player, int level)
	{
		super(gameController, player);
		
		this.level = level;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}
}
