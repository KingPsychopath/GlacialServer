package com.glacialrush.game.event;

import com.glacialrush.composite.Capture;
import com.glacialrush.composite.Faction;

public class FactionCaptureEvent extends CapturePointEvent
{
	private Faction offense;
	private Integer op;
	private Integer dp;
	private Integer opf;
	
	public FactionCaptureEvent(Capture capturePoint, Faction offense, Integer op, Integer dp, Integer opf)
	{
		super(capturePoint);
		
		this.offense = offense;
		this.op = op;
		this.dp = dp;
		this.opf = opf;
	}

	public Faction getOffense()
	{
		return offense;
	}

	public void setOffense(Faction offense)
	{
		this.offense = offense;
	}

	public Integer getOp()
	{
		return op;
	}

	public void setOp(Integer op)
	{
		this.op = op;
	}

	public Integer getDp()
	{
		return dp;
	}

	public void setDp(Integer dp)
	{
		this.dp = dp;
	}

	public Integer getOpf()
	{
		return opf;
	}

	public void setOpf(Integer opf)
	{
		this.opf = opf;
	}
}
