package com.glacialrush;

import com.glacialrush.api.GlacialPlugin;
import com.glacialrush.component.DataController;
import com.glacialrush.component.GameController;
import com.glacialrush.component.JobController;
import com.glacialrush.component.MarketController;
import com.glacialrush.component.NotificationController;
import com.glacialrush.component.PlayerController;
import com.glacialrush.component.SoundController;

public class GlacialServer extends GlacialPlugin
{
	private GameController gameController;
	private DataController dataController;
	private PlayerController playerController;
	private MarketController marketController;
	private JobController jobController;
	private NotificationController notificationController;
	private SoundController soundController;
	
	public void onEnable()
	{
		super.onEnable();
		
		gameController = new GameController(this);
		dataController = new DataController(this);
		playerController = new PlayerController(this);
		marketController = new MarketController(this);
		jobController = new JobController(this);
		notificationController = new NotificationController(this);
		soundController = new SoundController(this);
		
		super.startComponents();
	}
	
	public void onDisable()
	{
		super.onDisable();
	}

	public GameController getGameController()
	{
		return gameController;
	}

	public DataController getDataController()
	{
		return dataController;
	}

	public PlayerController getPlayerController()
	{
		return playerController;
	}

	public MarketController getMarketController()
	{
		return marketController;
	}

	public JobController getJobController()
	{
		return jobController;
	}

	public NotificationController getNotificationController()
	{
		return notificationController;
	}

	public SoundController getSoundController()
	{
		return soundController;
	}
}
