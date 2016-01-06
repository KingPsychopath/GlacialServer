package com.glacialrush.composite;

import org.bukkit.Location;

public class Capture
{
	private Location location;
	private Faction offense;
	private Faction defense;
	private Integer state;
	private Region region;
	
	public Capture(Region region, Location location)
	{
		this.location = location;
		this.region = region;
		this.state = 100;
		this.offense = null;
		this.defense = Faction.neutral();
	}
	
	public void reset()
	{
		offense = null;
		defense = region.getFaction();
		state = 100;
	}
	
	public boolean enemySecured()
	{
		if(state == -100)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean allySecured()
	{
		if(state == 100)
		{
			return true;
		}
		
		return false;
	}
	
	public Faction getSecured()
	{
		if(state == 100)
		{
			return defense;
		}
		
		else if(state == -100)	
		{
			return offense;
		}
		
		else
		{
			return Faction.neutral();
		}
	}
	
	public void capture(Faction offense, Integer op, Integer dp, Integer opf)
	{
		Faction secured = getSecured();
		
		if(this.offense == null)
		{
			this.offense = offense;
			
			if(op > dp)
			{
				state--;
			}
			
			else if(dp > op)
			{
				state++;
			}
		}
		
		else if(!this.offense.equals(offense))
		{
			if(op > dp)
			{
				if(state == 0)
				{
					if(opf > op - opf)
					{
						this.offense = offense;
					}
					
					state--;
				}
				
				else if(state < 0)
				{
					state--;
				}
			}
			
			else if(dp > op)
			{
				state++;
			}
		}
		
		else if(this.offense.equals(offense))
		{
			if(op > dp)
			{
				state--;
			}
			
			else if(dp > op)
			{
				state++;
			}
		}
		
		if(state < -100)
		{
			state = -100;
		}
		
		if(state > 100)
		{
			this.offense = null;
			state = 100;
		}
		
		if(!secured.equals(getSecured()))
		{
			region.capture(this, getSecured());
		}
	}

	public Location getLocation()
	{
		return location;
	}

	public void setLocation(Location location)
	{
		this.location = location;
	}

	public Faction getOffense()
	{
		return offense;
	}

	public void setOffense(Faction offense)
	{
		this.offense = offense;
	}

	public Faction getDefense()
	{
		return defense;
	}

	public void setDefense(Faction defense)
	{
		this.defense = defense;
	}

	public Integer getState()
	{
		return state;
	}

	public void setState(Integer state)
	{
		this.state = state;
	}

	public Region getRegion()
	{
		return region;
	}

	public void setRegion(Region region)
	{
		this.region = region;
	}
}
