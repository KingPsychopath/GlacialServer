package com.ulticraft.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import com.ulticraft.GlacialRush;
import com.ulticraft.composite.MapData;
import com.ulticraft.map.Map;
import com.ulticraft.uapi.Component;

public class DataComponent extends Component
{	
	public DataComponent(GlacialRush pl)
	{
		super(pl);
	}
	
	public void enable()
	{
		loadMaps();
	}
	
	public void disable()
	{
		saveMaps();
	}
	
	public void saveMaps()
	{
		for(Map i : pl.getState().getMaps())
		{
			saveMap(i);
		}
	}
	
	public void loadMaps()
	{
		for(File i : new File(pl.getDataFolder(), "maps").listFiles())
		{
			if(i.getName().endsWith(".grap"))
			{
				Map m = loadMap(i);
				boolean safe = true;
				
				for(Map j : pl.getState().getMaps())
				{
					if(j.getName().equals(m))
					{
						safe = false;
					}
				}
				
				if(safe)
				{
					pl.getState().getMaps().add(m);
				}
			}
		}
	}
	
	public void saveMap(Map map)
	{
		if(map.isCompleted())
		{
			MapData md = new MapData(map);
			
			File file = new File(new File(pl.getDataFolder(), "maps"), map.getName().toLowerCase().replace(' ', '-') + ".grap");
			verifyFile(file);
			serialize(md, file);
		}
	}
	
	public Map loadMap(File file)
	{
		MapData md = (MapData) deserialize(MapData.class, file);
		
		if(md != null)
		{
			return new Map(pl, md);
		}
		
		return null;
	}
	
	public void serialize(Object o, File f)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(f);
			GZIPOutputStream gzo = new GZIPOutputStream(fos);
			ObjectOutputStream oos = new ObjectOutputStream(gzo);
			
			oos.writeObject(o);
			oos.close();
		}
		
		catch(Exception e)
		{
			pl.f("DATA FAILURE: Failed to write object to " + f.getPath());
		}
	}
	
	public Object deserialize(Class<?> clazz, File f)
	{
		try
		{
			FileInputStream fin = new FileInputStream(f);
			GZIPInputStream gzi = new GZIPInputStream(fin);
			ObjectInputStream ois = new ObjectInputStream(gzi);
			
			Object o = ois.readObject();
			
			ois.close();
			
			if(o != null && o.getClass().equals(clazz))
			{
				return o;
			}
			
			return null;
		}
		
		catch(Exception e)
		{
			pl.f("DATA FAILURE: Failed to read object from " + f.getPath());
		}
		
		return null;
	}
	
	public void verifyFile(File file)
	{
		verify(file.getParentFile());
		
		try
		{
			file.createNewFile();
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void verify(File file)
	{
		if(!file.getParentFile().exists())
		{
			verify(file.getParentFile());
		}
		
		file.mkdir();
	}
}
