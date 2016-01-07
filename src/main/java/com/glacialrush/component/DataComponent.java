package com.glacialrush.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.GlacialPlugin;
import com.glacialrush.api.component.GlacialComponent;
import com.glacialrush.api.object.GList;
import com.glacialrush.composite.Map;
import com.glacialrush.composite.MapData;

public class DataComponent extends GlacialComponent
{
	private File base;
	
	public DataComponent(GlacialPlugin pl)
	{
		super(pl);
	}
	
	public void preEnable()
	{
		base = new File(pl.getDataFolder(), "maps");
		
		if(!base.exists())
		{
			verify(base);
		}
	}
	
	public void saveAll(GList<Map> maps)
	{
		for(Map i : maps)
		{
			File mFile = new File(base, i.getName().toLowerCase().replace(" ", "-") + ".grap");
			
			try
			{
				saveMap(i, mFile);
			}
			
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public GList<Map> loadAll()
	{
		GList<Map> maps = new GList<Map>();
		
		for(File i : base.listFiles())
		{
			try
			{
				Map map = loadMap(i);
				
				for(Map j : maps)
				{
					if(j.getName().equals(map.getName()))
					{
						continue;
					}
				}
				
				maps.add(map);
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		return maps;
	}
	
	public void saveMap(Map map, File file) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		GZIPOutputStream gzo = new GZIPOutputStream(fos);
		ObjectOutputStream oos = new ObjectOutputStream(gzo);
		
		MapData md = MapData.fromMap(map);
		oos.writeObject(md);
		oos.close();
	}
	
	public Map loadMap(File file) throws IOException, ClassNotFoundException
	{
		if(file.exists() && !file.isDirectory() && file.getName().endsWith(".grap"))
		{
			FileInputStream fin = new FileInputStream(file);
			GZIPInputStream gzi = new GZIPInputStream(fin);
			ObjectInputStream ois = new ObjectInputStream(gzi);
			
			MapData md = (MapData) ois.readObject();
			
			ois.close();
			
			if(md == null)
			{
				throw new IOException();
			}
			
			return md.toMap((GlacialServer) pl);
		}
		
		else
		{
			throw new IOException("Cannot find file, or does not end in .grap");
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
	
	public void verifyFile(File file)
	{
		if(!file.getParentFile().exists())
		{
			verify(file.getParentFile());
		}
		
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
