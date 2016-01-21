package com.glacialrush.controller;

import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.glacialrush.GlacialServer;
import com.glacialrush.Info;
import com.glacialrush.api.GlacialPlugin;
import com.glacialrush.api.component.Controller;
import com.glacialrush.api.game.Game;
import com.glacialrush.api.game.object.Faction;
import com.glacialrush.api.game.object.Job;
import com.glacialrush.api.map.Chunklet;
import com.glacialrush.api.map.Map;
import com.glacialrush.api.map.Region;
import com.glacialrush.api.map.region.Edge;
import com.glacialrush.api.map.region.Scenery;
import com.glacialrush.api.map.region.Territory;
import com.glacialrush.api.map.region.Village;
import com.glacialrush.api.object.GBiset;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.object.GMap;
import net.md_5.bungee.api.ChatColor;

public class CommandController extends Controller implements CommandExecutor
{
	private GlacialServer gs;
	private GMap<Player, GBiset<Map, Region>> selection;
	private GMap<Player, Integer> brushers;
	
	public CommandController(GlacialPlugin pl)
	{
		super(pl);
		
		gs = (GlacialServer) pl;
		selection = new GMap<Player, GBiset<Map, Region>>();
		brushers = new GMap<Player, Integer>();
	}
	
	public void set(Player p, Map map)
	{
		selection.put(p, new GBiset<Map, Region>(map, null));
	}
	
	public void set(Player p, Region region)
	{
		selection.put(p, new GBiset<Map, Region>(region.getMap(), region));
	}
	
	public GBiset<Map, Region> get(Player p)
	{
		return selection.get(p);
	}
	
	public boolean brushing(Player p)
	{
		return brushers.containsKey(p);
	}
	
	public void brush(Player p)
	{
		if(brushing(p))
		{
			brushers.remove(p);
			s(p, "Stopped Brushing " + ChatColor.AQUA + get(p).getB().getName());
			p.getInventory().clear();
		}
		
		else
		{
			brushers.put(p, 1);
			s(p, "Started Brushing " + ChatColor.AQUA + get(p).getB().getName());
			p.getInventory().clear();
			
			ItemStack add = new ItemStack(Material.SLIME_BALL);
			ItemStack sub = new ItemStack(Material.MAGMA_CREAM);
			
			ItemMeta am = add.getItemMeta();
			ItemMeta sm = sub.getItemMeta();
			
			am.setDisplayName(ChatColor.GREEN + "Add");
			sm.setDisplayName(ChatColor.RED + "Subtract");
			
			add.setItemMeta(am);
			sub.setItemMeta(sm);
			
			p.getInventory().setItem(0, add);
			p.getInventory().setItem(1, sub);
		}
	}
	
	public boolean hasMap(Player p)
	{
		if(get(p) != null && get(p).getA() != null)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean hasRegion(Player p)
	{
		if(get(p) != null && get(p).getA() != null && get(p).getB() != null)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean has(Player p)
	{
		return selection.containsKey(p);
	}
	
	public void clear(Player p)
	{
		selection.remove(p);
	}
	
	public void msg(CommandSender sender, String msg)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			String t = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "G";
			
			if(has(p))
			{
				if(hasMap(p))
				{
					t = t + ChatColor.GREEN + " - " + get(p).getA().getName();
				}
				
				if(hasRegion(p))
				{
					t = t + ChatColor.YELLOW + " > " + get(p).getB().getName();
				}
			}
			
			p.sendMessage(t + ChatColor.DARK_GRAY + "]" + ChatColor.AQUA + ": " + ChatColor.WHITE + msg);
		}
	}
	
	public void f(CommandSender sender, String msg)
	{
		msg(sender, ChatColor.RED + msg);
	}
	
	public void s(CommandSender sender, String msg)
	{
		msg(sender, ChatColor.GREEN + msg);
	}
	
	public void w(CommandSender sender, String msg)
	{
		msg(sender, ChatColor.YELLOW + msg);
	}
	
	public void n(CommandSender sender, String msg)
	{
		msg(sender, ChatColor.AQUA + msg);
	}
	
	public String subWord(String[] subs, int start)
	{
		String n = "";
		
		for(int i = start; i < subs.length; i++)
		{
			n = n + " " + subs[i];
		}
		
		return n.substring(1);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		Player p = null;
		Boolean isPlayer = false;
		Boolean isAdmin = sender.hasPermission(Info.PERM_ADMIN);
		Boolean isBuilder = sender.hasPermission(Info.PERM_BUILDER);
		Integer len = args.length;
		String sub = len > 0 ? args[0] : "";
		
		if(sender instanceof Player)
		{
			isPlayer = true;
			p = (Player) sender;
		}
		
		if(command.getName().equalsIgnoreCase(Info.CMD_MAP))
		{
			if(isPlayer && isBuilder)
			{
				if(len > 0)
				{
					if(sub.equalsIgnoreCase("list") || sub.equalsIgnoreCase("ls"))
					{
						if(gs.getGameController().getMaps().isEmpty())
						{
							f(p, "No maps.");
						}
						
						else
						{
							s(p, "--------------------------");
							for(Map i : gs.getGameController().getMaps())
							{
								s(p, ChatColor.AQUA + i.getName());
							}
							s(p, "--------------------------");
						}
					}
					
					if(sub.equalsIgnoreCase("create") || sub.equalsIgnoreCase("new"))
					{
						if(len > 1)
						{
							String name = subWord(args, 1);
							
							for(Map i : gs.getGameController().getMaps())
							{
								if(i.getName().equalsIgnoreCase(name))
								{
									f(p, "That is already a map.");
									return true;
								}
							}
							
							Map map = new Map(name, gs.getGameController().getBuildGame());
							
							gs.getGameController().getMaps().add(map);
							set(p, map);
							
							s(p, "Created and Selected " + ChatColor.AQUA + map.getName());
						}
						
						else
						{
							f(p, "/map new <name with spaces>");
						}
					}
					
					if(sub.equalsIgnoreCase("unselect") || sub.equalsIgnoreCase("unsel"))
					{
						if(hasMap(p))
						{
							clear(p);
							s(p, "Cleared Map Selection");
							
							if(brushing(p))
							{
								n(p, "Brushing Mode Disabled (ya had it selected)");
							}
						}
						
						else
						{
							f(p, "No Map Selection");
						}
					}
					
					if(sub.equalsIgnoreCase("info") || sub.equalsIgnoreCase("i"))
					{
						if(hasMap(p))
						{
							Map map = get(p).getA();
							
							s(p, "--------------------------");
							s(p, ChatColor.AQUA + map.getName());
							s(p, ChatColor.AQUA + "" + map.getRegions().size() + " Regions");
							s(p, "--------------------------");
						}
						
						else
						{
							f(p, "No Map Selection");
						}
					}
					
					if(sub.equalsIgnoreCase("build") || sub.equalsIgnoreCase("b"))
					{
						if(hasMap(p))
						{
							Map map = get(p).getA();
							
							if(!map.building())
							{
								map.build();
								s(p, "Started Build Task for " + map.getName());
							}
							
							else
							{
								f(p, map.getName() + " Is already building (" + map.buildProgress() + ")");
							}
						}
						
						else
						{
							f(p, "No Map Selection");
						}
					}
					
					if(sub.equalsIgnoreCase("accent") || sub.equalsIgnoreCase("a"))
					{
						if(hasMap(p))
						{
							if(len == 2)
							{
								Faction f = Faction.neutral();
								
								for(Faction i : Faction.all())
								{
									if(i.getName().equalsIgnoreCase(args[1]) || i.getName().toLowerCase().contains(args[1].toLowerCase()))
									{
										f = i;
									}
								}
								
								Map map = get(p).getA();
								
								if(!map.building())
								{
									map.accent(f.getDyeColor());
									s(p, "Started Accent Task for " + map.getName() + f.getColor() + f.getName());
								}
								
								else
								{
									f(p, map.getName() + " Is already accenting (" + map.accentProgress() + ")");
								}
							}
							
							else
							{
								f(p, "/map accent <faction name>");
							}
						}
						
						else
						{
							f(p, "No Map Selection");
						}
					}
					
					if(sub.equalsIgnoreCase("select") || sub.equalsIgnoreCase("sel"))
					{
						if(len > 1)
						{
							String name = subWord(args, 1);
							
							for(Map i : gs.getGameController().getMaps())
							{
								if(i.getName().equalsIgnoreCase(name) || i.getName().toLowerCase().contains(name.toLowerCase()))
								{
									set(p, i);
									s(p, "Selected Map " + ChatColor.AQUA + i.getName());
									return true;
								}
							}
							
							f(p, "That's not a map.");
							return true;
						}
						
						else
						{
							for(Map i : gs.getGameController().getMaps())
							{
								if(i.contains(p))
								{
									set(p, i);
									s(p, "Selected Map " + ChatColor.AQUA + i.getName());
									return true;
								}
							}
							
							f(p, "That's not a map. Supply a name, or get in a map.");
							return true;
						}
					}
				}
			}
		}
		
		if(command.getName().equalsIgnoreCase(Info.CMD_BRUSH))
		{
			if(isPlayer && isBuilder)
			{
				if(len > 0)
				{
					if(!brushing(p))
					{
						f(p, "Be the /brush, Feel the /brush... Idiot.");
						return true;
					}
					
					try
					{
						Integer rad = Integer.parseInt(sub);
						
						if(rad < 1 || rad > 8)
						{
							f(p, "Invalid Brush Size (1 - 8)");
						}
						
						else
						{
							brushers.put(p, rad);
							s(p, "Brush size set to " + ChatColor.AQUA + rad);
						}
					}
					
					catch(NumberFormatException e)
					{
						f(p, "Not a number. /b <radius>");
					}
				}
				
				else
				{
					if(hasRegion(p))
					{
						brush(p);
					}
					
					else
					{
						f(p, "No Region Selected.");
					}
				}
			}
		}
		
		if(command.getName().equalsIgnoreCase(Info.CMD_REGION))
		{
			if(isPlayer && isBuilder)
			{
				if(len > 0)
				{
					if(sub.equalsIgnoreCase("list") || sub.equalsIgnoreCase("ls"))
					{
						if(hasMap(p))
						{
							Map m = get(p).getA();
							
							if(m.getRegions().isEmpty())
							{
								f(p, "No Regions.");
							}
							
							else
							{
								s(p, "--------------------------");
								for(Region i : m.getRegions())
								{
									s(p, ChatColor.AQUA + i.getName() + ChatColor.YELLOW + " [" + i.getType().toString() + "]");
								}
								s(p, "--------------------------");
							}
						}
						
						else
						{
							f(p, "No Map Selected");
						}
					}
					
					if(sub.equalsIgnoreCase("create") || sub.equalsIgnoreCase("new"))
					{
						if(!hasMap(p))
						{
							f(p, "No map selected");
							return true;
						}
						
						if(len > 2)
						{
							String m = args[1];
							String name = subWord(args, 2);
							Game game = gs.getGameController().getBuildGame();
							Map map = get(p).getA();
							Region region = null;
							
							if(m.equalsIgnoreCase("t"))
							{
								region = new Territory(map, name, game);
							}
							
							else if(m.equalsIgnoreCase("v"))
							{
								region = new Village(map, name, game);
							}
							
							else if(m.equalsIgnoreCase("s"))
							{
								region = new Scenery(map, name, game);
							}
							
							else if(m.equalsIgnoreCase("e"))
							{
								region = new Edge(map, name, game);
							}
							
							else
							{
								f(p, "Invalid Mode. t=territory v=village s=scenery e=edge");
								return true;
							}
							
							map.getRegions().add(region);
							set(p, region);
							s(p, "Created and Selected " + ChatColor.AQUA + region.getName());
						}
						
						else
						{
							f(p, "/r new [t/v/s/e] <name spaced>");
						}
					}
					
					if(sub.equalsIgnoreCase("unselect") || sub.equalsIgnoreCase("unsel"))
					{
						if(!hasMap(p))
						{
							f(p, "No Map selected");
						}
						
						if(hasRegion(p))
						{
							get(p).setB(null);
							s(p, "Cleared Region Selection");
							
							if(brushing(p))
							{
								n(p, "Brushing Mode Disabled (ya had it selected)");
							}
						}
						
						else
						{
							f(p, "No Region Selection");
						}
					}
					
					if(sub.equalsIgnoreCase("info") || sub.equalsIgnoreCase("i"))
					{
						if(!hasMap(p))
						{
							f(p, "No Map selected");
						}
						
						if(hasRegion(p))
						{
							Map map = get(p).getA();
							Region region = get(p).getB();
							
							s(p, "--------------------------");
							s(p, ChatColor.AQUA + map.getName());
							s(p, ChatColor.AQUA + "" + map.getRegions().size() + " Regions");
							s(p, "--------------------------");
							s(p, ChatColor.YELLOW + region.getName());
							s(p, ChatColor.YELLOW + "Type: " + ChatColor.GOLD + region.getType().toString());
							s(p, ChatColor.YELLOW + "Chunklets: " + ChatColor.GOLD + region.getChunklets().size());
							s(p, "--------------------------");
						}
						
						else
						{
							f(p, "No Region Selection");
						}
					}
					
					if(sub.equalsIgnoreCase("select") || sub.equalsIgnoreCase("sel"))
					{
						if(len > 1)
						{
							if(hasMap(p))
							{
								Map m = get(p).getA();
								String name = subWord(args, 1);
								
								for(Region i : m.getRegions())
								{
									if(i.getName().equalsIgnoreCase(name) || i.getName().toLowerCase().contains(name.toLowerCase()))
									{
										set(p, i);
										s(p, "Selected Region " + ChatColor.AQUA + m.getName() + ChatColor.YELLOW + " / " + i.getName());
										return true;
									}
								}
							}
							
							else
							{
								f(p, "No Map Selected");
							}
						}
						
						else
						{
							for(Map i : gs.getGameController().getMaps())
							{
								if(i.contains(p))
								{
									for(Region j : i.getRegions())
									{
										if(j.contains(p))
										{
											set(p, j);
											s(p, "Selected Region " + ChatColor.AQUA + i.getName() + ChatColor.YELLOW + " / " + j.getName());
											return true;
										}
									}
								}
							}
							
							f(p, "That's not a region.");
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayer(PlayerInteractEvent e)
	{
		if(!brushing(e.getPlayer()))
		{
			return;
		}
		
		Player p = e.getPlayer();
		Location l = e.getPlayer().getTargetBlock((HashSet<Byte>) null, 256).getLocation();
		Region r = get(p).getB();
		GList<Location> last = null;
		
		e.getPlayer().sendMessage(e.getAction().toString());
		
		if(e.getAction().equals(Action.LEFT_CLICK_AIR))
		{
			if(e.getItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Add") && e.getItem().getType().equals(Material.SLIME_BALL))
			{
				last = r.getOutline(l.getBlockY());
				r.addChunklets(new Chunklet(l).getCircle(brushers.get(p)));
			}
			
			else if(e.getItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Subtract") && e.getItem().getType().equals(Material.MAGMA_CREAM))
			{
				last = r.getOutline(l.getBlockY());
				r.delChunklets(new Chunklet(l).getCircle(brushers.get(p)));
			}
			
			else
			{
				return;
			}
			
			Job j = new Job("Region Outline Task for " + r.getMap().getName() + " > " + r.getName(), gs.getGameController().getBuildGame());
			GList<Location> ne = r.getOutline(l.getBlockY());
			
			for(Location k : last)
			{
				if(ne.contains(k))
				{
					continue;
				}
				
				j.queue(k, k.getBlock().getType(), p);
			}
			
			for(Location k : ne)
			{
				if(last.contains(k))
				{
					continue;
				}
				
				j.queue(k, r.getType().material(), p);
			}
			
			j.flush();
			
			r.getMap().draw(p, l.getBlockY());
		}
	}
}
