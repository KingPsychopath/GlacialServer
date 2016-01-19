package com.glacialrush.component;

import org.bukkit.entity.Player;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.component.Controller;
import com.glacialrush.game.event.BattleRankUpEvent;
import com.glacialrush.game.event.ExperienceEvent;
import com.glacialrush.game.event.ExperienceType;

public class ExperienceController extends Controller
{
	public ExperienceController(GlacialServer pl)
	{
		super(pl);
	}
	
	public void preEnable()
	{
		super.preEnable();
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
	
	public long getExperience(Player p)
	{
		return ((GlacialServer) pl).gpd(p).getExperience();
	}
	
	public double getExperienceBonus(Player p)
	{
		return ((GlacialServer) pl).gpd(p).getExperienceBonus();
	}
	
	public long getSkill(Player p)
	{
		return ((GlacialServer) pl).gpd(p).getSkill();
	}
	
	public long getNextSkill(Player p)
	{
		return ((GlacialServer) pl).gpd(p).getNextSkill();
	}
	
	public int getBattleRank(Player p)
	{
		long xp = getExperience(p) + 1;
		
		long a = (xp / (100 + (xp / 200))) / 3;
		
		return (int) (a/4);
	}
	
	public long getExperienceForRank(Player p)
	{
		double cbr = getBattleRank(p) + 1;
		long cxp = getExperience(p);
		long nbr = (long) ((1 / 3) * ((double) (cbr / (double) 100) + (double) (cbr / (double) 200)));
		return ((nbr - cxp) * 4);
	}
	
	public void setExperience(Player p, long experience)
	{
		if(experience < 0)
		{
			return;
		}
		
		((GlacialServer) pl).gpd(p).setExperience(experience);
	}
	
	public void setExperienceBonus(Player p, double experienceBonus)
	{
		((GlacialServer) pl).gpd(p).setExperienceBonus(experienceBonus);
	}
	
	public void setSkill(Player p, long skill)
	{
		if(skill < 0)
		{
			return;
		}
		
		((GlacialServer) pl).gpd(p).setSkill(skill);
	}
	
	public void setNextSkill(Player p, long nextSkill)
	{
		if(nextSkill < 0)
		{
			return;
		}
		
		((GlacialServer) pl).gpd(p).setNextSkill(nextSkill);
	}
	
	public void giveExperience(Player p, long experience, ExperienceType type)
	{
		if(experience < 0)
		{
			return;
		}
		
		int pbr = getBattleRank(p);
		
		experience += (experience * getExperienceBonus(p));
		
		if(((GlacialServer) pl).getGameController().isRunning())
		{
			ExperienceEvent e = new ExperienceEvent(((GlacialServer) pl).getGameController(), p, experience, type);
			pl.callEvent(e);
			
			if(!e.isCancelled())
			{
				experience = e.getAmount();
			}
			
			else
			{
				return;
			}
		}
		
		setExperience(p, getExperience(p) + experience);
		setNextSkill(p, getNextSkill(p) + experience);
		long gnx = getNextSkill(p);
		
		if(gnx >= 100)
		{
			long mnx = gnx / (long) 100;
			
			setNextSkill(p, gnx - (mnx * 100));
			setSkill(p, getSkill(p) + mnx);
		}
		
		if(pbr < getBattleRank(p))
		{
			pl.callEvent(new BattleRankUpEvent(((GlacialServer) pl).getGameController(), p, getBattleRank(p)));
		}
	}
}
