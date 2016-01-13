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
import com.glacialrush.composite.Faction;
import com.glacialrush.composite.Map;
import com.glacialrush.composite.Region;
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
		
		sender.sendMessage(tag);
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
						
						else if(sub.equalsIgnoreCase("start") || sub.equalsIgnoreCase("sg"))
						{
							if(((GlacialServer)pl).getGameController().isRunning())
							{
								f(p, "Cannot start game. Already Running.");
								return true;
							}
							
							if(!((GlacialServer)pl).getGameController().getMaps().isEmpty())
							{
								((GlacialServer)pl).getGameController().start();
							}
							
							else
							{
								f(p, "Cannot start game. No Maps.");
							}
						}
						
						else if(sub.equalsIgnoreCase("stop") || sub.equalsIgnoreCase("stg"))
						{
							if(!((GlacialServer)pl).getGameController().isRunning())
							{
								f(p, "Cannot stop game. Not Running.");
								return true;
							}
							
							((GlacialServer)pl).getGameController().stop();
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
						
						else if(sub.equalsIgnoreCase("lock") || sub.equalsIgnoreCase("l"))
						{
							if(has(p))
							{
								if(get(p).getLocked())
								{
									f("That map is already locked retard.");
									return true;
								}
								
								if(get(p).check(p))
								{
									get(p).setLocked(true);
									s(p, "Map Locked");
								}
								
								else
								{
									f(p, "---------------------------");
									w(p, "Please Fix the Orange ones");
									f(p, "JESUS FUCK FIX THE RED ONES");
								}
							}
							
							else
							{
								f(p, "No Map Selected.");
							}
						}
						
						else if(sub.equalsIgnoreCase("unlock") || sub.equalsIgnoreCase("u"))
						{
							if(has(p))
							{
								if(get(p).getLocked())
								{
									get(p).setLocked(false);
									s(p, "Map Unlocked");
								}
								
								else
								{
									f(p, "That map isn't locked dumbass...");
								}
							}
							
							else
							{
								f(p, "No Map Selected.");
							}
						}
						
						else if(sub.equalsIgnoreCase("info") || sub.equalsIgnoreCase("i"))
						{
							if(has(p))
							{
								Map m = get(p);
								Region r = m.getRegion(p);
								
								s(p, "Map: " + m.getName());
								n(p, "Regions: " + m.getRegions().size());
								n(p, "Status: " + (m.isBuilding() ? ChatColor.RED + "Building " : ChatColor.GREEN + "Built ") + (m.isAccenting() ? ChatColor.RED + "Accenting" : ChatColor.GREEN + "Accented"));
								
								if(r != null)
								{
									s(p, "  Current Region: " + r.getName());
									s(p, "  ACC: " + r.getAccents().size() + " CAP: " + r.getCaptures().size());
									s(p, "  FAC: " + r.getFaction().getColor() + r.getFaction().getName());
									n(p, "  STA: " + (r.isBuilding() ? ChatColor.RED + "Building " : ChatColor.GREEN + "Built ") + (r.isAccenting() ? ChatColor.RED + "Accenting" : ChatColor.GREEN + "Accented"));
								}
							}
							
							else
							{
								f(p, "No Map Selected.");
							}
						}
						
						else if(sub.equalsIgnoreCase("tp") || sub.equalsIgnoreCase("spawn"))
						{
							if(has(p))
							{
								Map m = get(p);
								
								if(!m.getRegions().isEmpty())
								{
									p.teleport(m.getRegions().get(0).getSpawn());
								}
								
								else
								{
									f(p, "No Regions Found.");
								}
							}
							
							else
							{
								f(p, "No Map Selected.");
							}
						}
						
						else if(sub.equalsIgnoreCase("build") || sub.equalsIgnoreCase("b"))
						{
							if(has(p))
							{
								Map m = get(p);
								
								if(m.isBuilding())
								{
									f(p, "Map is still Building...");
								}
								
								else if(m.isAccenting())
								{
									f(p, "Map is still Accenting...");
								}
								
								else
								{
									s(p, "Started Build Task for " + m.getName());
									m.build();
								}
							}
							
							else
							{
								f(p, "No Map Selected.");
							}
						}
						
						else if(sub.equalsIgnoreCase("accent") || sub.equalsIgnoreCase("a"))
						{
							if(has(p))
							{
								Map m = get(p);
								Faction f = Faction.neutral();
								
								if(m.isBuilding())
								{
									f(p, "Map is still Building...");
								}
								
								else if(m.isAccenting())
								{
									f(p, "Map is still Accenting...");
								}
								
								else
								{
									if(args.length > 1)
									{
										if(Faction.omni().getName().toLowerCase().contains(args[1].toLowerCase()))
										{
											f = Faction.omni();
										}
										
										else if(Faction.cryptic().getName().toLowerCase().contains(args[1].toLowerCase()))
										{
											f = Faction.cryptic();
										}
										
										else if(Faction.enigma().getName().toLowerCase().contains(args[1].toLowerCase()))
										{
											f = Faction.enigma();
										}
										
										else if(Faction.neutral().getName().toLowerCase().contains(args[1].toLowerCase()))
										{
											f = Faction.neutral();
										}
										
										else
										{
											f(p, "Unknown Faction: " + args[1] + " Setting to neutral");
										}
									}
									
									s(p, "Started Accent[" + f.getColor() + f.getName() + ChatColor.GREEN + "] Task for " + m.getName());
									m.accent(f);
								}
							}
							
							else
							{
								f(p, "No Map Selected.");
							}
						}
						
						else if(sub.equalsIgnoreCase("accent-e") || sub.equalsIgnoreCase("ae"))
						{
							if(has(p))
							{
								Map m = get(p);
								
								if(m.isBuilding())
								{
									f(p, "Map is still Building...");
								}
								
								else if(m.isAccenting())
								{
									f(p, "Map is still Accenting...");
								}
								
								else
								{
									s(p, "Started Accent[" + ChatColor.BLUE + "Even"+ ChatColor.GREEN + "] Task for " + m.getName());
									m.accentEvenley();
								}
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
				n(sender, "Not a command");
			}
		}
		
		return false;
	}
}
