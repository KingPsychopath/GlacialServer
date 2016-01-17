package com.glacialrush;

import org.bukkit.entity.Player;
import com.glacialrush.api.GlacialPlugin;
import com.glacialrush.component.CommandController;
import com.glacialrush.component.DataController;
import com.glacialrush.component.ExperienceController;
import com.glacialrush.component.GameController;
import com.glacialrush.component.JobController;
import com.glacialrush.component.MarketController;
import com.glacialrush.component.NotificationController;
import com.glacialrush.component.PlayerController;
import com.glacialrush.component.SoundController;
import com.glacialrush.composite.data.PlayerData;

public class GlacialServer extends GlacialPlugin
{
	private GameController gameController;
	private DataController dataController;
	private PlayerController playerController;
	private MarketController marketController;
	private JobController jobController;
	private NotificationController notificationController;
	private SoundController soundController;
	private ExperienceController experienceController;
	private CommandController commandController;
	
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
		experienceController = new ExperienceController(this);
		commandController = new CommandController(this);
		
		super.startComponents();
		
		getCommand(Info.CMD_GLACIALRUSH).setExecutor(commandController);
		getCommand(Info.CMD_DEBUGGER).setExecutor(commandController);
		getCommand(Info.CMD_SKILL).setExecutor(commandController);
	}
	
	public PlayerData gpd(Player p)
	{
		return getDataController().getPlayer(p);
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
	
	public ExperienceController getExperienceController()
	{
		return experienceController;
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
	
	public CommandController getCommandController()
	{
		return commandController;
	}
}
