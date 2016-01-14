package com.glacialrush.component;

import java.util.Iterator;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.component.Controller;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.thread.GlacialTask;
import com.glacialrush.composite.Job;
import com.glacialrush.composite.Job.JobStatus;
import net.md_5.bungee.api.ChatColor;

public class JobController extends Controller
{
	private int tickLimit;
	
	private GList<Job> jobs;
	
	public JobController(GlacialServer pl)
	{
		super(pl);
		
		this.tickLimit = 45;
		
		jobs = new GList<Job>();
	}
	
	public void preEnable()
	{
		super.preEnable();
		
		o("Starting Job Thread");
		
		pl.newThread(new GlacialTask()
		{
			public void run()
			{
				if(!jobs.isEmpty())
				{
					Iterator<Job> it = jobs.iterator();
					
					while(it.hasNext())
					{
						Job j = it.next();
						
						if(j.getStatus().equals(JobStatus.NEW))
						{
							j.execute((GlacialServer) pl);
							o("Started Job: " + ChatColor.LIGHT_PURPLE +  j.getName());
						}
						
						else if(j.getStatus().equals(JobStatus.WAITING))
						{
							j.execute((GlacialServer) pl);
						}
						
						else if(j.getStatus().equals(JobStatus.FINISHED))
						{
							it.remove();
							o("Finished Job: " + ChatColor.GREEN + j.getName() + " " + j.getRunTime() + "ms");
						}
					}
				}
			}
		});
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
	
	public double jobLoad()
	{
		return tickLimit / (double)((double)jobs.size() / (double)4);
	}
	
	public void addJob(Job j)
	{
		for(Job i : jobs)
		{
			if(j.getName().equals(i.getName()))
			{
				j.setName(j.getName() + "-");
				addJob(j);
				return;
			}
		}
		
		jobs.add(j);
	}
}
