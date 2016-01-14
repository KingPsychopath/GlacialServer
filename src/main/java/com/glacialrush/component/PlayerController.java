package com.glacialrush.component;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.component.Controller;
import com.glacialrush.api.object.GList;

public class PlayerController extends Controller
{
	private GList<Player> disabled;
	
	public PlayerController(GlacialServer pl)
	{
		super(pl);
		
		this.disabled = new GList<Player>();
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
	
	public void disable(Player p)
	{
		if(disabled.contains(p))
		{
			return;
		}
		
		disabled.add(p);
		p.setGameMode(GameMode.ADVENTURE);
		p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100000, 50));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100000, 50));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100000, 50));
	}
	
	public void enable(Player p)
	{
		disabled.remove(p);
		p.setGameMode(GameMode.ADVENTURE);
		p.setHealth(p.getMaxHealth());
		p.removePotionEffect(PotionEffectType.BLINDNESS);
		p.removePotionEffect(PotionEffectType.SLOW);
		p.removePotionEffect(PotionEffectType.SLOW_DIGGING);
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e)
	{
		if(!e.getEntityType().equals(EntityType.PLAYER))
		{
			return;
		}
		
		if(disabled.contains((Player)e.getEntity()))
		{
			e.setCancelled(true);
		}
	}
}
