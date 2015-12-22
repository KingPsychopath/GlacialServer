package com.ulticraft.component;

import java.util.HashSet;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ulticraft.GlacialRush;
import com.ulticraft.Info;
import com.ulticraft.composite.MaterialData;
import com.ulticraft.faction.Faction;
import com.ulticraft.map.Map;
import com.ulticraft.map.Region;
import com.ulticraft.uapi.Component;
import com.ulticraft.uapi.UList;
import com.ulticraft.uapi.UMap;
import net.md_5.bungee.api.ChatColor;

public class CommandComponent extends Component implements CommandExecutor
{
	private UMap<Player, UList<MaterialData>> data;
	private UMap<Player, String> mode;
	private UList<Player> drawmode;
	
	public CommandComponent(final GlacialRush pl)
	{
		super(pl);
		
		mode = new UMap<Player, String>();
		drawmode = new UList<Player>();
		data = new UMap<Player, UList<MaterialData>>();
		
		pl.scheduleSyncRepeatingTask(1, 20, new Runnable()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void run()
			{
				for(Player i : drawmode)
				{
					if(!data.get(i).isEmpty())
					{
						for(MaterialData j : data.get(i))
						{
							pl.getWorldComponent().addJob(i, j.getLocation(), j.getMaterial());
						}
						
						data.get(i).clear();
					}
					
					if(hasMode(i))
					{
						Region r = pl.getState().search(getMode(i)).getRegion(i);
						
						if(r != null)
						{
							Location tl = i.getLocation().getWorld().getChunkAt(r.getCenterChunk().getX() - 1, r.getCenterChunk().getZ() + 1).getBlock(0, i.getTargetBlock((HashSet<Byte>) null, 64).getLocation().getBlockY() + 1, 15).getLocation();
							Location tr = i.getLocation().getWorld().getChunkAt(r.getCenterChunk().getX() + 1, r.getCenterChunk().getZ() + 1).getBlock(15, i.getTargetBlock((HashSet<Byte>) null, 64).getLocation().getBlockY() + 1, 15).getLocation();
							Location bl = i.getLocation().getWorld().getChunkAt(r.getCenterChunk().getX() - 1, r.getCenterChunk().getZ() - 1).getBlock(0, i.getTargetBlock((HashSet<Byte>) null, 64).getLocation().getBlockY() + 1, 0).getLocation();
							Location br = i.getLocation().getWorld().getChunkAt(r.getCenterChunk().getX() + 1, r.getCenterChunk().getZ() - 1).getBlock(15, i.getTargetBlock((HashSet<Byte>) null, 64).getLocation().getBlockY() + 1, 0).getLocation();
							
							data.get(i).add(new MaterialData(tl.getBlock().getType(), (byte) 0, tl));
							data.get(i).add(new MaterialData(tr.getBlock().getType(), (byte) 0, tr));
							data.get(i).add(new MaterialData(bl.getBlock().getType(), (byte) 0, bl));
							data.get(i).add(new MaterialData(br.getBlock().getType(), (byte) 0, br));
							
							pl.getWorldComponent().addJob(i, tl, Material.GLOWSTONE);
							pl.getWorldComponent().addJob(i, tr, Material.GLOWSTONE);
							pl.getWorldComponent().addJob(i, bl, Material.GLOWSTONE);
							pl.getWorldComponent().addJob(i, br, Material.GLOWSTONE);
						}
					}
				}
			}
		});
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
								Region r = pl.getState().search(getMode(p)).getRegion(p);
								
								if(r != null)
								{
									suc(p, "Region: " + r.getName() + " is being updated.");
									r.reset();
								}
								
								else
								{
									err(p, "What the fuck is this? Go in a region.");
								}
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
								Region r = pl.getState().search(getMode(p)).getRegion(p);
								
								if(r != null)
								{
									suc(p, "Region: " + r.getName() + " is being updated.");
									r.reset(Faction.omni());
								}
								
								else
								{
									err(p, "What the fuck is this? Go in a region.");
								}
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
								Region r = pl.getState().search(getMode(p)).getRegion(p);
								
								if(r != null)
								{
									suc(p, "Region: " + r.getName() + " is being updated.");
									r.reset(Faction.enigma());
								}
								
								else
								{
									err(p, "What the fuck is this? Go in a region.");
								}
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
								Region r = pl.getState().search(getMode(p)).getRegion(p);
								
								if(r != null)
								{
									suc(p, "Region: " + r.getName() + " is being updated.");
									r.reset(Faction.cryptic());
								}
								
								else
								{
									err(p, "What the fuck is this? Go in a region.");
								}
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
						
						else if(args[0].equals("grid-live") || args[0].equals("draw-live"))
						{
							if(hasMode(p))
							{
								if(drawmode.contains(p))
								{
									drawmode.remove(p);
									data.remove(p);
									suc(p, "Drawing Mode Set to STATIC");
								}
								
								else
								{
									drawmode.add(p);
									data.put(p, new UList<MaterialData>());
									suc(p, "Drawing Mode Set to REALTIME");
								}
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
						
						else if(args[0].equals("info") || args[0].equals("inf"))
						{
							if(hasMode(p))
							{
								suc(p, "Map: " + getMode(p));
								nte(p, "Players: " + pl.getState().search(getMode(p)).getPlayers().size());
								nte(p, "Regions: " + pl.getState().search(getMode(p)).getRegions().size());
							}
							
							else
							{
								err(p, "What the fuck do i lookup? Use /g sel <mode>");
							}
						}
						
						else if(args[0].equals("r-name") || args[0].equals("r-n"))
						{
							if(hasMode(p))
							{
								Region r = pl.getState().search(getMode(p)).getRegion(p);
								
								if(r != null)
								{
									if(args.length > 1)
									{
										String n = "";
										
										for(int i = 1; i < args.length; i++)
										{
											n = n + " " + args[i];
										}
										
										n = n.substring(1);
										
										r.setName(n);
										suc(p, "Set Region name to " + n);
									}
									
									else
									{
										err(p, "*Facepalm*    /g r-name <THE FUCKING NAME>");
									}
								}
								
								else
								{
									err(p, "What the fuck is this? Go in a region.");
								}
							}
							
							else
							{
								err(p, "What the fuck do name? Use /g sel <mode>");
							}
						}
						
						else if(args[0].equals("info-r") || args[0].equals("inf-r"))
						{
							if(hasMode(p))
							{
								Region r = pl.getState().search(getMode(p)).getRegion(p);
								
								if(r != null)
								{
									suc(p, "Region: " + r.getName());
									nte(p, "Accents: " + r.getAccents().size());
									nte(p, "Players: " + r.getPlayers().size());
									nte(p, "Captures: " + r.getCapturePoints().size());
									nte(p, "Dominant: " + r.getFaction().getColor() + r.getFaction().getName());
								}
								
								else
								{
									err(p, "What the fuck is this? Go in a region.");
								}
							}
							
							else
							{
								err(p, "What the fuck do i lookup? Use /g sel <mode>");
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
								map.reset();
								suc(p, "Created Map! Autoselected it for ya too!");
							}
							
							else
							{
								err(p, "Create a map with /g new <name> <width> <height>");
							}
						}
					}
					
					else
					{
						suc(p, "Glacial Rush v1.21");
						nte(p, "/g new <map> <x> <z>" + ChatColor.YELLOW + " - Creates a map (width x height)");
						nte(p, "/g list" + ChatColor.YELLOW + " - Lists all maps");
						nte(p, "/g sel <map>" + ChatColor.YELLOW + " - Selects a map for editing");
						nte(p, "/g uns" + ChatColor.YELLOW + " - Unselects the current map");
						nte(p, "/g upd" + ChatColor.YELLOW + " - Updates the map (upd-[r/p/y] also)");
						nte(p, "/g inf" + ChatColor.YELLOW + " - Displays info on selected map [inf-r > region]");
						nte(p, "/g r-name <name>" + ChatColor.YELLOW + " - Name the region your in");
						
					}
				}
			}
		}
		
		return false;
	}
}
