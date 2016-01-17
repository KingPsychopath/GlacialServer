package com.glacialrush.game;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import com.glacialrush.GlacialServer;

public class CombatHandler extends GlacialHandler
{
	public CombatHandler(GlacialServer pl)
	{
		super(pl);
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
				}
			}
		}
	}
}