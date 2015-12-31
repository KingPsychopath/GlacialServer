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
import com.ulticraft.composite.Map;
import com.ulticraft.data.MapData;
import com.ulticraft.uapi.Component;
import com.ulticraft.uapi.UList;

public class DataComponent extends Component
{
	private File base;
	
	public DataComponent(GlacialRush pl)
	{
		super(pl);
	}
	
	public void enable()
	{
		base = new File(pl.getDataFolder(), "maps");
		
		if(!base.exists())
		{
			verify(base);
		}
	}
	
	public void disable()
	{
	
	}
	
	public void saveAll(UList<Map> maps)
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
	
	public UList<Map> loadAll()
	{
		UList<Map> maps = new UList<Map>();
		
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
			
			catch(ClassNotFoundException | IOException e)
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
			
			return md.toMap(pl);
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
