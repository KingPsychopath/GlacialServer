package com.glacialrush.composite;

import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.glacialrush.api.object.GList;
import com.glacialrush.composite.Hunk.HunkFace;
import com.glacialrush.game.Game;
import com.glacialrush.game.event.FactionCaptureEvent;
import com.glacialrush.xapi.UMap;

public class Capture
{
	private Location location;
	private Faction offense;
	private Faction defense;
	private Integer state;
	private Region region;
	private Faction secured;
	
	private static final int size = 1;
	
	public Capture(Region region, Location location)
	{
		this.location = location;
		this.region = region;
		this.state = 100;
		this.offense = null;
		this.defense = Faction.neutral();
		this.secured = Faction.neutral();
	}
	
	public GList<Player> getPlayers()
	{
		GList<Player> players = new GList<Player>();
		
		for(Player i : region.getGame().pl().onlinePlayers())
		{
			if(getRegion().contains(i))
			{
				if(location.distanceSquared(i.getLocation()) < 25)
				{
					players.add(i);
				}
			}
		}
		
		return players;
	}
	
	public void tick(Game g)
	{
		if(!g.isRunning())
		{
			return;
		}
		
		UMap<Player, Faction> players = g.getState().getFactionMap().getFactions(getPlayers());
		UMap<Faction, Integer> points = new UMap<Faction, Integer>();
		
		if(players.isEmpty())
		{
			return;
		}
		
		for(Faction i : Faction.all())
		{
			if(!canCapture(i))
			{
				Iterator<Player> it = players.keySet().iterator();
				
				while(it.hasNext())
				{
					Player p = it.next();
					
					if(players.get(p).equals(i))
					{
						players.remove(p);
					}
				}
			}
		}
		
		for(Player i : players.keySet())
		{
			if(!points.containsKey(players.get(i)))
			{
				points.put(players.get(i), 1);
			}
			
			else
			{
				points.put(players.get(i), points.get(players.get(i)) + 1);
			}
		}
		
		int mdefense = 0;
		int moffense = 0;
		int moffenseMain = 0;
		int moffenseStray = 0;
		
		int a = 0;
		int b = 0;
		
		Faction fa = null;
		Faction fb = null;
		Faction foffenseMain = null;
		Faction foffenseStray = null;
		
		if(points.containsKey(defense))
		{
			mdefense = points.get(defense);
		}
		
		for(Faction i : points.keySet())
		{
			if(!i.equals(defense))
			{
				if(fa == null)
				{
					fa = i;
					a++;
				}
				
				else if(fa.equals(i))
				{
					a++;
				}
				
				else
				{
					fb = i;
					b++;
				}
			}
		}
		
		if(a > b)
		{
			foffenseMain = fa;
			foffenseStray = fb;
			moffenseMain = a;
			moffenseStray = b;
		}
		
		else if(b > a)
		{
			foffenseMain = fb;
			foffenseStray = fa;
			moffenseMain = b;
			moffenseStray = a;
		}
		
		else
		{
			foffenseMain = fa;
			foffenseStray = fb;
			moffenseMain = a;
			moffenseStray = b;
		}
		
		offense = foffenseMain;
		moffense = moffenseMain + moffenseStray;
		
		if(moffense == 0 && mdefense != 0)
		{
			return;
		}
		
		if(mdefense > moffense)
		{
			state += size;
			g.pl().callEvent(new FactionCaptureEvent(this, offense, moffense, mdefense, moffenseStray));
		}
		
		else if(mdefense < moffense)
		{
			state -= size;
			g.pl().callEvent(new FactionCaptureEvent(this, offense, moffense, mdefense, moffenseStray));
		}
		
		if(state > 100)
		{
			state = 100;
			offense = null;
			secured = defense;
		}
		
		if(state < -100)
		{
			state = -100;
			region.capture(this, offense);
			secured = offense;
		}
	}
	
	public void setSecured(Faction secured)
	{
		this.secured = secured;
	}
	
	public boolean canCapture(Faction f)
	{
		if(region.getFaction().equals(f))
		{
			return true;
		}
		
		for(HunkFace i : HunkFace.values())
		{
			if(region.getMap().getRegion(region.getHunk().getRelative(i)) != null && f.equals(region.getMap().getRegion(region.getHunk().getRelative(i)).getFaction()))
			{
				return true;
			}
		}
		
		return false;
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
		return secured;
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
