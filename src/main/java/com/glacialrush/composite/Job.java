package com.glacialrush.composite;

import java.util.Iterator;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.object.GList;
import com.glacialrush.component.JobController;

public class Job
{
	public enum JobStatus
	{
		NEW, RUNNING, WAITING, FINISHED
	}
	
	private GList<Manipulation> manipulations;
	private JobStatus status;
	private JobController j;
	private String name;
	private long runTime;
	
	public Job(String name, JobController j)
	{
		this.j = j;
		this.name = name;
		
		manipulations = new GList<Manipulation>();
		status = JobStatus.NEW;
		runTime = 0;
	}
	
	public void execute(GlacialServer pl)
	{
		status = JobStatus.RUNNING;
		
		Iterator<Manipulation> it = manipulations.iterator();
		long msa = System.currentTimeMillis();
		
		while(it.hasNext() && System.currentTimeMillis() - msa < j.jobLoad())
		{
			it.next().execute();
		}
		
		long msb = System.currentTimeMillis() - msa;
		
		runTime+= msb;
		
		if(!it.hasNext())
		{
			status = JobStatus.FINISHED;
		}
		
		else
		{
			status = JobStatus.WAITING;
		}
	}
	
	public long getRunTime()
	{
		return runTime;
	}

	public void setRunTime(long runTime)
	{
		this.runTime = runTime;
	}

	public void add(Location location, DyeColor color)
	{
		add(new Manipulation(location, color));
	}
	
	public void add(Location location, Material material)
	{
		add(new Manipulation(location, material));
	}
	
	public void add(Location location, Material material, Player player)
	{
		add(new Manipulation(location, material, player));
	}

	public void add(Manipulation m)
	{
		manipulations.add(m);
	}
	
	public GList<Manipulation> getManipulations()
	{
		return manipulations;
	}

	public void setManipulations(GList<Manipulation> manipulations)
	{
		this.manipulations = manipulations;
	}

	public JobStatus getStatus()
	{
		return status;
	}

	public void setStatus(JobStatus status)
	{
		this.status = status;
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}
