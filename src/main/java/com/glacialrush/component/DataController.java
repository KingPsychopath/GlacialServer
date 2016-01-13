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
import com.glacialrush.GlacialServer;
import com.glacialrush.api.component.Controller;
import com.glacialrush.composite.Map;
import com.glacialrush.composite.data.MapData;

public class DataController extends Controller
{
	private File maps;
	
	public DataController(GlacialServer pl)
	{
		super(pl);
		
		maps = new File(pl.getDataFolder(), "maps");
	}
	
	public void preEnable()
	{
		verify(maps);
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
		super.postDisable();
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
			pl.s("Saved Map: " + mapData.getName());
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
					pl.s("Loaded Map: " + map.getName());
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
		
		return null;
	}
	
	public void verify(File file)
	{
		if(!file.exists())
		{
			file.mkdirs();
		}
	}
	
	public void verifyFile(File file)
	{
		verify(file.getParentFile());
		
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
