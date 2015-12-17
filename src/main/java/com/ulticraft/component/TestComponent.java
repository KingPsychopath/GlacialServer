package com.ulticraft.component;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import com.ulticraft.GlacialRush;
import com.ulticraft.data.Faction;
import com.ulticraft.region.GridLocation;
import com.ulticraft.region.Region;
import com.ulticraft.uapi.Component;
import com.ulticraft.uapi.UList;
import net.md_5.bungee.api.ChatColor;

public class TestComponent extends Component implements Listener
{
	private UList<GridLocation> mms;
	private Region r;
	
	public TestComponent(GlacialRush pl)
	{
		super(pl);
		
		mms = new UList<GridLocation>();
	}
	
	public void enable()
	{
		super.enable();
	}
	
	public void disable()
	{
		super.disable();
	}
	
	@EventHandler
	public void onPlayer(PlayerMoveEvent e)
	{
		if(r == null)
		{
			return;
		}
		
		for(Block i : r.getCapturePoints())
		{
			if(i.getLocation().distanceSquared(e.getTo()) < 9)
			{
				r.setFactionCapture(i, new Faction("tf", ChatColor.BLUE));
			}
		}
	}
	
	@EventHandler
	public void onPlayer(PlayerInteractEvent e)
	{
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getPlayer().isSneaking())
		{
			r = new Region(e.getClickedBlock().getChunk());
			e.getPlayer().sendMessage("ACC: " + r.getAccents().size());
			
			Faction f = new Faction("Test", ChatColor.RED);
			
			r.setFaction(f);
			
			r.drawOutline(e.getPlayer(), Material.STONE, e.getClickedBlock().getLocation().getBlockY());
		}
	}
}
