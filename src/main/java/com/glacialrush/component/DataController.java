package com.glacialrush.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.component.Controller;
import com.glacialrush.api.object.GMap;
import com.glacialrush.composite.Map;
import com.glacialrush.composite.data.MapData;
import com.glacialrush.composite.data.PlayerData;
import net.md_5.bungee.api.ChatColor;

public class DataController extends Controller
{
	private File maps;
	private File players;
	
	private GMap<Player, PlayerData> cache;
	
	public DataController(GlacialServer pl)
	{
		super(pl);
		
		cache = new GMap<Player, PlayerData>();
		
		maps = new File(pl.getDataFolder(), "maps");
		players = new File(pl.getDataFolder(), "players");
	}
	
	public void preEnable()
	{
		verify(maps);
		verify(players);
		loadMaps();
		
		super.preEnable();
	}
	
	public void postEnable()
	{
		super.postEnable();
	}
	
	public void preDisable()
	{
		super.preDisable();
	}
	
	public void postDisable()
	{
		saveMaps();
		super.postDisable();
	}
	
	public void loadPlayer(Player p)
	{
		if(cache.containsKey(p))
		{
			return;
		}
		
		File f = new File(players, p.getUniqueId().toString() + ".gp");
		
		if(f.exists())
		{
			FileConfiguration fc = new YamlConfiguration();
			
			try
			{
				fc.load(f);
				cache.put(p, new PlayerData(fc));
				o("Loaded Player: " + p.getName() + " :: " + p.getUniqueId().toString());
				return;
			}
			
			catch(IOException | InvalidConfigurationException e)
			{
				e.printStackTrace();
				pl.f("ERROR LOADING PLAYER DATA: " + p.getUniqueId());
			}
		}
		
		else
		{
			cache.put(p, new PlayerData(p));
			o("New Player: " + p.getName() + " :: " + p.getUniqueId().toString());
		}
	}
	
	public void savePlayer(Player p)
	{
		if(!cache.containsKey(p))
		{
			return;
		}
		
		File f = new File(players, p.getUniqueId().toString() + ".gp");
		
		verifyFile(f);
		
		try
		{
			cache.get(p).toFileConfiguration().save(f);
			o("Saved Player: " + p.getName() + " :: " + p.getUniqueId().toString());
			cache.remove(p);
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
			pl.f("ERROR SAVING PLAYER DATA: " + p.getUniqueId());
		}
	}
	
	public void loadPlayers()
	{
		for(Player i : pl.onlinePlayers())
		{
			loadPlayer(i);
		}
	}
	
	public void savePlayers()
	{
		for(Player i : cache.keySet())
		{
			savePlayer(i);
		}
	}
	
	public void saveMaps()
	{
		for(Map i : ((GlacialServer)pl).getGameController().getMaps())
		{
			saveMap(i.getData());
		}
	}
	
	public void loadMaps()
	{
		for(File i : maps.listFiles())
		{
			if(i.isDirectory())
			{
				continue;
			}
			
			if(i.getName().endsWith(".grap"))
			{
				Map map = loadMap(i);
				
				if(map != null)
				{
					((GlacialServer)pl).getGameController().getMaps().add(map);
				}
				
				else
				{
					i.delete();
				}
			}
		}
	}
	
	public void saveMap(MapData mapData)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(new File(maps, mapData.getName().toLowerCase().replace(' ', '-') + ".grap"));
			GZIPOutputStream gzo = new GZIPOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(gzo);
			
			oos.writeObject(mapData);
			oos.close();
			s("Saved Map: " + mapData.getName());
		}
		
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Map loadMap(File file)
	{
		try
		{
			FileInputStream fin = new FileInputStream(file);
			GZIPInputStream gzi = new GZIPInputStream(fin);
			ObjectInputStream ois = new ObjectInputStream(gzi);
			
			Object o = ois.readObject();
			ois.close();
			
			if(o != null)
			{
				MapData mapData = (MapData) o;
				Map map = mapData.toMap((GlacialServer) pl);
				
				if(map != null)
				{
					s("Loaded Map: " + map.getName());
					map.setLocked(true);
					map.build();
					return map;
				}
			}
		}
		
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		pl.f("Failed to load Map " + file.getName());
		pl.f("Map is incompatible, deleted. heh.");
		file.delete();
		
		return null;
	}
	
	public PlayerData getPlayer(Player p)
	{
		return cache.get(p);
	}
	
	public void verify(File file)
	{
		if(!file.exists())
		{
			file.mkdirs();
		}
		
		o("Verifying Directory: " + ChatColor.RED + file.getPath());
	}
	
	public void verifyFile(File file)
	{
		verify(file.getParentFile());
		
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
				o("Created File: " + ChatColor.RED + file.getPath());
			}
			
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler
	public void onPlayer(PlayerJoinEvent e)
	{
		loadPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onPlayer(PlayerQuitEvent e)
	{
		savePlayer(e.getPlayer());
	}
}
