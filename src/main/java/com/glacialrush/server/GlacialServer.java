package com.glacialrush.server;

import java.io.File;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.glacialrush.api.GlacialPlugin;
import com.glacialrush.api.component.MapDataController;
import com.glacialrush.api.component.PlayerDataComponent;
import com.glacialrush.api.component.UIController;
import com.glacialrush.api.game.Game;
import com.glacialrush.api.game.GameController;
import com.glacialrush.api.game.GameType;
import com.glacialrush.api.game.RegionedGame;
import com.glacialrush.api.game.data.ChunkletData;
import com.glacialrush.api.game.data.MapData;
import com.glacialrush.api.game.data.PlayerData;
import com.glacialrush.api.game.data.RegionData;
import com.glacialrush.api.gui.Element;
import com.glacialrush.api.gui.ElementClickListener;
import com.glacialrush.api.gui.Shortcut;
import com.glacialrush.api.gui.ShortcutLaunchListener;
import com.glacialrush.api.map.Chunklet;
import com.glacialrush.api.map.Map;
import com.glacialrush.api.map.RegionType;
import com.glacialrush.api.map.region.Edge;
import com.glacialrush.api.map.region.Scenery;
import com.glacialrush.api.map.region.Territory;
import com.glacialrush.api.map.region.Village;
import com.glacialrush.api.sfx.Audio;
import net.md_5.bungee.api.ChatColor;

public class GlacialServer extends GlacialPlugin
{
	private GameController gameController;
	private CommandController commandController;
	private MapDataController mapDataController;
	private PlayerDataComponent playerDataComponent;
	private boolean uppd;
	
	public void onEnable()
	{
		super.onEnable();
		
		if(update)
		{
			uppd = true;
			return;
		}
		
		uppd = false;
		
		commandController = new CommandController(this);
		mapDataController = new MapDataController(this, new File(getDataFolder(), "maps"), "map");
		playerDataComponent = new PlayerDataComponent(this, new File(getDataFolder(), "playerdata"), "gp");
		gameController = new GameController(this, playerDataComponent);
		
		super.startComponents();
		
		for(Player i : onlinePlayers())
		{
			playerDataComponent.load(i);
		}
		
		gameController.setBlocking(true);
		
		for(File i : mapDataController.getMdc().getCache().keySet())
		{
			MapData md = mapDataController.getMdc().getCache().get(i);
			
			Map map = new Map(md.getName(), gameController.getBuildGame(), getServer().getWorld(md.getWorld()));
			
			if(md.getLocked())
			{
				map.lock();
			}
			
			for(RegionData j : md.getRegions())
			{
				if(j.getType().equals(RegionType.TERRITORY))
				{
					Territory r = new Territory(map, j.getName(), gameController.getBuildGame());
					
					for(ChunkletData k : j.getChunklets())
					{
						Chunklet c = new Chunklet(k.getX(), k.getZ(), getServer().getWorld(md.getWorld()));
						
						r.addChunklet(c);
					}
					
					map.getRegions().add(r);
				}
				
				else if(j.getType().equals(RegionType.VILLAGE))
				{
					Village r = new Village(map, j.getName(), gameController.getBuildGame());
					
					for(ChunkletData k : j.getChunklets())
					{
						Chunklet c = new Chunklet(k.getX(), k.getZ(), getServer().getWorld(md.getWorld()));
						
						r.addChunklet(c);
					}
					
					map.getRegions().add(r);
				}
				
				else if(j.getType().equals(RegionType.EDGE))
				{
					Edge r = new Edge(map, j.getName(), gameController.getBuildGame());
					
					for(ChunkletData k : j.getChunklets())
					{
						Chunklet c = new Chunklet(k.getX(), k.getZ(), getServer().getWorld(md.getWorld()));
						
						r.addChunklet(c);
					}
					
					map.getRegions().add(r);
				}
				
				else if(j.getType().equals(RegionType.SCENERY))
				{
					Scenery r = new Scenery(map, j.getName(), gameController.getBuildGame());
					
					for(ChunkletData k : j.getChunklets())
					{
						Chunklet c = new Chunklet(k.getX(), k.getZ(), getServer().getWorld(md.getWorld()));
						
						r.addChunklet(c);
					}
					
					map.getRegions().add(r);
				}
			}
			
			gameController.getMaps().add(map);
			map.lock();
			map.fastBuild();
			s("Injected Map: " + map.getName());
		}
		
		gameController.setBlocking(false);
		
		getCommand(Info.CMD_GAME).setExecutor(commandController);
		getCommand(Info.CMD_REGION).setExecutor(commandController);
		getCommand(Info.CMD_MAP).setExecutor(commandController);
		getCommand(Info.CMD_BRUSH).setExecutor(commandController);
		getCommand(Info.CMD_TELEPORT).setExecutor(commandController);
		getCommand(Info.CMD_TELEPORT_HERE).setExecutor(commandController);
		getCommand(Info.CMD_GAMEMODE).setExecutor(commandController);
		getCommand(Info.CMD_GAMEMODEA).setExecutor(commandController);
		getCommand(Info.CMD_GAMEMODEC).setExecutor(commandController);
		getCommand(Info.CMD_GAMEMODES).setExecutor(commandController);
		getCommand(Info.CMD_MESSAGE).setExecutor(commandController);
		getCommand(Info.CMD_REPLY).setExecutor(commandController);
		getCommand(Info.CMD_LEAVE).setExecutor(commandController);
		getCommand(Info.CMD_SUN).setExecutor(commandController);
		getCommand(Info.CMD_RAIN).setExecutor(commandController);
		getCommand(Info.CMD_DAY).setExecutor(commandController);
		getCommand(Info.CMD_NIGHT).setExecutor(commandController);
		getCommand(Info.CMD_DEVELOPER).setExecutor(commandController);
		
		registerShortcuts();
	}
	
	public void onDisable()
	{
		if(uppd)
		{
			return;
		}
		
		for(Map i : gameController.getMaps())
		{
			boolean held = false;
			
			for(File j : mapDataController.getMdc().getCache().keySet())
			{
				if(mapDataController.getMdc().getCache().get(j).getName().equals(i.getName()))
				{
					held = true;
				}
			}
			
			if(!held)
			{
				mapDataController.track(i);
			}
		}
		
		super.onDisable();
	}
	
	public PlayerData gpd(Player p)
	{
		return playerDataComponent.get(p);
	}
	
	public void registerShortcuts()
	{
		Shortcut sGames = new Shortcut(ChatColor.AQUA + "Games", Material.INK_SACK, 0, 1).setData((byte) 4).onShortcutLaunch(new ShortcutLaunchListener()
		{
			public void run()
			{
				int slot = -1;
				
				for(Game i : gameController.getGames())
				{
					if(i.getType().equals(GameType.REGIONED))
					{
						slot++;
						final RegionedGame g = (RegionedGame) i;
						Element e = new Element(getPane(), ChatColor.AQUA + g.getMap().getName(), Material.BOW, slot);
						e.addLore(ChatColor.GREEN + "" + i.getState().getPlayers().size() + " Players");
						e.addLore(g.getFactionHandler().map());
						
						if(g.contains(getPlayer()))
						{
							e.addLore(ChatColor.AQUA + "Currently Playing");
						}
						
						e.setOnLeftClickListener(new ElementClickListener()
						{
							public void run()
							{
								if(g.contains(getPlayer()))
								{
									Audio.UI_FAIL.play(getPlayer());
								}
								
								else
								{
									close();
									g.join(getPlayer());
								}
							}
						});
					}
				}
			}
		}).setIX(0).setIY(2);
		
		uiController.addShortcut(sGames);
	}
	
	public UIController getUiController()
	{
		return uiController;
	}
	
	public GameController getGameController()
	{
		return gameController;
	}
	
	public CommandController getCommandController()
	{
		return commandController;
	}
	
	public MapDataController getMapDataController()
	{
		return mapDataController;
	}
}
