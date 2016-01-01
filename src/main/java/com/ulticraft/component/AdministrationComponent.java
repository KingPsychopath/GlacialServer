package com.ulticraft.component;

import java.util.HashSet;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.ulticraft.GlacialServer;
import com.ulticraft.composite.Hunk;
import com.ulticraft.composite.Map;
import com.ulticraft.uapi.Component;
import com.ulticraft.uapi.UMap;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class AdministrationComponent extends Component implements Listener
{
	private UMap<Player, String> administrators;
	
	public AdministrationComponent(GlacialServer pl)
	{
		super(pl);
		
		administrators = new UMap<Player, String>();
	}
	
	public void enable()
	{
		pl.register(this);
		pl.scheduleSyncRepeatingTask(0, 5, new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : administrators.keySet())
				{
					if(administrators.get(i).equals("_"))
					{
						if(i.getInventory().getItem(0) != null && i.getInventory().getItem(0).getItemMeta().getDisplayName().contains("New Map"))
						{
							continue;
						}
						
						ItemStack newMap = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIME.getData());
						ItemMeta nim = newMap.getItemMeta();
						nim.setDisplayName("New Map");
						newMap.setItemMeta(nim);
						
						ItemStack editMap = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.ORANGE.getData());
						ItemMeta eim = editMap.getItemMeta();
						eim.setDisplayName("Edit Map");
						editMap.setItemMeta(eim);
						
						i.getInventory().setItem(0, newMap);
						i.getInventory().setItem(1, editMap);
					}
					
					else
					{
						if(i.getInventory().getItem(0) != null && i.getInventory().getItem(0).getItemMeta().getDisplayName().contains("I WURK"))
						{
							continue;
						}
						
						ItemStack black = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.BLACK.getData());
						ItemMeta bim = black.getItemMeta();
						bim.setDisplayName("I WURK");
						black.setItemMeta(bim);
						ItemStack yellow = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.YELLOW.getData());
						ItemMeta yim = black.getItemMeta();
						yim.setDisplayName("Construction");
						yellow.setItemMeta(yim);
						boolean blakk = false;
						
						for(int j = 0; j < 9; j++)
						{
							if(blakk)
							{
								i.getInventory().setItem(j, black);
							}
							
							else
							{
								i.getInventory().setItem(j, yellow);
							}
							
							blakk = !blakk;
						}
					}
				}
			}
		});
	}
	
	public void disable()
	{
	
	}
	
	public void administrate(Player p)
	{
		if(administrators.containsKey(p))
		{
			administrators.remove(p);
		}
		
		else
		{
			administrators.put(p, "_");
		}
	}
	
	@EventHandler
	public void onPlayer(PlayerChatEvent e)
	{
		if(administrators.containsKey(e.getPlayer()))
		{
			if(administrators.get(e.getPlayer()).equals("__"))
			{
				if(pl.getGame().getMap(e.getMessage()) != null)
				{
					e.getPlayer().sendMessage(ChatColor.RED + "That's already a map dumbass.");
					e.setCancelled(true);
					return;
				}
				
				Map map = new Map(pl, e.getMessage(), e.getPlayer().getWorld());
				pl.getGame().getMaps().add(map);
				administrators.put(e.getPlayer(), e.getMessage());
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.YELLOW + "Map Created, Left click to create a region.");
			}
		}
	}
	
	@EventHandler
	public void onPlayer(PlayerInteractEvent e)
	{
		if(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK) || e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if(administrators.containsKey(e.getPlayer()))
			{
				if(administrators.get(e.getPlayer()).equals("_"))
				{
					if(e.getPlayer().getInventory().getHeldItemSlot() == 0)
					{
						e.getPlayer().sendMessage(ChatColor.DARK_GRAY + "-------------");
						e.getPlayer().sendMessage(ChatColor.AQUA + "Title Of Map?");
						administrators.put(e.getPlayer(), "__");
					}
					
					else if(e.getPlayer().getInventory().getHeldItemSlot() == 1)
					{
						
					}
				}
				
				else
				{
					Map m = pl.getGame().getMap(administrators.get(e.getPlayer()));
					
					boolean b = m.addRegionNear(e.getPlayer(), e.getPlayer().getTargetBlock((HashSet<Byte>)null, 256).getLocation());
					
					if(b)
					{
						e.getPlayer().getWorld().strikeLightningEffect(new Hunk(e.getPlayer().getLocation()).getCenter(1));
					}
					
					else
					{
						e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ANVIL_BREAK, 1.0f, 0.3f);
					}
				}
			}
		}
	}
}
