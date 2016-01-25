package com.glacialrush.server;

import java.io.File;
import com.glacialrush.api.GlacialPlugin;
import com.glacialrush.api.component.MapDataController;
import com.glacialrush.api.game.GameController;
import com.glacialrush.api.game.data.ChunkletData;
import com.glacialrush.api.game.data.MapData;
import com.glacialrush.api.game.data.RegionData;
import com.glacialrush.api.map.Chunklet;
import com.glacialrush.api.map.Map;
import com.glacialrush.api.map.RegionType;
import com.glacialrush.api.map.region.Edge;
import com.glacialrush.api.map.region.Scenery;
import com.glacialrush.api.map.region.Territory;
import com.glacialrush.api.map.region.Village;

public class GlacialServer extends GlacialPlugin
{
	private GameController gameController;
	private CommandController commandController;
	private MapDataController mapDataController;
	
	public void onEnable()
	{
		super.onEnable();
		
		gameController = new GameController(this);
		commandController = new CommandController(this);
		mapDataController = new MapDataController(this, new File(getDataFolder(), "maps"), "map");
		
		super.startComponents();
		
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
			s("Injected Map: " + map.getName());
		}
		
		getCommand(Info.CMD_GAME).setExecutor(commandController);
		getCommand(Info.CMD_REGION).setExecutor(commandController);
		getCommand(Info.CMD_MAP).setExecutor(commandController);
		getCommand(Info.CMD_BRUSH).setExecutor(commandController);
		getCommand(Info.CMD_TELEPORT).setExecutor(commandController);
		getCommand(Info.CMD_GAMEMODE).setExecutor(commandController);
		getCommand(Info.CMD_GAMEMODEA).setExecutor(commandController);
		getCommand(Info.CMD_GAMEMODEC).setExecutor(commandController);
		getCommand(Info.CMD_GAMEMODES).setExecutor(commandController);
		getCommand(Info.CMD_MESSAGE).setExecutor(commandController);
		getCommand(Info.CMD_REPLY).setExecutor(commandController);
	}
	
	public void onDisable()
	{
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
