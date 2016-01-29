package com.glacialrush.server;

import java.util.HashSet;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.glacialrush.api.GlacialPlugin;
import com.glacialrush.api.component.Controller;
import com.glacialrush.api.dispatch.Title;
import com.glacialrush.api.game.Game;
import com.glacialrush.api.game.GameType;
import com.glacialrush.api.game.RegionedGame;
import com.glacialrush.api.game.object.Faction;
import com.glacialrush.api.map.Chunklet;
import com.glacialrush.api.map.Map;
import com.glacialrush.api.map.Region;
import com.glacialrush.api.map.RegionType;
import com.glacialrush.api.map.region.Edge;
import com.glacialrush.api.map.region.LinkedRegion;
import com.glacialrush.api.map.region.Scenery;
import com.glacialrush.api.map.region.Territory;
import com.glacialrush.api.map.region.Village;
import com.glacialrush.api.object.GBiset;
import com.glacialrush.api.object.GMap;
import net.md_5.bungee.api.ChatColor;

public class CommandController extends Controller implements CommandExecutor
{
	private GlacialServer gs;
	private GMap<Player, GBiset<Map, Region>> selection;
	private GMap<Player, Integer> brushers;
	private GMap<Player, Player> conversations;
	
	public CommandController(GlacialPlugin pl)
	{
		super(pl);
		
		gs = (GlacialServer) pl;
		selection = new GMap<Player, GBiset<Map, Region>>();
		brushers = new GMap<Player, Integer>();
		conversations = new GMap<Player, Player>();
		
		pl.scheduleSyncRepeatingTask(0, 1, new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : selection.keySet())
				{
					gs.getGameController().join(gs.getGameController().getBuildGame(), i);
					gs.getGameController().getBuildGame().updateSelection(i, get(i));
				}
			}
		});
	}
	
	public void set(Player p, Map map)
	{
		selection.put(p, new GBiset<Map, Region>(map, null));
		gs.getGameController().join(gs.getGameController().getBuildGame(), p);
	}
	
	public void set(Player p, Region region)
	{
		selection.put(p, new GBiset<Map, Region>(region.getMap(), region));
		gs.getGameController().join(gs.getGameController().getBuildGame(), p);
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
			
			if(hasMap(p))
			{
				for(Player i : brushers.keySet())
				{
					if(hasMap(i))
					{
						Map m = get(i).getA();
						
						if(m.equals(get(p).getA()))
						{
							return;
						}
					}
				}
				
				get(p).getA().undraw();
			}
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
			
			if(hasMap(p))
			{
				get(p).getA().draw(pl.target(p).getBlockY());
			}
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
		gs.getGameController().leave(gs.getGameController().getBuildGame(), p);
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
		Player p;
		
		if(sender instanceof Player)
		{
			p = (Player) sender;
			
			for(Player i : selection.keySet())
			{
				Title l = new Title();
				String f = "";
				
				if(hasMap(p))
				{
					f = f + ChatColor.GREEN + get(p).getA().getName();
				}
				
				if(hasRegion(p))
				{
					f = f + ChatColor.YELLOW + " > " + get(p).getB().getName();
				}
				
				l.setSubSubTitle(ChatColor.YELLOW + i.getName() + ": " + f + " " + ChatColor.WHITE + msg);
				l.send(i);
			}
		}
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
							
							Map map = new Map(name, gs.getGameController().getBuildGame(), p.getWorld());
							
							gs.getGameController().getMaps().add(map);
							set(p, map);
							
							s(p, "Created and Selected " + ChatColor.AQUA + map.getName());
						}
						
						else
						{
							f(p, "/map new <name with spaces>");
						}
					}
					
					if(sub.equalsIgnoreCase("check") || sub.equalsIgnoreCase("chk"))
					{
						if(!hasMap(p))
						{
							f(p, "No Map selected");
						}
						
						else
						{
							Map map = get(p).getA();
							
							s(p, "Checking " + ChatColor.AQUA + map.getName() + " / ALL_REGIONS");
							map.check(p);
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
					
					if(sub.equalsIgnoreCase("draw") || sub.equalsIgnoreCase("dr"))
					{
						if(hasMap(p))
						{
							get(p).getA().draw(pl.target(p).getBlockY());
							s(p, "Drawing " + get(p).getA().getName());
						}
						
						else
						{
							f(p, "No Map Selection");
						}
					}
					
					if(sub.equalsIgnoreCase("undraw") || sub.equalsIgnoreCase("udr"))
					{
						if(hasMap(p))
						{
							get(p).getA().undraw();
							s(p, "Un-Drawing " + get(p).getA().getName());
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
					
					if(sub.equalsIgnoreCase("accent-e") || sub.equalsIgnoreCase("ae"))
					{
						if(hasMap(p))
						{
							Map map = get(p).getA();
							
							if(map.getMdm().getDrawn())
							{
								f(p, "Map is drawn. Please Undraw it to build");
								return true;
							}
							
							map.accentEvenley();
							
							s(p, "Started Accent-e Task");
						}
						
						else
						{
							f(p, "No Map Selection");
						}
					}
					
					if(sub.equalsIgnoreCase("lock") || sub.equalsIgnoreCase("lk"))
					{
						if(hasMap(p))
						{
							Map map = get(p).getA();
							
							if(map.isLocked())
							{
								f(p, "Map is already locked");
								return true;
							}
							
							map.safeLock(p);
							
							s(p, "Locking...");
						}
						
						else
						{
							f(p, "No Map Selection");
						}
					}
					
					if(sub.equalsIgnoreCase("delete"))
					{
						if(hasMap(p))
						{
							Map map = get(p).getA();
							
							if(map.isLocked())
							{
								f(p, "Cannot delete. Map is locked");
							}
							
							else
							{
								for(Game g : gs.getGameController().getGames())
								{
									if(g.getType().equals(GameType.REGIONED))
									{
										RegionedGame rg = (RegionedGame) g;
										if(rg.getMap().equals(map))
										{
											f(p, "Cannot delete. Map is being used");
											return true;
										}
									}
								}
								
								gs.getGameController().getMaps().remove(map);
								
								f(p, "Deleting Map...");
								f(p, "Reference Removed. To fully remove, delete the file");
							}
						}
					}
					
					if(sub.equalsIgnoreCase("unlock") || sub.equalsIgnoreCase("ulk"))
					{
						if(hasMap(p))
						{
							Map map = get(p).getA();
							
							if(!map.isLocked())
							{
								f(p, "Map is not locked");
								return true;
							}
							
							map.unlock();
							
							s(p, "Map Unlocked");
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
							
							if(map.getMdm().getDrawn())
							{
								f(p, "Map is drawn. Please Undraw it to build");
								return true;
							}
							
							map.build();
							
							s(p, "Started Build Task");
						}
						
						else
						{
							f(p, "No Map Selection");
						}
					}
					
					if(sub.equalsIgnoreCase("tp") || sub.equalsIgnoreCase("goto"))
					{
						if(hasMap(p))
						{
							Map map = get(p).getA();
							
							for(LinkedRegion i : map.getLinkedRegions())
							{
								if(!i.getSpawns().isEmpty())
								{
									i.spawn(p);
									return true;
								}
							}
							
							f(p, "No spawns avalible, no teleport.");
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
								
								if(map.getMdm().getDrawn())
								{
									f(p, "Map is drawn. Please Undraw it to accent");
									return true;
								}
								
								map.accent(f.getDyeColor());
								
								s(p, "Accenting to " + f.getName());
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
					
					if(sub.equalsIgnoreCase("delete") || sub.equalsIgnoreCase("del"))
					{
						if(hasRegion(p))
						{
							Region r = get(p).getB();
							
							set(p, r.getMap());
							r.getMap().getRegions().remove(r);
							r.getChunklets().clear();
							
							s(p, "Deleted.");
						}
						
						else
						{
							f(p, "No region selected.");
						}
					}
					
					if(sub.equalsIgnoreCase("tp") || sub.equalsIgnoreCase("goto"))
					{
						if(hasRegion(p))
						{
							Region r = get(p).getB();
							
							if(r.getType().equals(RegionType.TERRITORY) || r.getType().equals(RegionType.VILLAGE))
							{
								if(!((LinkedRegion) r).getSpawns().isEmpty())
								{
									((LinkedRegion) r).spawn(p);
								}
								
								else
								{
									f(p, "No spawns avaliblem no teleport. Try /map tp");
								}
							}
							
							f(p, "No spawns avalible, no teleport.");
						}
						
						else
						{
							f(p, "No region selected.");
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
							
							for(Region r : map.getRegions())
							{
								if(r.getName().equals(name))
								{
									f(p, "That is already a region");
									return true;
								}
							}
							
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
							s(p, ChatColor.YELLOW + "Accents: " + ChatColor.GOLD + region.getAccents().size());
							
							String borders = "";
							
							if(region.getBorders().isEmpty())
							{
								borders = "None";
							}
							
							else
							{
								for(Region i : region.getBorders())
								{
									borders = borders + ", " + i.getName();
								}
								
								borders = borders.substring(2);
							}
							
							s(p, ChatColor.YELLOW + "Borders: " + ChatColor.GOLD + borders);
							
							if(region.getType().equals(RegionType.TERRITORY) || region.getType().equals(RegionType.VILLAGE))
							{
								s(p, ChatColor.YELLOW + "Spawns: " + ChatColor.GOLD + ((LinkedRegion) region).getSpawns().size());
							}
							
							s(p, "--------------------------");
						}
						
						else
						{
							f(p, "No Region Selection");
						}
					}
					
					if(sub.equalsIgnoreCase("check") || sub.equalsIgnoreCase("chk"))
					{
						if(!hasMap(p))
						{
							f(p, "No Map selected");
						}
						
						if(hasRegion(p))
						{
							Map map = get(p).getA();
							Region region = get(p).getB();
							
							s(p, "Checking " + ChatColor.AQUA + map.getName() + " / " + region.getName());
							region.check(p);
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
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_GAME))
		{
			if(isPlayer && isAdmin)
			{
				if(len > 0)
				{
					if(sub.equalsIgnoreCase("new") || sub.equalsIgnoreCase("create"))
					{
						if(len > 1)
						{
							String map = subWord(args, 1);
							
							for(Map i : gs.getGameController().getMaps())
							{
								if(i.getName().equalsIgnoreCase(map))
								{
									if(gs.getGameController().getGames().contains(i))
									{
										f(p, "Game is already running!");
									}
									
									else
									{
										RegionedGame rg = new RegionedGame(gs.getGameController(), i);
										gs.getGameController().addGame(rg);
										s(p, "Game Started!");
									}
									
									return true;
								}
								
								if(i.getName().toLowerCase().contains(map.toLowerCase()))
								{
									if(gs.getGameController().getGames().contains(i))
									{
										f(p, "Game is already running!");
									}
									
									else
									{
										RegionedGame rg = new RegionedGame(gs.getGameController(), i);
										gs.getGameController().addGame(rg);
										s(p, "Game Started!");
									}
									
									return true;
								}
							}
						}
						
						else
						{
						
						}
					}
				}
			}
			
			if(isPlayer)
			{
				if(len > 0)
				{
					if(sub.equalsIgnoreCase("list") || sub.equalsIgnoreCase("l"))
					{
						s(p, "--------------------------");
						
						for(Game g : gs.getGameController().getGames())
						{
							if(g.getType().equals(GameType.REGIONED))
							{
								RegionedGame rg = (RegionedGame) g;
								s(p, "Map: " + rg.getMap().getName() + " " + ChatColor.AQUA + rg.players().size() + " Playing");
							}
						}
						
						s(p, "--------------------------");
					}
					
					if(sub.equalsIgnoreCase("join") || sub.equalsIgnoreCase("j"))
					{
						if(len > 1)
						{
							String name = subWord(args, 1);
							
							for(Game g : gs.getGameController().getGames())
							{
								if(g.getType().equals(GameType.REGIONED))
								{
									RegionedGame rg = (RegionedGame) g;
									
									if(rg.getMap().getName().equalsIgnoreCase(name))
									{
										s(p, "Joining " + rg.getMap().getName() + " " + ChatColor.AQUA + rg.players().size() + " Playing");
										gs.getGameController().join(rg, p);
										return true;
									}
									
									if(rg.getMap().getName().toLowerCase().contains(name.toLowerCase()))
									{
										s(p, "Joining " + rg.getMap().getName() + " " + ChatColor.AQUA + rg.players().size() + " Playing");
										gs.getGameController().join(rg, p);
										return true;
									}
								}
							}
							
							f(p, "Unknown game running. Use /game list");
						}
						
						else
						{
							for(Game g : gs.getGameController().getGames())
							{
								if(g.getType().equals(GameType.REGIONED))
								{
									RegionedGame rg = (RegionedGame) g;
									s(p, "Joining " + rg.getMap().getName() + " " + ChatColor.AQUA + rg.players().size() + " Playing");
									gs.getGameController().join(rg, p);
									return true;
								}
							}
						}
					}
				}
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_GAMEMODEC))
		{
			if(isPlayer && isAdmin)
			{
				p.setGameMode(GameMode.CREATIVE);
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_GAMEMODEA))
		{
			if(isPlayer && isAdmin)
			{
				p.setGameMode(GameMode.ADVENTURE);
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_GAMEMODES))
		{
			if(isPlayer && isAdmin)
			{
				p.setGameMode(GameMode.SURVIVAL);
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_REPLY))
		{
			if(isPlayer)
			{
				if(len > 0)
				{
					if(conversations.containsKey(p))
					{
						String msg = subWord(args, 0);
						Player px = conversations.get(p);
						px.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + p.getName() + ChatColor.YELLOW + " > " + ChatColor.GREEN + "You" + ChatColor.DARK_GRAY + "]: " + ChatColor.AQUA + msg);
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "You" + ChatColor.YELLOW + " > " + ChatColor.AQUA + px.getName() + ChatColor.DARK_GRAY + "]: " + ChatColor.GREEN + msg);
						conversations.put(p, px);
						conversations.put(px, p);
					}
					
					else
					{
						f(p, "You are not conversing with anyone. Use /msg");
					}
				}
				
				else
				{
					f(p, "/reply <message>");
				}
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_LEAVE))
		{
			if(isPlayer)
			{
				Game game = gs.getGameController().getGame(p);
				
				if(game == null)
				{
					f(p, "You are not in a game!");
				}
				
				else
				{
					if(game.getType().equals(GameType.REGIONED))
					{
						((RegionedGame) game).stopDeploying(p);
						((RegionedGame) game).leave(p);
					}
				}
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_DAY))
		{
			if(isPlayer && isAdmin)
			{
				p.getWorld().setTime(1000);
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_NIGHT))
		{
			if(isPlayer && isAdmin)
			{
				p.getWorld().setTime(13000);
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_SUN))
		{
			if(isPlayer)
			{
				p.setPlayerWeather(WeatherType.CLEAR);
				s("Cleared the skies.");
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_RAIN))
		{
			if(isPlayer)
			{
				p.setPlayerWeather(WeatherType.DOWNFALL);
				s("Downfall Begins!");
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_MESSAGE))
		{
			if(isPlayer)
			{
				if(len > 1)
				{
					String name = args[0];
					Player px = pl.findPlayer(name);
					
					if(px != null)
					{
						String msg = subWord(args, 2);
						px.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + p.getName() + ChatColor.YELLOW + " > " + ChatColor.GREEN + "You" + ChatColor.DARK_GRAY + "]: " + ChatColor.AQUA + msg);
						p.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "You" + ChatColor.YELLOW + " > " + ChatColor.AQUA + px.getName() + ChatColor.DARK_GRAY + "]: " + ChatColor.GREEN + msg);
						conversations.put(p, px);
						conversations.put(px, p);
					}
					
					else
					{
						f(p, "Unknown Player");
					}
				}
				
				else
				{
					f(p, "/msg <player> <message>");
				}
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_TELEPORT))
		{
			if(isPlayer && isAdmin)
			{
				if(len > 0)
				{
					String name = args[0];
					Player px = pl.findPlayer(name);
					
					if(px != null)
					{
						p.teleport(px);
					}
					
					else
					{
						f(p, "Huh?");
					}
				}
				
				else
				{
					f(p, "/tp <player>");
				}
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_TELEPORT_HERE))
		{
			if(isPlayer && isAdmin)
			{
				if(len > 0)
				{
					String name = args[0];
					Player px = pl.findPlayer(name);
					
					if(px != null)
					{
						px.teleport(p);
					}
					
					else
					{
						f(p, "Huh?");
					}
				}
				
				else
				{
					f(p, "/tphere <player>");
				}
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_DEVELOPER))
		{
			if(isPlayer && isAdmin)
			{
				if(len > 0)
				{
					if(sub.equalsIgnoreCase("overbose") || sub.equalsIgnoreCase("ob"))
					{
						if(pl.getDispatchListener().getPlayers().contains(p))
						{
							pl.getDispatchListener().getPlayers().remove(p);
							s(p, "Stopped Overbose");
						}
						
						else
						{
							pl.getDispatchListener().getPlayers().add(p);
							s(p, "OVERBOSE ENABLED");
						}
					}
				}
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_GAMEMODE))
		{
			if(isPlayer && isAdmin)
			{
				if(len > 0)
				{
					if(sub.equalsIgnoreCase("0"))
					{
						p.setGameMode(GameMode.SURVIVAL);
					}
					
					else if(sub.equalsIgnoreCase("1"))
					{
						p.setGameMode(GameMode.CREATIVE);
					}
					
					else if(sub.equalsIgnoreCase("2"))
					{
						p.setGameMode(GameMode.ADVENTURE);
					}
					
					else
					{
						f(p, "/gm 0/1/2");
					}
				}
				
				else
				{
					f(p, "/gm 0/1/2");
				}
			}
		}
		
		return false;
	}
	
	@EventHandler
	public void onPlayer(PlayerQuitEvent e)
	{
		Player p = e.getPlayer();
		conversations.remove(p);
		
		for(Player i : conversations.keySet())
		{
			if(conversations.get(i).equals(p))
			{
				conversations.remove(i);
				break;
			}
		}
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
		Location l = e.getPlayer().getTargetBlock((HashSet<Byte>) null, 512).getLocation();
		Region r = get(p).getB();
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR))
		{
			if(e.getItem() == null)
			{
				return;
			}
			
			if(e.getItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Add") && e.getItem().getType().equals(Material.SLIME_BALL))
			{
				r.addChunklets(new Chunklet(l).getCircle(brushers.get(p)));
			}
			
			else if(e.getItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "Subtract") && e.getItem().getType().equals(Material.MAGMA_CREAM))
			{
				r.delChunklets(new Chunklet(l).getCircle(brushers.get(p)));
			}
			
			else
			{
				return;
			}
			
			r.getMap().draw(l.getBlockY());
		}
	}
	
	@EventHandler
	public void onPing(ServerListPingEvent e)
	{
		o("PINGED: " + e.getAddress().getHostAddress().toString());
	}
}
