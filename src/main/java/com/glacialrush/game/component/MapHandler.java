package com.glacialrush.game.component;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.glacialrush.composite.Capture;
import com.glacialrush.composite.Faction;
import com.glacialrush.composite.Region;
import com.glacialrush.game.Game;
import com.glacialrush.game.GameComponent;
import com.glacialrush.game.Tickrement;
import com.glacialrush.game.Tickreval;
import com.glacialrush.game.event.RegionCaptureEvent;
import net.md_5.bungee.api.ChatColor;

@Tickrement(Tickreval.HALFSECOND)
public class MapHandler implements GameComponent, Listener
{
	boolean rst = false;
	
	@Override
	public void onTick(Game g)
	{
		g.getState().getMap().tick();
	}

	@Override
	public void onStart(Game g)
	{
		g.pl().register(this);
	}

	@Override
	public void onStop(Game g)
	{
		g.pl().unRegister(this);
	}
	
	@EventHandler
	public void onRegionCapture(final RegionCaptureEvent e)
	{
		if(rst)
		{
			return;
		}
		
		for(Capture i : e.getRegion().getCaptures())
		{
			i.getLocation().getWorld().strikeLightningEffect(i.getLocation());
		}
		
		for(Player i : e.getRegion().getGame().pl().onlinePlayers())
		{
			i.sendMessage(ChatColor.DARK_GRAY + "[" + e.getFaction().getColor() + e.getFaction().getName() + ChatColor.DARK_GRAY + "]: " + ChatColor.AQUA + "Captured " + ChatColor.GREEN + e.getRegion().getName());
		}
		
		Faction tf = null;
		boolean locked = true;
		
		for(Region i : e.getRegion().getMap().getRegions())
		{
			if(tf == null)
			{
				tf = i.getFaction();
			}
			
			if(!tf.equals(i.getFaction()))
			{
				locked = false;
			}
		}
		
		if(locked)
		{
			if(rst)
			{
				return;
			}
			
			for(Player i : e.getRegion().getGame().pl().onlinePlayers())
			{
				i.sendMessage(ChatColor.DARK_GRAY + "[" + e.getFaction().getColor() + e.getFaction().getName() + ChatColor.DARK_GRAY + "]: " + ChatColor.AQUA + "Victorious! New Game Starting!");
				e.getRegion().getMap().accent(Faction.neutral());
				rst = true;
				
				e.getRegion().getGame().pl().scheduleSyncTask(80, new Runnable()
				{
					@Override
					public void run()
					{
						e.getRegion().getGame().pl().restart();
					}
				});
				
				for(Player j : e.getRegion().getGame().pl().onlinePlayers())
				{
					j.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 3600, 20));
					j.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3600, 20));
					j.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 3600, 20));
				}
				
				return;
			}
		}
	}
}
