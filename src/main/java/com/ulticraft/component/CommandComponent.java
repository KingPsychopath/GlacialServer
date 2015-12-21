package com.ulticraft.component;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ulticraft.GlacialRush;
import com.ulticraft.Info;
import com.ulticraft.faction.Faction;
import com.ulticraft.map.Map;
import com.ulticraft.uapi.Component;
import com.ulticraft.uapi.UMap;
import net.md_5.bungee.api.ChatColor;

public class CommandComponent extends Component implements CommandExecutor
{
	private UMap<Player, String> mode;
	
	public CommandComponent(GlacialRush pl)
	{
		super(pl);
		
		mode = new UMap<Player, String>();
	}
	
	public void enable()
	{
		
	}
	
	public void disable()
	{
		
	}
	
	public void msg(Player p, String msg)
	{
		String tag = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "GRush";
		
		if(mode.containsKey(p))
		{
			tag = tag + ChatColor.DARK_GRAY + " - " + ChatColor.GREEN + mode.get(p);
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
	
	public void setMode(Player p, String mde)
	{
		mode.put(p, mde);
	}
	
	public String getMode(Player p)
	{
		return mode.get(p);
	}
	
	public void clearMode(Player p)
	{
		mode.remove(p);
	}
	
	public boolean hasMode(Player p)
	{
		return mode.containsKey(p);
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
						if(args[0].equals("sel") || args[0].equals("select"))
						{
							if(args.length == 2)
							{
								Map map = pl.getState().searchLoose(args[1]);
								
								if(map == null)
								{
									err(p, "Unknown map!");
								}
								
								setMode(p, map.getName());
								suc(p, "Selected " + map.getName());
							}
							
							else
							{
								err(p, "Select maps with /g sel <MAP>");
							}
						}
						
						else if(args[0].equals("ls") || args[0].equals("list"))
						{
							for(Map i : pl.getState().getMaps())
							{
								nte(p, i.getName() + ": " + ChatColor.GREEN + i.getRegions().size() + " RG");
							}
						}
						
						else if(args[0].equals("upd") || args[0].equals("update"))
						{
							if(hasMode(p))
							{
								pl.getState().search(getMode(p)).reset();
								suc(p, "Updating changes to neutral faction.");
							}
							
							else
							{
								err(p, "Cant update 'nothing' unless your apple. Use /g sel <mode>");
							}
						}
						
						else if(args[0].equals("upd-p") || args[0].equals("update-p"))
						{
							if(hasMode(p))
							{
								pl.getState().search(getMode(p)).reset(new Faction("Omni", ChatColor.DARK_PURPLE));
								suc(p, "Updating changes to Omni.");
							}
							
							else
							{
								err(p, "Cant update 'nothing' unless your apple. Use /g sel <mode>");
							}
						}
						
						else if(args[0].equals("upd-r") || args[0].equals("update-r"))
						{
							if(hasMode(p))
							{
								pl.getState().search(getMode(p)).reset(new Faction("Enigma", ChatColor.RED));
								suc(p, "Updating changes to Enigma.");
							}
							
							else
							{
								err(p, "Cant update 'nothing' unless your apple. Use /g sel <mode>");
							}
						}
						
						else if(args[0].equals("upd-y") || args[0].equals("update-y"))
						{
							if(hasMode(p))
							{
								pl.getState().search(getMode(p)).reset(new Faction("Cryptic", ChatColor.YELLOW));
								suc(p, "Updating changes to Cryptic.");
							}
							
							else
							{
								err(p, "Cant update 'nothing' unless your apple. Use /g sel <mode>");
							}
						}
						
						else if(args[0].equals("grid") || args[0].equals("draw"))
						{
							if(hasMode(p))
							{
								pl.getState().search(getMode(p)).outline(p);
								suc(p, "Drawing Map Regions at your level.");
							}
							
							else
							{
								err(p, "What the fuck do i draw? EVERYTHING? Use /g sel <mode>");
							}
						}
						
						else if(args[0].equals("uns") || args[0].equals("unselect"))
						{
							if(hasMode(p))
							{
								clearMode(p);
								suc(p, "Mode Unselected");
							}
							
							else
							{
								err(p, "What the fuck do i unselect? Use /g sel <mode>");
							}
						}
						
						else if(args[0].equals("new"))
						{
							if(hasMode(p))
							{
								err(p, "You are in a mode. Use /g uns");
								return true;
							}
							
							if(args.length == 4)
							{
								String name = args[1].replace('-', ' ').replace('_', ' ');
								Integer x = null;
								Integer z = null;
								
								if(pl.getState().search(name) != null)
								{
									err(p, "Thats already a map dumbass. Try again.");
									return true;
								}
								
								try
								{
									x = Integer.valueOf(args[2]);
								}
								
								catch(NumberFormatException e)
								{
									err(p, "'Width' typically means a number retard.");
									return true;
								}
								
								try
								{
									z = Integer.valueOf(args[3]);
								}
								
								catch(NumberFormatException e)
								{
									err(p, "'Height' typically means a number retard.");
									return true;
								}
								
								Map map = new Map(pl, name, p.getWorld());
								map.addRegions(p.getLocation().getChunk(), x, z);
								
								pl.getState().addMap(map);
								
								setMode(p, map.getName());
								suc(p, "Created Map! Autoselected it for ya too!");
							}
							
							else
							{
								err(p, "Create a map with /g new <name> <width> <height>");
							}
						}
					}
				}
			}
		}
		
		return false;
	}
}
