package com.glacialrush.component;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.glacialrush.GlacialServer;
import com.glacialrush.Info;
import com.glacialrush.api.component.Controller;
import com.glacialrush.api.object.GMap;
import com.glacialrush.composite.Map;
import net.md_5.bungee.api.ChatColor;

public class CommandController extends Controller implements CommandExecutor
{
	private GMap<Player, Map> selections;
	
	public CommandController(GlacialServer pl)
	{
		super(pl);
		selections = new GMap<Player, Map>();
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
	
	public boolean has(Player p)
	{
		return selections.containsKey(p);
	}
	
	public Map get(Player p)
	{
		return selections.get(p);
	}
	
	public void set(Player p, Map m)
	{
		selections.put(p, m);
	}
	
	public void clear(Player p)
	{
		selections.remove(p);
	}
	
	public void msg(CommandSender sender, String msg)
	{
		String tag = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "Grush";
		
		if((sender instanceof Player) && has((Player) sender))
		{
			tag = tag + ChatColor.GREEN + " - " + get((Player) sender).getName() + ChatColor.DARK_GRAY + "]: " + ChatColor.WHITE + msg;
		}
		
		else
		{
			tag = tag + ChatColor.DARK_GRAY + "]: " + ChatColor.WHITE + msg;
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

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(command.getName().equalsIgnoreCase(Info.CMD_GLACIALRUSH))
		{
			Player p = (sender instanceof Player) ? (Player)sender : null;
			Boolean isPlayer = p != null ? true : false;
			Boolean isGod = sender.hasPermission(Info.PERM_ADMIN);
			GameController g = ((GlacialServer)pl).getGameController();
			
			if(args.length > 0)
			{
				String sub = args[0];
				
				if(isPlayer)
				{
					if(isGod)
					{
						if(sub.equalsIgnoreCase("new"))
						{
							if(args.length > 1)
							{
								String name = subWord(args, 1);
								World world = p.getWorld();
								Map map = new Map((GlacialServer) pl, world, name);
								
								if(g.getMap(map.getName()) == null)
								{
									g.getMaps().add(map);
									set(p, g.getMap(map.getName()));
									s(sender, "Created and Selected Map: " + map.getName());
								}
								
								else
								{
									f(sender, "Map: " + map.getName() + " Already exists.");
								}
							}
							
							else
							{
								f(sender, "/g new <multi word name>");
							}
						}
						
						else if(sub.equalsIgnoreCase("select") || sub.equalsIgnoreCase("sel"))
						{
							if(args.length > 1)
							{
								String name = subWord(args, 1);
								Map map = g.findMap(name);
								
								if(map != null)
								{
									set(p, map);
									s(p, "Selected " + map.getName());
								}
								
								else
								{
									f(sender, "That is not a map. Use /g list");
								}
							}
							
							else if(args.length == 1)
							{
								Map map = g.getMap(p.getLocation());
								
								if(map != null)
								{
									set(p, map);
									s(p, "Selected " + map.getName());
								}
								
								else
								{
									f(sender, "You are not in a map, Supply a name.");
								}
							}
						}
						
						else if(sub.equalsIgnoreCase("unselect") || sub.equalsIgnoreCase("uns"))
						{
							clear(p);
							s(p, "Unselected Map");
						}
						
						else if(sub.equalsIgnoreCase("add") || sub.equalsIgnoreCase("+"))
						{
							if(has(p))
							{
								get(p).addRegion(p);
							}
							
							else
							{
								f(p, "No Map Selected.");
							}
						}
					}
					
					else
					{
						f(sender, "You are not authorized to do that.");
					}
				}
				
				else
				{
					f(sender, "Consoles cannot create maps.");
				}
			}
			
			else
			{
				//TODO Help Page
			}
		}
		
		return false;
	}
}
