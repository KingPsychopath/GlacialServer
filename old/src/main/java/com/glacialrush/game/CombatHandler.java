package com.glacialrush.game;

import java.util.Random;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.object.GMap;
import com.glacialrush.api.object.GSound;
import com.glacialrush.game.event.ExperienceType;

public class CombatHandler extends GlacialHandler
{
	protected GMap<Player, GList<Player>> assists;
	protected GMap<Player, Integer> power;
	
	public CombatHandler(GlacialServer pl)
	{
		super(pl);
		
		assists = new GMap<Player, GList<Player>>();
		power = new GMap<Player, Integer>();
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
	
	@EventHandler
	public void onPlayer(PlayerDeathEvent e)
	{
		if(e.getEntityType().equals(EntityType.PLAYER))
		{
			Player killed = e.getEntity();
			Player killer = e.getEntity().getKiller();
			
			if(killer != null)
			{
				if(!power.containsKey(killer))
				{
					power.put(killer, 0);
				}
				
				new GSound("g.event.combat.kill", 1f, 0.7f).play(killer);
				
				power.put(killer, power.get(killer) + 1);
				pl.getExperienceController().giveExperience(killer, 50, ExperienceType.KILL);
				
				if(power.containsKey(killed))
				{
					int pow = power.get(killed);
					
					if(pow > 6)
					{
						pl.getExperienceController().giveExperience(killer, 11 * pow, ExperienceType.KILL_HIGH_THREAT);
					}
					
					if(pow > 0)
					{
						pl.getExperienceController().giveExperience(killer, 4 * pow, ExperienceType.KILL_BONUS);
					}
				}
			}
			
			if(assists.containsKey(killed))
			{
				for(Player i : assists.get(killed))
				{
					if(killer != null && killer.equals(i))
					{
						continue;
					}
					
					pl.getExperienceController().giveExperience(i, 25, ExperienceType.KILL_ASSIST);
					
					if(power.containsKey(killed))
					{
						int pow = power.get(killed);
						
						if(pow > 6)
						{
							pl.getExperienceController().giveExperience(i, 6 * pow, ExperienceType.KILL_HIGH_THREAT);
						}
						
						if(pow > 0)
						{
							pl.getExperienceController().giveExperience(i, 2 * pow, ExperienceType.KILL_BONUS);
						}
					}
				}
			}
			
			power.put(killed, 0);
			assists.put(killed, new GList<Player>());
		}
	}
	
	@EventHandler
	public void onCombat(EntityDamageEvent e)
	{
		if(e instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent) e;
			Entity damager = edbeEvent.getDamager();
			Entity defense = edbeEvent.getEntity();
			
			if(damager.getType().equals(EntityType.PLAYER) && defense.getType().equals(EntityType.PLAYER))
			{
				Player o = (Player) damager;
				Player d = (Player) defense;
				
				if(g.getPlayerHandler().getFaction(o).equals(g.getPlayerHandler().getFaction(d)))
				{
					e.setCancelled(true);
				}
				
				else
				{
					if(!assists.containsKey(d))
					{
						assists.put(d, new GList<Player>());
					}
					
					if(!assists.get(d).contains(o))
					{
						assists.get(d).add(o);
					}
					
					if(d.isBlocking())
					{
						new GSound("g.event.combat.block", 1f, new Random().nextFloat() + 0.6f).play(o);
						new GSound("g.event.combat.block", 1f, new Random().nextFloat() + 0.6f).play(d);
					}
				}
			}
			
			else if(damager.getType().equals(EntityType.ARROW))
			{
				Arrow a = (Arrow) damager;
				
				if(a.getShooter() instanceof Player)
				{
					Player o = (Player) a.getShooter();
					Player d = (Player) defense;
					
					if(g.getPlayerHandler().getFaction(o).equals(g.getPlayerHandler().getFaction(d)))
					{
						e.setCancelled(true);
					}
					
					else
					{
						
					}
				}
			}
		}
	}
}
