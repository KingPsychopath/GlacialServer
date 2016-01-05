package com.ulticraft.component;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ulticraft.GlacialServer;
import com.ulticraft.Info;
import com.ulticraft.composite.Faction;
import com.ulticraft.composite.Hunk;
import com.ulticraft.composite.Map;
import com.ulticraft.composite.Region;
import com.ulticraft.xapi.Component;
import com.ulticraft.xapi.Teleporter;
import com.ulticraft.xapi.UMap;
import net.md_5.bungee.api.ChatColor;

public class CommandComponent extends Component implements CommandExecutor
{
	private UMap<Player, Map> selection;
	
	public CommandComponent(final GlacialServer pl)
	{
		super(pl);
		
		selection = new UMap<Player, Map>();
	}
	
	public void enable()
	{
	
	}
	
	public void disable()
	{
	
	}
	
	public void setSelection(Player p, Map m)
	{
		selection.put(p, m);
	}
	
	public Map getSelection(Player p)
	{
		return selection.get(p);
	}
	
	public boolean hasSelection(Player p)
	{
		return selection.containsKey(p);
	}
	
	public void clearSelection(Player p)
	{
		selection.remove(p);
	}
	
	public void msg(Player p, String msg)
	{
		String tag = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "GRush";
		
		if(hasSelection(p))
		{
			tag = tag + ChatColor.GREEN + " - " + getSelection(p).getName();
		}
		
		p.sendMessage(tag + ChatColor.DARK_GRAY + "]" + ChatColor.YELLOW + ": " + ChatColor.WHITE + msg);
	}
	
	public void err(Player p, String msg)
	{
		msg(p, ChatColor.RED + msg);
	}
	
	public void wrn(Player p, String msg)
	{
		msg(p, ChatColor.GOLD + msg);
	}
	
	public void suc(Player p, String msg)
	{
		msg(p, ChatColor.GREEN + msg);
	}
	
	public void nte(Player p, String msg)
	{
		msg(p, ChatColor.AQUA + msg);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player p = null;
		boolean isPlayer = false;
		
		if(sender instanceof Player)
		{
			p = (Player) sender;
			isPlayer = true;
		}
		
		if(cmd.getName().equalsIgnoreCase(Info.COMMAND_GLACIAL_RUSH))
		{
			if(sender.hasPermission(Info.PERMISSION_GOD))
			{
				if(isPlayer)
				{
					if(args.length > 0)
					{
						if(args[0].equalsIgnoreCase("build") || args[0].equalsIgnoreCase("bd"))
						{
							if(hasSelection(p))
							{
								getSelection(p).build();
								suc(p, "Build Process Started for " + getSelection(p).getName());
							}
							
							else
							{
								err(p, "No map selected. Use /g sel <map>");
							}
						}
						
						else if(args[0].equalsIgnoreCase("setname") || args[0].equalsIgnoreCase("name"))
						{
							if(args.length < 2)
							{
								err(p, "Supply a name...");
								return true;
							}
							
							if(hasSelection(p))
							{
								if(getSelection(p).getRegions().isEmpty())
								{
									err(p, "There are no regions for this map");
									return true;
								}
								
								String name = "";
								
								for(int i = 1; i < args.length; i++)
								{
									name = name + " " + args[i];
								}
								
								name = name.substring(1);
								Map m = getSelection(p);
								
								for(Region i : m.getRegions())
								{
									if(i.contains(p))
									{
										i.setName(name);
										suc(p, "Region named: " + name);
										return true;
									}
								}
								
								err(p, "Stand in a region to set the name");
							}
							
							else
							{
								err(p, "No map selected. Use /g sel <map>");
							}
						}
						
						else if(args[0].equalsIgnoreCase("setspawn") || args[0].equalsIgnoreCase("settp"))
						{
							if(hasSelection(p))
							{
								if(getSelection(p).getRegions().isEmpty())
								{
									err(p, "There are no regions for this map");
									return true;
								}
								
								for(Region i : getSelection(p).getRegions())
								{
									if(i.contains(p))
									{
										i.setSpawn(p.getLocation());
										p.getWorld().strikeLightningEffect(p.getLocation());
										suc(p, "Region " + i.getName() + "'s spawn updated");
										return true;
									}
								}
								
								err(p, "You need to be inside of a region to set it's spawn");
							}
							
							else
							{
								err(p, "No map selected. Use /g sel <map>");
							}
						}
						
						else if(args[0].equalsIgnoreCase("spawn") || args[0].equalsIgnoreCase("tp"))
						{
							if(hasSelection(p))
							{
								Map m = getSelection(p);
								
								if(!m.getRegions().isEmpty())
								{
									boolean d = Teleporter.safeTeleport(p, m.getRegions().get(0).getSpawn());
									
									if(!d)
									{
										err(p, "No safe place to put ya >> " + m.getRegions().get(0).getSpawn());
									}
								}
							}
							
							else
							{
								err(p, "No map selected. Use /g sel <map>");
							}
						}
						
						else if(args[0].equalsIgnoreCase("ready") || args[0].equalsIgnoreCase("rdy"))
						{
							if(hasSelection(p))
							{
								Map m = getSelection(p);
								
								if(m.getReady())
								{
									err(p, "Map is already ready!");
									return true;
								}
								
								boolean mrd = m.ready(p);
								
								if(mrd)
								{
									suc(p, "Map set to ready, use /g urdy  to edit");
								}
								
								else
								{
									err(p, "---------------------------");
									err(p, "Please fix the above errors");
								}
							}
							
							else
							{
								err(p, "No map selected. Use /g sel <map>");
							}
						}
						
						else if(args[0].equalsIgnoreCase("unredy") || args[0].equalsIgnoreCase("urdy"))
						{
							if(hasSelection(p))
							{
								Map m = getSelection(p);
								
								if(!m.getReady())
								{
									err(p, "Map isn't ready!");
									return true;
								}
								
								m.setReady(false);
								
								suc(p, "Unready set, go ahead and edit");
							}
							
							else
							{
								err(p, "No map selected. Use /g sel <map>");
							}
						}
						
						else if(args[0].equalsIgnoreCase("accent") || args[0].equalsIgnoreCase("acc"))
						{
							if(hasSelection(p))
							{
								getSelection(p).accent(Faction.neutral());
								suc(p, "Accenting: Neutral");
							}
							
							else
							{
								err(p, "No map selected. Use /g sel <map>");
							}
						}
						
						else if(args[0].equalsIgnoreCase("forcebuild") || args[0].equalsIgnoreCase("build-f"))
						{
							if(hasSelection(p))
							{
								getSelection(p).rebuild();
							}
							
							else
							{
								err(p, "No map selected. Use /g sel <map>");
							}
						}
						
						else if(args[0].equalsIgnoreCase("draw") || args[0].equalsIgnoreCase("show"))
						{
							if(hasSelection(p))
							{
								getSelection(p).draw(p);
							}
							
							else
							{
								err(p, "No map selected. Use /g sel <map>");
							}
						}
						
						else if(args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("inf"))
						{
							if(hasSelection(p))
							{
								Map m = getSelection(p);
								
								for(Region i : m.getRegions())
								{
									if(i.contains(p))
									{
										nte(p, "Map: " + m.getName());
										nte(p, "Region Count: " + m.getRegions().size());
										suc(p, "Current Region: " + i.getName());
										suc(p, "  CAPP: " + i.getCaptures().size() + " ACCS: " + i.getAccents().size());
										return true;
									}
								}
								
								nte(p, "Map: " + m.getName());
								nte(p, "Region Count: " + m.getRegions().size());
							}
							
							else
							{
								err(p, "No map selected. Use /g sel <map>");
							}
						}
						
						else if(args[0].equalsIgnoreCase("accent-cryptic") || args[0].equalsIgnoreCase("acc-c"))
						{
							if(hasSelection(p))
							{
								getSelection(p).accent(Faction.cryptic());
								suc(p, "Accenting: Cryptic");
							}
							
							else
							{
								err(p, "No map selected. Use /g sel <map>");
							}
						}
						
						else if(args[0].equalsIgnoreCase("accent-omni") || args[0].equalsIgnoreCase("acc-o"))
						{
							if(hasSelection(p))
							{
								getSelection(p).accent(Faction.omni());
								suc(p, "Accenting: Omni");
							}
							
							else
							{
								err(p, "No map selected. Use /g sel <map>");
							}
						}
						
						else if(args[0].equalsIgnoreCase("accent-enigma") || args[0].equalsIgnoreCase("acc-e"))
						{
							if(hasSelection(p))
							{
								getSelection(p).accent(Faction.enigma());
								suc(p, "Accenting: Enigma");
							}
							
							else
							{
								err(p, "No map selected. Use /g sel <map>");
							}
						}
						
						else if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("+"))
						{
							if(hasSelection(p))
							{
								boolean added = getSelection(p).addRegionNear(p);
								
								if(added)
								{
									p.playSound(p.getLocation(), Sound.SHOOT_ARROW, 1f, 1.8f);
								}
								
								else
								{
									p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1f, 0.3f);
								}
							}
							
							else
							{
								err(p, "No map selected. Use /g sel <map>");
							}
						}
						
						else if(args[0].equalsIgnoreCase("select") || args[0].equalsIgnoreCase("sel"))
						{
							if(args.length == 1)
							{
								Map map = pl.getGame().getMap(new Hunk(p.getLocation()));
								
								if(map != null)
								{
									setSelection(p, map);
									suc(p, "Selected Map: " + ChatColor.AQUA + map.getName());
								}
								
								else
								{
									err(p, "You aren't inside of a map. Try using the name");
								}
							}
							
							else
							{
								String name = "";
								
								for(int i = 1; i < args.length; i++)
								{
									name = name + " " + args[i];
								}
								
								name = name.substring(1);
								Map map = pl.getGame().findMap(name);
								
								if(map != null)
								{
									setSelection(p, map);
									suc(p, "Selected Map: " + ChatColor.AQUA + map.getName());
								}
								
								else
								{
									err(p, "You aren't inside of a map. Try using the real name");
								}
							}
						}
						
						else if(args[0].equalsIgnoreCase("unselect") || args[0].equalsIgnoreCase("uns"))
						{
							if(hasSelection(p))
							{
								String mmp = getSelection(p).getName();
								clearSelection(p);
								suc(p, mmp + " Unselected");
							}
							
							else
							{
								err(p, "Try actually having something selected first.");
							}
						}
						
						else if(args[0].equalsIgnoreCase("new") || args[0].equalsIgnoreCase("newmap"))
						{
							if(args.length > 1)
							{
								String name = "";
								
								for(int i = 1; i < args.length; i++)
								{
									name = name + " " + args[i];
								}
								
								name = name.substring(1);
								
								Map map = new Map(pl, name, p.getWorld());
								
								if(pl.getGame().getMap(name) == null)
								{
									pl.getGame().getMaps().add(map);
									setSelection(p, map);
									suc(p, "Created Map: " + map.getName() + " and selected it");
								}
								
								else
								{
									err(p, "That's already a map dumbass.");
								}
							}
						}
						
						else if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("ls"))
						{
							for(Map i : pl.getGame().getMaps())
							{
								msg(p, ChatColor.AQUA + i.getName() + ": " + i.getRegions().size() + "R " + (i.isBuilding() ? ChatColor.RED + "Building..." : i.isBuilt() ? ChatColor.GREEN + "Built" : ChatColor.RED + "Unbuilt"));
							}
						}
						
						else
						{
						
						}
					}
				}
			}
		}
		
		return false;
	}
}
