package com.ulticraft.component;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ulticraft.GlacialServer;
import com.ulticraft.Info;
import com.ulticraft.composite.Hunk;
import com.ulticraft.composite.Map;
import com.ulticraft.uapi.Component;
import com.ulticraft.uapi.UMap;
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
						if(args[0].equals("select") || args[0].equals("sel"))
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
						
						if(args[0].equals("unselect") || args[0].equals("uns"))
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
						
						if(args[0].equals("new") || args[0].equals("newmap"))
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
						
						if(args[0].equals("list") || args[0].equals("ls"))
						{
							for(Map i : pl.getGame().getMaps())
							{
								msg(p, ChatColor.AQUA + i.getName() + ": " + i.getRegions().size() + "R " + (i.isBuilding() ? ChatColor.RED + "Building..." : i.isBuilt() ? ChatColor.GREEN + "Built" : ChatColor.RED + "Unbuilt"));
							}
						}
					}
				}
			}
		}
		
		return false;
	}
}
