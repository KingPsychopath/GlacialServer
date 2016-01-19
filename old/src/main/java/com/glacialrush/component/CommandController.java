package com.glacialrush.component;

import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import com.glacialrush.GlacialServer;
import com.glacialrush.Info;
import com.glacialrush.api.component.Controller;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.object.GMap;
import com.glacialrush.api.thread.GlacialTask;
import com.glacialrush.api.thread.ThreadState;
import com.glacialrush.composite.Faction;
import com.glacialrush.composite.Hunk;
import com.glacialrush.composite.Job;
import com.glacialrush.composite.Map;
import com.glacialrush.composite.Region;
import com.glacialrush.game.event.ExperienceType;
import com.glacialrush.xapi.Cuboid;
import com.glacialrush.xapi.Cuboid.CuboidDirection;
import com.glacialrush.xapi.Gui;
import com.glacialrush.xapi.Gui.Pane;
import com.glacialrush.xapi.Gui.Pane.Element;
import net.md_5.bungee.api.ChatColor;

public class CommandController extends Controller implements CommandExecutor
{
	private GList<Player> gods;
	private GMap<Player, Map> selections;
	
	public CommandController(final GlacialServer pl)
	{
		super(pl);
		selections = new GMap<Player, Map>();
		gods = new GList<Player>();
		
		pl.scheduleSyncRepeatingTask(0, 20, new Runnable()
		{
			@Override
			public void run()
			{
				for(Player i : gods)
				{
					i.setFlySpeed(1f);
					i.setWalkSpeed(1f);
					i.setAllowFlight(true);
					i.setGameMode(GameMode.SURVIVAL);
					i.setMaxHealth(80);
					i.setFoodLevel(20);
					i.setHealth(i.getMaxHealth());
					i.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 5, 1));
					i.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 2000, 100));
					i.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 2000, 100));
					i.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 2000, 100));
					i.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2000, 100));
					i.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 2000, 100));
					i.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2000, 100));
					i.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2000, 100));
					i.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2000, 100));
				}
			}
		});
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
	
	public void god(Player p)
	{
		if(gods.contains(p))
		{
			s("God Disabled");
			gods.remove(p);
		}
		
		else
		{
			s("See in the godly eyes of cyberpwn");
			gods.add(p);
		}
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
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		Player p = (sender instanceof Player) ? (Player) sender : null;
		Boolean isPlayer = p != null ? true : false;
		Boolean isGod = sender.hasPermission(Info.PERM_ADMIN);
		GameController g = ((GlacialServer) pl).getGameController();
		
		if(command.getName().equalsIgnoreCase(Info.CMD_SKILL))
		{
			if(args.length == 0)
			{
				if(isPlayer)
				{
					s(p, "Skill Points: " + ((GlacialServer) pl).getExperienceController().getSkill(p));
				}
			}
			
			else
			{
				if(args[0].equalsIgnoreCase("give"))
				{
					if(args.length == 3)
					{
						Player tg = pl.findPlayer(args[1]);
						
						if(tg != null)
						{
							try
							{
								int ix = Integer.parseInt(args[2]);
								((GlacialServer)pl).getExperienceController().giveExperience(tg, ix, ExperienceType.UNKNOWN);
							}
							
							catch(NumberFormatException e)
							{
								f(p, "Invalid number");
							}
						}
						
						else
						{
							f(p, "Unknown player");
						}
					}
					
					else
					{
						f(p, "/skill give <player> <exp>");
					}
				}
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_DEBUGGER))
		{
			if(args.length > 0)
			{
				String sub = args[0];
				
				if(isPlayer)
				{
					if(isGod)
					{
						if(sub.equalsIgnoreCase("god") || sub.equalsIgnoreCase("gg"))
						{
							god(p);
						}
						
						if(sub.equalsIgnoreCase("threads") || sub.equalsIgnoreCase("t"))
						{
							final Gui gui = new Gui(p, pl);
							final Pane pane = gui.new Pane("Threads");
							
							int c = 1;
							
							for(GlacialTask j : pl.getThreadComponent().getTasks())
							{
								Material m = null;
								String s = j.getName();
								
								if(j.getState().equals(ThreadState.RUNNING))
								{
									m = Material.SLIME_BALL;
									s = s + ChatColor.GREEN + " RUNNING";
								}
								
								else if(j.getState().equals(ThreadState.IDLE))
								{
									m = Material.MAGMA_CREAM;
									s = s + ChatColor.YELLOW + " IDLE";
								}
								
								else if(j.getState().equals(ThreadState.NEW))
								{
									m = Material.NETHER_STAR;
									s = s + ChatColor.WHITE + " NEW";
								}
								
								else if(j.getState().equals(ThreadState.FINISHED))
								{
									m = Material.REDSTONE;
									s = s + ChatColor.RED + " FINISHED";
								}
								
								Element e = pane.new Element(s, m, c);
								e.addInfo("Cycles: " + j.getCycles());
								e.addBullet("Milliseconds: " + j.getCycleTime());
								e.addBullet("MPC: " + (j.getCycleTime() / j.getCycles() == 0 ? 1 : j.getCycles()));
								e.addBullet("PID: " + j.getPid());
								
								c++;
							}
							
							pane.new Element("Close", Material.BARRIER, 0).setQuickRunnable(new Runnable()
							{
								@Override
								public void run()
								{
									gui.close();
								}
							});
							
							pane.setTitle("Threads: " + (c - 1));
							pane.setDefault();
							pane.build();
							
							gui.show();
						}
					}
				}
			}
		}
		
		else if(command.getName().equalsIgnoreCase(Info.CMD_GLACIALRUSH))
		{
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
							if(((GlacialServer) pl).getGameController().isRunning())
							{
								f(p, "Cannot start game. Already Running.");
								return true;
							}
							
							if(!((GlacialServer) pl).getGameController().getMaps().isEmpty())
							{
								((GlacialServer) pl).getGameController().start();
							}
							
							else
							{
								f(p, "Cannot start game. No Maps.");
							}
						}
						
						else if(sub.equalsIgnoreCase("stop") || sub.equalsIgnoreCase("stg"))
						{
							if(!((GlacialServer) pl).getGameController().isRunning())
							{
								f(p, "Cannot stop game. Not Running.");
								return true;
							}
							
							((GlacialServer) pl).getGameController().startCredits(Faction.random());
						}
						
						else if(sub.equalsIgnoreCase("restart") || sub.equalsIgnoreCase("rsg"))
						{
							if(!((GlacialServer) pl).getGameController().isRunning())
							{
								s(p, "Starting Game");
								((GlacialServer) pl).getGameController().stop();
							}
							
							s(p, "Restarting Game");
							((GlacialServer) pl).getGameController().restart();
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
						
						else if(sub.equalsIgnoreCase("changefaction") || sub.equalsIgnoreCase("cf"))
						{
							Faction f = null;
							
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
								
								else
								{
									f(p, "Unknown Faction: " + args[1]);
									return true;
								}
							}
							
							g.getPlayerHandler().getFactions().put(p, f);
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
						
						else if(sub.equalsIgnoreCase("delete") || sub.equalsIgnoreCase("deletemap"))
						{
							if(has(p))
							{
								g.getMaps().remove(get(p));
								s(p, "Map Deleted, remove it fron the filesystem to prevent it from restoring");
							}
							
							else
							{
								f(p, "No Map Selected.");
							}
						}
						
						else if(sub.equalsIgnoreCase("draw-here") || sub.equalsIgnoreCase("dh"))
						{
							Hunk h = new Hunk(pl.target(p));
							Job drawJob = new Job("Hunk [" + h.getX() + ", " + h.getZ() + "]: " + " Draw[" + p.getName() + "]", ((GlacialServer) pl).getJobController());
							
							Iterator<Block> n = new Cuboid(h.getCuboid()).flatten(p.getTargetBlock((HashSet<Byte>) null, 256).getLocation().getBlockY()).getFace(CuboidDirection.North).iterator();
							Iterator<Block> s = new Cuboid(h.getCuboid()).flatten(p.getTargetBlock((HashSet<Byte>) null, 256).getLocation().getBlockY()).getFace(CuboidDirection.South).iterator();
							Iterator<Block> e = new Cuboid(h.getCuboid()).flatten(p.getTargetBlock((HashSet<Byte>) null, 256).getLocation().getBlockY()).getFace(CuboidDirection.East).iterator();
							Iterator<Block> w = new Cuboid(h.getCuboid()).flatten(p.getTargetBlock((HashSet<Byte>) null, 256).getLocation().getBlockY()).getFace(CuboidDirection.West).iterator();
							
							while(n.hasNext())
							{
								drawJob.add(n.next().getLocation(), Material.SEA_LANTERN, p);
							}
							
							while(s.hasNext())
							{
								drawJob.add(s.next().getLocation(), Material.SEA_LANTERN, p);
							}
							
							while(e.hasNext())
							{
								drawJob.add(e.next().getLocation(), Material.SEA_LANTERN, p);
							}
							
							while(w.hasNext())
							{
								drawJob.add(w.next().getLocation(), Material.SEA_LANTERN, p);
							}
							
							((GlacialServer) pl).getJobController().addJob(drawJob);
						}
						
						else if(sub.equalsIgnoreCase("draw") || sub.equalsIgnoreCase("drw"))
						{
							if(has(p))
							{
								get(p).draw(p);
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
									s(p, "  SPW: " + r.getSpawns().size() + " " + (r.isCaptureable() ? "Capturable" : "Scenery"));
									s(p, "  FAC: " + r.getFaction().getColor() + r.getFaction().getName());
									n(p, "  STA: " + (r.isBuilding() ? ChatColor.RED + "Building " : ChatColor.GREEN + "Built ") + (r.isAccenting() ? ChatColor.RED + "Accenting" : ChatColor.GREEN + "Accented"));
								}
							}
							
							else
							{
								f(p, "No Map Selected.");
							}
						}
						
						else if(sub.equalsIgnoreCase("setname") || sub.equalsIgnoreCase("sn"))
						{
							if(has(p))
							{
								Region r = get(p).getRegion(p);
								
								if(r != null)
								{
									if(args.length > 1)
									{
										r.setName(subWord(args, 1));
										s("Set Region Name to: " + subWord(args, 1));
									}
									
									else
									{
										f(p, "/g sn <multi space name>");
									}
								}
								
								else
								{
									f(p, "Not inside of a region.");
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
									s(p, "Started Accent[" + ChatColor.BLUE + "Even" + ChatColor.GREEN + "] Task for " + m.getName());
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

	public GList<Player> getGods()
	{
		return gods;
	}

	public void setGods(GList<Player> gods)
	{
		this.gods = gods;
	}

	public GMap<Player, Map> getSelections()
	{
		return selections;
	}

	public void setSelections(GMap<Player, Map> selections)
	{
		this.selections = selections;
	}
}
