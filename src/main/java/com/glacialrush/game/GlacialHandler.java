package com.glacialrush.game;

import org.bukkit.event.Listener;
import com.glacialrush.GlacialServer;
import com.glacialrush.component.GameController;

public class GlacialHandler implements GameHandler, Listener
{
	protected GlacialServer pl;
	protected GameController g;
	protected long cycleTime;
	protected long cycles;
	
	public GlacialHandler(GlacialServer pl)
	{
		this.pl = pl;
		this.g = pl.getGameController();
		this.cycles = 0;
		this.cycleTime = 0;
		
		g.add(this);
	}
	
	@Override
	public void start()
	{
		
	}

	@Override
	public void begin()
	{
		
	}

	@Override
	public void finish()
	{
		
	}

	@Override
	public void stop()
	{
		
	}

	@Override
	public void tick()
	{
		
	}

	public long getCycleTime()
	{
		return cycleTime;
	}

	public void setCycleTime(long cycleTime)
	{
		this.cycleTime = cycleTime;
	}

	public long getCycles()
	{
		return cycles;
	}

	public void setCycles(long cycles)
	{
		this.cycles = cycles;
	}
}
