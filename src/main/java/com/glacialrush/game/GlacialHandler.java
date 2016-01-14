package com.glacialrush.game;

import org.bukkit.event.Listener;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.dispatch.Dispatcher;
import com.glacialrush.component.GameController;

public class GlacialHandler implements GameHandler, Listener
{
	protected GlacialServer pl;
	protected GameController g;
	protected long cycleTime;
	protected long cycles;
	protected GDispatcher d;
	
	public GlacialHandler(GlacialServer pl)
	{
		this.pl = pl;
		this.g = pl.getGameController();
		this.cycles = 0;
		this.cycleTime = 0;
		this.d = new GDispatcher(pl, this);
		
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
	
	public String getName()
	{
		return getClass().getSimpleName();
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
	
	public Dispatcher getDispatcher()
	{
		return d;
	}
	
	public void i(String... o)
	{
		d.info(o);
	}
	
	public void s(String... o)
	{
		d.success(o);
	}
	
	public void f(String... o)
	{
		d.failure(o);
	}
	
	public void w(String... o)
	{
		d.warning(o);
	}
	
	public void v(String... o)
	{
		d.verbose(o);
	}
	
	public void o(String... o)
	{
		d.overbose(o);
	}
	
	public void si(String... o)
	{
		d.sinfo(o);
	}
	
	public void ss(String... o)
	{
		d.ssuccess(o);
	}
	
	public void sf(String... o)
	{
		d.sfailure(o);
	}
	
	public void sw(String... o)
	{
		d.swarning(o);
	}
	
	public void sv(String... o)
	{
		d.sverbose(o);
	}
	
	public void so(String... o)
	{
		d.soverbose(o);
	}
}
