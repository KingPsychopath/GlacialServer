package com.glacialrush.component;

import com.glacialrush.GlacialServer;
import com.glacialrush.api.component.Controller;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.thread.GlacialTask;
import com.glacialrush.composite.Job;
import com.glacialrush.composite.Job.JobStatus;

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
		
		pl.newThread(new GlacialTask()
		{
			public void run()
			{
				if(!jobs.isEmpty())
				{
					Job j = jobs.get(0);
					
					if(j.getStatus().equals(JobStatus.NEW))
					{
						j.execute((GlacialServer) pl);
						o("Started Job: " + j.getName());
					}
					
					else if(j.getStatus().equals(JobStatus.WAITING))
					{
						j.execute((GlacialServer) pl);
					}
					
					else if(j.getStatus().equals(JobStatus.FINISHED))
					{
						jobs.remove(0);
						o("Finished Job: " + j.getName() + " " + j.getRunTime() + "ms");
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
	
	public int jobLoad()
	{
		return tickLimit;
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
