package com.glacialrush.server;

import java.io.File;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import com.glacialrush.api.GlacialPlugin;
import com.glacialrush.api.component.MapDataController;
import com.glacialrush.api.component.PlayerDataComponent;
import com.glacialrush.api.component.UIController;
import com.glacialrush.api.dispatch.notification.NotificationPreset;
import com.glacialrush.api.game.Game;
import com.glacialrush.api.game.GameController;
import com.glacialrush.api.game.GameType;
import com.glacialrush.api.game.RegionedGame;
import com.glacialrush.api.game.data.ChunkletData;
import com.glacialrush.api.game.data.MapData;
import com.glacialrush.api.game.data.PlayerData;
import com.glacialrush.api.game.data.RegionData;
import com.glacialrush.api.game.loadout.Loadout;
import com.glacialrush.api.game.object.Greek;
import com.glacialrush.api.game.object.Squad;
import com.glacialrush.api.game.object.Statistic;
import com.glacialrush.api.game.obtainable.Ability;
import com.glacialrush.api.game.obtainable.Item;
import com.glacialrush.api.game.obtainable.Obtainable;
import com.glacialrush.api.game.obtainable.ObtainableFilter;
import com.glacialrush.api.game.obtainable.Projectile;
import com.glacialrush.api.game.obtainable.Upgrade;
import com.glacialrush.api.game.obtainable.item.Weapon;
import com.glacialrush.api.game.obtainable.item.weapon.MeleeWeapon;
import com.glacialrush.api.game.obtainable.item.weapon.RangedWeapon;
import com.glacialrush.api.game.obtainable.item.weapon.type.WeaponEnclosureType;
import com.glacialrush.api.game.obtainable.projectile.ProjectileArrow;
import com.glacialrush.api.game.obtainable.type.ProjectileType;
import com.glacialrush.api.game.obtainable.type.UpgradeType;
import com.glacialrush.api.game.obtainable.upgrade.MeleeWeaponUpgrade;
import com.glacialrush.api.game.obtainable.upgrade.ProjectileUpgrade;
import com.glacialrush.api.game.obtainable.upgrade.RangedWeaponUpgrade;
import com.glacialrush.api.gui.Element;
import com.glacialrush.api.gui.ElementClickListener;
import com.glacialrush.api.gui.Pane;
import com.glacialrush.api.gui.Shortcut;
import com.glacialrush.api.gui.ShortcutLaunchListener;
import com.glacialrush.api.map.Chunklet;
import com.glacialrush.api.map.Map;
import com.glacialrush.api.map.Region;
import com.glacialrush.api.map.RegionType;
import com.glacialrush.api.map.region.Edge;
import com.glacialrush.api.map.region.Scenery;
import com.glacialrush.api.map.region.Territory;
import com.glacialrush.api.map.region.Village;
import com.glacialrush.api.object.GList;
import com.glacialrush.api.object.GMap;
import com.glacialrush.api.rank.Rank;
import com.glacialrush.api.sfx.Audio;
import net.md_5.bungee.api.ChatColor;

public class GlacialServer extends GlacialPlugin implements Listener
{
	private GameController gameController;
	private CommandController commandController;
	private MapDataController mapDataController;
	private PlayerDataComponent playerDataComponent;
	
	private Shortcut sLoadout;
	private Shortcut sSquad;
	private Shortcut sStats;
	
	private boolean uppd;
	
	public void onEnable()
	{
		super.onEnable();
		
		if(update)
		{
			uppd = true;
			return;
		}
		
		uppd = false;
		
		commandController = new CommandController(this);
		mapDataController = new MapDataController(this, new File(getDataFolder(), "maps"), "map");
		playerDataComponent = new PlayerDataComponent(this, new File(getDataFolder(), "playerdata"), "gp");
		gameController = new GameController(this, playerDataComponent);
		gameControl = gameController;
		
		super.startComponents();
		
		register(this);
		
		for(Player i : onlinePlayers())
		{
			playerDataComponent.load(i);
			if(gpd(i).getVerbose())
			{
				dispatchListener.getPlayers().add(i);
			}
		}
		
		gameController.setBlocking(true);
		
		for(File i : mapDataController.getMdc().getCache().keySet())
		{
			MapData md = mapDataController.getMdc().getCache().get(i);
			
			Map map = new Map(md.getName(), gameController.getBuildGame(), getServer().getWorld(md.getWorld()));
			map.setMapConfiguration(md.getConfig());
			map.lock();
			
			for(RegionData j : md.getRegions())
			{
				if(j.getType().equals(RegionType.TERRITORY))
				{
					Territory r = new Territory(map, j.getName(), gameController.getBuildGame());
					
					for(ChunkletData k : j.getChunklets())
					{
						Chunklet c = new Chunklet(k.getX(), k.getZ(), getServer().getWorld(md.getWorld()));
						
						r.addChunklet(c);
					}
					
					map.getRegions().add(r);
				}
				
				else if(j.getType().equals(RegionType.VILLAGE))
				{
					Village r = new Village(map, j.getName(), gameController.getBuildGame());
					
					for(ChunkletData k : j.getChunklets())
					{
						Chunklet c = new Chunklet(k.getX(), k.getZ(), getServer().getWorld(md.getWorld()));
						
						r.addChunklet(c);
					}
					
					map.getRegions().add(r);
				}
				
				else if(j.getType().equals(RegionType.EDGE))
				{
					Edge r = new Edge(map, j.getName(), gameController.getBuildGame());
					
					for(ChunkletData k : j.getChunklets())
					{
						Chunklet c = new Chunklet(k.getX(), k.getZ(), getServer().getWorld(md.getWorld()));
						
						r.addChunklet(c);
					}
					
					map.getRegions().add(r);
				}
				
				else if(j.getType().equals(RegionType.SCENERY))
				{
					Scenery r = new Scenery(map, j.getName(), gameController.getBuildGame());
					
					for(ChunkletData k : j.getChunklets())
					{
						Chunklet c = new Chunklet(k.getX(), k.getZ(), getServer().getWorld(md.getWorld()));
						
						r.addChunklet(c);
					}
					
					map.getRegions().add(r);
				}
			}
			
			gameController.getMaps().add(map);
			map.lock();
			map.fastBuild();
			s("Injected Map: " + map.getName());
		}
		
		gameController.setBlocking(false);
		
		getCommand(Info.CMD_GAME).setExecutor(commandController);
		getCommand(Info.CMD_REGION).setExecutor(commandController);
		getCommand(Info.CMD_MAP).setExecutor(commandController);
		getCommand(Info.CMD_BRUSH).setExecutor(commandController);
		getCommand(Info.CMD_TELEPORT).setExecutor(commandController);
		getCommand(Info.CMD_TELEPORT_HERE).setExecutor(commandController);
		getCommand(Info.CMD_GAMEMODE).setExecutor(commandController);
		getCommand(Info.CMD_GAMEMODEA).setExecutor(commandController);
		getCommand(Info.CMD_GAMEMODEC).setExecutor(commandController);
		getCommand(Info.CMD_GAMEMODES).setExecutor(commandController);
		getCommand(Info.CMD_MESSAGE).setExecutor(commandController);
		getCommand(Info.CMD_REPLY).setExecutor(commandController);
		getCommand(Info.CMD_LEAVE).setExecutor(commandController);
		getCommand(Info.CMD_SUN).setExecutor(commandController);
		getCommand(Info.CMD_RAIN).setExecutor(commandController);
		getCommand(Info.CMD_DAY).setExecutor(commandController);
		getCommand(Info.CMD_NIGHT).setExecutor(commandController);
		getCommand(Info.CMD_DEVELOPER).setExecutor(commandController);
		getCommand(Info.CMD_RANK).setExecutor(commandController);
		getCommand(Info.CMD_SKILL).setExecutor(commandController);
		getCommand(Info.CMD_TUTORIAL).setExecutor(commandController);
		getCommand(Info.CMD_SPECTATE).setExecutor(commandController);
		getCommand(Info.CMD_GIVEXP).setExecutor(commandController);
		getCommand(Info.CMD_GIVESKILL).setExecutor(commandController);
		getCommand(Info.CMD_GIVESHARDS).setExecutor(commandController);
		getCommand(Info.CMD_RESPAWN).setExecutor(commandController);
		getCommand(Info.CMD_SQUAD).setExecutor(commandController);
		getCommand(Info.CMD_INVITE).setExecutor(commandController);
		getCommand(Info.CMD_OBJECTIVE).setExecutor(commandController);
		getCommand(Info.CMD_POINT).setExecutor(commandController);
		getCommand(Info.CMD_HELP).setExecutor(commandController);
		
		pdc = playerDataComponent;
		
		registerShortcuts();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(final PlayerJoinEvent e)
	{
		scheduleSyncTask(1, new Runnable()
		{
			@Override
			public void run()
			{
				if(gpd(e.getPlayer()).getVerbose())
				{
					if(!dispatchListener.getPlayers().contains(e.getPlayer()))
					{
						dispatchListener.getPlayers().add(e.getPlayer());
					}
				}
				
				else
				{
					dispatchListener.getPlayers().remove(e.getPlayer());
				}
			}
		});
	}
	
	public void onDisable()
	{
		if(uppd)
		{
			return;
		}
		
		for(Player i : onlinePlayers())
		{
			playerDataComponent.save(i);
		}
		
		for(Map i : gameController.getMaps())
		{
			boolean held = false;
			
			for(File j : mapDataController.getMdc().getCache().keySet())
			{
				if(mapDataController.getMdc().getCache().get(j).getName().equals(i.getName()))
				{
					held = true;
				}
			}
			
			if(!held)
			{
				mapDataController.track(i);
			}
		}
		
		super.onDisable();
	}
	
	public PlayerData gpd(Player p)
	{
		return playerDataComponent.get(p);
	}
	
	public void registerShortcuts()
	{
		Shortcut sGames = new Shortcut(ChatColor.AQUA + "Games", Material.INK_SACK, 0, 1).setData((byte) 4).onShortcutLaunch(new ShortcutLaunchListener()
		{
			public void run()
			{
				int slot = -1;
				
				for(RegionedGame i : gameController.getRegionedGames())
				{
					slot++;
					final RegionedGame g = i;
					Element e = new Element(getPane(), ChatColor.AQUA + g.getMap().getName(), Material.SLIME_BALL, slot);
					e.addLore(ChatColor.GREEN + "" + i.getState().getPlayers().size() + " Players");
					e.addLore(g.getFactionHandler().map());
					
					if(g.contains(getPlayer()))
					{
						e.setMaterial(Material.MAGMA_CREAM);
						e.addLore(ChatColor.AQUA + "Currently Playing");
					}
					
					e.setOnLeftClickListener(new ElementClickListener()
					{
						public void run()
						{
							if(g.contains(getPlayer()))
							{
								Audio.UI_FAIL.play(getPlayer());
							}
							
							else
							{
								close();
								g.join(getPlayer());
							}
						}
					});
				}
			}
		}).setIX(0).setIY(2);
		
		sStats = new Shortcut(ChatColor.GOLD + "Stats", Material.INK_SACK, 3, 1).setData((byte) 14).onShortcutLaunch(new ShortcutLaunchListener()
		{
			public void run()
			{
				int slot = 0;
				
				GMap<String, GList<Statistic>> map = Statistic.filter();
				
				for(final String i : map.keySet())
				{
					Element e = new Element(getPane(), ChatColor.GOLD + i + " Stats", Material.BREAD, slot);
					e.addLore(ChatColor.YELLOW + "Click to view " + i + " related stats.");
					e.setOnLeftClickListener(new ElementClickListener()
					{
						public void run()
						{
							showStats(i, getPlayer());
						}
					});
					slot++;
				}
			}
		}).setIX(3).setIY(2);
		
		Shortcut sSettings = new Shortcut(ChatColor.GREEN + "Settings", Material.REDSTONE, 4, 1).onShortcutLaunch(new ShortcutLaunchListener()
		{
			public void run()
			{
				final boolean particlesOn = gpd(getPlayer()).getParticles();
				final boolean customSounds = gpd(getPlayer()).getCustomSounds();
				
				final Element particles = new Element(getPane(), "Particles " + (particlesOn ? "ON" : "MINIMAL"), particlesOn ? Material.SLIME_BALL : Material.MAGMA_CREAM, -1, 4);
				
				if(!particlesOn)
				{
					particles.setTitle(ChatColor.YELLOW + "Particles MINIMAL");
					particles.clearLore();
					particles.addLore(ChatColor.YELLOW + "Particles will be used only for some projectiles");
					particles.addLore(ChatColor.YELLOW + "This is the bare minimum for particles.");
				}
				
				else
				{
					particles.setTitle(ChatColor.GREEN + "Particles ON");
					particles.clearLore();
					particles.addLore(ChatColor.GREEN + "Particles will be used to show projectiles, ");
					particles.addLore(ChatColor.GREEN + "events, explosions, and abilities.");
				}
				
				particles.setOnLeftClickListener(new ElementClickListener()
				{
					public void run()
					{
						final boolean pparticlesOn = gpd(getPlayer()).getParticles();
						
						if(pparticlesOn)
						{
							gpd(getPlayer()).setParticles(false);
							particles.setMaterial(Material.MAGMA_CREAM);
							particles.setTitle(ChatColor.YELLOW + "Particles MINIMAL");
							particles.clearLore();
							particles.addLore(ChatColor.YELLOW + "Particles will be used only for projectiles");
							particles.addLore(ChatColor.YELLOW + "This is the bare minimum for particles.");
						}
						
						else
						{
							gpd(getPlayer()).setParticles(true);
							particles.setMaterial(Material.SLIME_BALL);
							particles.setTitle(ChatColor.GREEN + "Particles ON");
							particles.clearLore();
							particles.addLore(ChatColor.GREEN + "Particles will be used to show projectiles, ");
							particles.addLore(ChatColor.GREEN + "events, explosions, and abilities.");
						}
						
						Audio.UI_ACTION.play(getPlayer());
						
						getPane().getUi().update();
					}
				});
				
				final Element sounds = new Element(getPane(), "Custom Sounds " + (customSounds ? "ON" : "OFF"), customSounds ? Material.SLIME_BALL : Material.MAGMA_CREAM, 0, 4);
				
				if(!customSounds)
				{
					sounds.setTitle(ChatColor.YELLOW + "Custom Sounds OFF");
					sounds.clearLore();
					sounds.addLore(ChatColor.YELLOW + "Any sound that is played by the server");
					sounds.addLore(ChatColor.YELLOW + "will not be sent to you. Normal minecraft");
					sounds.addLore(ChatColor.YELLOW + "sounds however " + ChatColor.UNDERLINE + "will still play");
				}
				
				else
				{
					sounds.setTitle(ChatColor.GREEN + "Custom Sounds ON");
					sounds.clearLore();
					sounds.addLore(ChatColor.GREEN + "Custom sounds will play upon events.");
					sounds.addLore(ChatColor.GREEN + "If you do not hear anything, make sure");
					sounds.addLore(ChatColor.GREEN + "Your ambient sound is not off.");
				}
				
				sounds.setOnLeftClickListener(new ElementClickListener()
				{
					public void run()
					{
						final boolean ssoundsOn = gpd(getPlayer()).getCustomSounds();
						
						if(ssoundsOn)
						{
							gpd(getPlayer()).setCustomSounds(false);
							sounds.setMaterial(Material.MAGMA_CREAM);
							sounds.setTitle(ChatColor.YELLOW + "Custom Sounds OFF");
							sounds.clearLore();
							sounds.addLore(ChatColor.YELLOW + "Any sound that is played by the server");
							sounds.addLore(ChatColor.YELLOW + "will not be sent to you. Normal minecraft");
							sounds.addLore(ChatColor.YELLOW + "sounds however " + ChatColor.UNDERLINE + "will still play");
						}
						
						else
						{
							gpd(getPlayer()).setCustomSounds(true);
							sounds.setMaterial(Material.SLIME_BALL);
							sounds.setTitle(ChatColor.GREEN + "Custom Sounds ON");
							sounds.clearLore();
							sounds.addLore(ChatColor.GREEN + "Custom sounds will play upon events.");
							sounds.addLore(ChatColor.GREEN + "If you do not hear anything, make sure");
							sounds.addLore(ChatColor.GREEN + "Your ambient sound is not off.");
						}
						
						Audio.UI_ACTION.play(getPlayer());
						
						getPane().getUi().update();
					}
				});
				
				if(gpd(getPlayer()).getRanks().contains(Rank.OWNER))
				{
					final boolean overbose = gpd(getPlayer()).getVerbose();
					
					final Element verbose = new Element(getPane(), "Overbose " + (overbose ? "ON" : "OFF"), overbose ? Material.SLIME_BALL : Material.MAGMA_CREAM, 0, 5);
					
					if(!overbose)
					{
						verbose.setTitle(ChatColor.YELLOW + "Overbose OFF");
						verbose.clearLore();
						verbose.addLore(ChatColor.YELLOW + "You are safe from the overwhelming console");
					}
					
					else
					{
						verbose.setTitle(ChatColor.GREEN + "Overbose ON");
						verbose.clearLore();
						verbose.addLore(ChatColor.GREEN + "Grip your balls for this one.");
						verbose.addLore(ChatColor.GREEN + "I think i feel a draft coming on.");
						verbose.addLore(ChatColor.GREEN + "Better drop a stack trace on overbose");
					}
					
					verbose.setOnLeftClickListener(new ElementClickListener()
					{
						public void run()
						{
							final boolean sOverbose = gpd(getPlayer()).getVerbose();
							
							if(sOverbose)
							{
								gpd(getPlayer()).setVerbose(false);
								dispatchListener.getPlayers().remove(getPlayer());
								verbose.setMaterial(Material.MAGMA_CREAM);
								verbose.setTitle(ChatColor.YELLOW + "Overbose OFF");
								verbose.clearLore();
								verbose.addLore(ChatColor.YELLOW + "You are safe from the overwhelming console");
							}
							
							else
							{
								gpd(getPlayer()).setVerbose(true);
								dispatchListener.getPlayers().add(getPlayer());
								verbose.setMaterial(Material.SLIME_BALL);
								verbose.setTitle(ChatColor.GREEN + "Overbose ON");
								verbose.clearLore();
								verbose.addLore(ChatColor.GREEN + "Grip your balls for this one.");
								verbose.addLore(ChatColor.GREEN + "I think i feel a draft coming on.");
								verbose.addLore(ChatColor.GREEN + "Better drop a stack trace on overbose");
							}
							
							Audio.UI_ACTION.play(getPlayer());
							
							getPane().getUi().update();
						}
					});
				}
			}
		}).setIX(4).setIY(2);
		
		Shortcut sShop = new Shortcut(ChatColor.GOLD + "Shop", Material.BREAD, -2, 1).onShortcutLaunch(new ShortcutLaunchListener()
		{
			public void run()
			{
				Element w = new Element(getPane(), ChatColor.GREEN + "Weapons", Material.DIAMOND_SWORD, -2, 3);
				Element a = new Element(getPane(), ChatColor.BLUE + "Ammunition", Material.ARROW, -1, 3);
				Element u = new Element(getPane(), ChatColor.YELLOW + "Upgrades", Material.COOKIE, 0, 3);
				Element ab = new Element(getPane(), ChatColor.LIGHT_PURPLE + "Abilities", Material.SUGAR, 1, 3);
				Element bb = new Element(getPane(), ChatColor.AQUA + "BOOST!", Material.INK_SACK, 0, 4);
				Element rr = new Element(getPane(), ChatColor.AQUA + "Ranks", Material.INK_SACK, 0, 2);
				
				w.addLore(ChatColor.GREEN + "Get all weapons here.");
				a.addLore(ChatColor.BLUE + "Get all ammunition here.");
				u.addLore(ChatColor.YELLOW + "Get all upgrades here.");
				ab.addLore(ChatColor.LIGHT_PURPLE + "Get all abilities here.");
				bb.addLore(ChatColor.AQUA + "Boosts increase your xp income which earns you more skill points faster!");
				bb.addLore(ChatColor.AQUA + "Boosts only last for the game you purchase them in! Make sure its a brand new game!");
				rr.addLore(ChatColor.AQUA + "Get Powerful ranks within the rush!");
				rr.addLore(ChatColor.AQUA + "Simply use shards to get ranks.");
				bb.setData((byte) 4);
				rr.setData((byte) 4);
				
				u.setOnLeftClickListener(new ElementClickListener()
				{
					public void run()
					{
						close();
						getUi().getPanes().clear();
						Pane pane = new Pane(getUi(), "Select Upgrades");
						
						int c = 0;
						
						for(final Upgrade i : gameController.getObtainableBank().getObtainableFilter().getUpgrades())
						{
							if(!gameController.getObtainableBank().getObtainableFilter().has(getPlayer(), i))
							{
								Element e = configure(getPlayer(), pane, i, c);
								e.setOnLeftClickListener(new ElementClickListener()
								{
									public void run()
									{
										PlayerData pd = gpd(getPlayer());
										
										if(i.getCost() <= pd.getSkill())
										{
											getMarketController().buy(getPlayer(), i);
											close();
										}
										
										else
										{
											Audio.UI_FAIL.play(getPlayer());
										}
									}
								});
								
								c++;
							}
						}
						
						getUi().open(pane);
					}
				});
				
				rr.setOnLeftClickListener(new ElementClickListener()
				{
					public void run()
					{
						close();
						getUi().getPanes().clear();
						Pane pane = new Pane(getUi(), ChatColor.AQUA + "Select a Rank!");
						
						int c = 0;
						
						for(final Rank i : Rank.forPlayer(getPlayer()))
						{
							Element e = new Element(getPane(), ChatColor.AQUA + i.getName(), Material.INK_SACK, c).setData((byte) 4);
							
							e.addLore(ChatColor.AQUA + "Costs " + i.getCost() + " Shards");
							
							e.setOnLeftClickListener(new ElementClickListener()
							{
								public void run()
								{
									if(gameControl.pl().getMarketController().getShards(getPlayer()) >= i.getCost())
									{
										gameControl.pl().getMarketController().rankUp(getPlayer(), i);
									}
									
									else
									{
										Audio.UI_FAIL.play(getPlayer());
									}
								}
							});
							
							c++;
						}
						
						getUi().open(pane);
					}
				});
				
				bb.setOnLeftClickListener(new ElementClickListener()
				{
					public void run()
					{
						close();
						getUi().getPanes().clear();
						Pane pane = new Pane(getUi(), ChatColor.AQUA + "Select Boosts!");
						
						Element bba = new Element(pane, ChatColor.AQUA + "+ 50% XP", Material.INK_SACK, -1, 3).setData((byte) 4);
						bba.addLore(ChatColor.AQUA + "Costs 75 Shards!");
						bba.addLore(ChatColor.RED + "100 XP " + ChatColor.AQUA + "> " + ChatColor.GREEN + "150 XP");
						bba.addLore(ChatColor.AQUA + "Put your shards where they matter. Boost your xp income!");
						
						Element bbb = new Element(pane, ChatColor.AQUA + "+ 75% XP", Material.INK_SACK, 0, 3).setData((byte) 4);
						bbb.addLore(ChatColor.AQUA + "Costs 125 Shards!");
						bbb.addLore(ChatColor.RED + "100 XP " + ChatColor.AQUA + "> " + ChatColor.GREEN + "175 XP");
						bbb.addLore(ChatColor.AQUA + "This is almost double XP!");
						
						Element bbc = new Element(pane, ChatColor.AQUA + "+ 125% XP", Material.INK_SACK, 1, 3).setData((byte) 4);
						bbc.addLore(ChatColor.AQUA + "Costs 220 Shards!");
						bbc.addLore(ChatColor.RED + "100 XP " + ChatColor.AQUA + "> " + ChatColor.GREEN + "225 XP");
						bbc.addLore(ChatColor.AQUA + "MORE THAN DOUBLE XP! DOUBLE SKILL! BOOM!");
						
						bba.setOnLeftClickListener(new ElementClickListener()
						{
							public void run()
							{
								if(gameController.getGame(getPlayer()) == null || !gameControl.getGame(getPlayer()).getType().equals(GameType.REGIONED))
								{
									getPlayer().sendMessage(ChatColor.RED + "You must be in a game to purchase this!");
									getUi().close();
									return;
								}
								
								if(gameController.gpd(getPlayer()).getShards() < 75)
								{
									getPlayer().sendMessage(ChatColor.RED + "You need 75 shards to purchase this! You currently have " + gameController.gpd(getPlayer()).getShards() + " (/skill)");
									getUi().close();
									return;
								}
								
								RegionedGame rg = (RegionedGame) gameControl.getGame(getPlayer());
								
								if(!rg.canBoost(getPlayer()))
								{
									getPlayer().sendMessage(ChatColor.RED + "You have already boosted this game! You can do this in the next game!");
									getUi().close();
									return;
								}
								
								gameController.gpd(getPlayer()).setShards(gameController.gpd(getPlayer()).getShards() - 75);
								rg.secureBoost(getPlayer(), 0.5);
								getUi().close();
								rg.getNotificationHandler().queue(getPlayer(), NotificationPreset.BOOSTED.format(null, new Object[] {ChatColor.AQUA + "^50%"}, null));
							}
						});
						
						bbb.setOnLeftClickListener(new ElementClickListener()
						{
							public void run()
							{
								if(gameController.getGame(getPlayer()) == null || !gameControl.getGame(getPlayer()).getType().equals(GameType.REGIONED))
								{
									getPlayer().sendMessage(ChatColor.RED + "You must be in a game to purchase this!");
									getUi().close();
									return;
								}
								
								if(gameController.gpd(getPlayer()).getShards() < 125)
								{
									getPlayer().sendMessage(ChatColor.RED + "You need 125 shards to purchase this! You currently have " + gameController.gpd(getPlayer()).getShards() + " (/skill)");
									getUi().close();
									return;
								}
								
								RegionedGame rg = (RegionedGame) gameControl.getGame(getPlayer());
								
								if(!rg.canBoost(getPlayer()))
								{
									getPlayer().sendMessage(ChatColor.RED + "You have already boosted this game! You can do this in the next game!");
									getUi().close();
									return;
								}
								
								gameController.gpd(getPlayer()).setShards(gameController.gpd(getPlayer()).getShards() - 125);
								rg.secureBoost(getPlayer(), 0.75);
								getUi().close();
								rg.getNotificationHandler().queue(getPlayer(), NotificationPreset.BOOSTED.format(null, new Object[] {ChatColor.AQUA + "^75%"}, null));
							}
						});
						
						bbc.setOnLeftClickListener(new ElementClickListener()
						{
							public void run()
							{
								if(gameController.getGame(getPlayer()) == null || !gameControl.getGame(getPlayer()).getType().equals(GameType.REGIONED))
								{
									getPlayer().sendMessage(ChatColor.RED + "You must be in a game to purchase this!");
									getUi().close();
									return;
								}
								
								if(gameController.gpd(getPlayer()).getShards() < 220)
								{
									getPlayer().sendMessage(ChatColor.RED + "You need 220 shards to purchase this! You currently have " + gameController.gpd(getPlayer()).getShards() + " (/skill)");
									getUi().close();
									return;
								}
								
								RegionedGame rg = (RegionedGame) gameControl.getGame(getPlayer());
								
								if(!rg.canBoost(getPlayer()))
								{
									getPlayer().sendMessage(ChatColor.RED + "You have already boosted this game! You can do this in the next game!");
									getUi().close();
									return;
								}
								
								gameController.gpd(getPlayer()).setShards(gameController.gpd(getPlayer()).getShards() - 220);
								rg.secureBoost(getPlayer(), 1.25);
								getUi().close();
								rg.getNotificationHandler().queue(getPlayer(), NotificationPreset.BOOSTED.format(null, new Object[] {ChatColor.AQUA + "^125%"}, null));
							}
						});
						
						getUi().open(pane);
					}
				});
				
				w.setOnLeftClickListener(new ElementClickListener()
				{
					public void run()
					{
						close();
						getUi().getPanes().clear();
						Pane pane = new Pane(getUi(), "Select a Weapon");
						
						int c = 0;
						
						for(final Weapon i : gameController.getObtainableBank().getObtainableFilter().getWeapons())
						{
							if(!gameController.getObtainableBank().getObtainableFilter().has(getPlayer(), i))
							{
								Element e = configure(getPlayer(), pane, i, c);
								e.setOnLeftClickListener(new ElementClickListener()
								{
									public void run()
									{
										PlayerData pd = gpd(getPlayer());
										
										if(i.getCost() <= pd.getSkill())
										{
											Audio.CAPTURE_CAPTURE.play(getPlayer());
											getMarketController().buy(getPlayer(), i);
											close();
										}
										
										else
										{
											Audio.UI_FAIL.play(getPlayer());
										}
									}
								});
								
								c++;
							}
						}
						
						getUi().open(pane);
					}
				});
				
				a.setOnLeftClickListener(new ElementClickListener()
				{
					public void run()
					{
						close();
						getUi().getPanes().clear();
						Pane pane = new Pane(getUi(), "Select Ammunition");
						
						int c = 0;
						
						for(final Projectile i : gameController.getObtainableBank().getObtainableFilter().getProjectiles())
						{
							if(!gameController.getObtainableBank().getObtainableFilter().has(getPlayer(), i))
							{
								Element e = configure(getPlayer(), getPane(), i, c);
								e.setOnLeftClickListener(new ElementClickListener()
								{
									public void run()
									{
										PlayerData pd = gpd(getPlayer());
										
										if(i.getCost() <= pd.getSkill())
										{
											Audio.CAPTURE_CAPTURE.play(getPlayer());
											getMarketController().buy(getPlayer(), i);
											close();
										}
										
										else
										{
											Audio.UI_FAIL.play(getPlayer());
										}
									}
								});
								
								c++;
							}
						}
						
						getUi().open(pane);
					}
				});
				
				ab.setOnLeftClickListener(new ElementClickListener()
				{
					public void run()
					{
						close();
						getUi().getPanes().clear();
						Pane pane = new Pane(getUi(), "Select Abilities");
						
						int c = 0;
						
						for(final Ability i : gameController.getObtainableBank().getObtainableFilter().getAbilities())
						{
							if(!gameController.getObtainableBank().getObtainableFilter().has(getPlayer(), i))
							{
								Element e = configure(getPlayer(), getPane(), i, c);
								
								e.setOnLeftClickListener(new ElementClickListener()
								{
									public void run()
									{
										PlayerData pd = gpd(getPlayer());
										
										if(i.getCost() <= pd.getSkill())
										{
											Audio.CAPTURE_CAPTURE.play(getPlayer());
											getMarketController().buy(getPlayer(), i);
											close();
										}
										
										else
										{
											Audio.UI_FAIL.play(getPlayer());
										}
									}
								});
								
								c++;
							}
						}
						
						getUi().open(pane);
					}
				});
			}
		}).setIX(-2).setIY(2);
		
		sLoadout = new Shortcut(ChatColor.BLUE + "Loadout", Material.COOKIE, -3, 1).onShortcutLaunch(new ShortcutLaunchListener()
		{
			public void run()
			{
				Loadout loadout = gpd(getPlayer()).getLoadoutSet().getLoadout();
				Element primary = new Element(getPane(), ChatColor.RED + "No Primary Weapon", Material.QUARTZ, -1, 2).addLore(ChatColor.DARK_RED + "Click to equip an item you own");
				Element secondary = new Element(getPane(), ChatColor.RED + "No Secondary Weapon", Material.QUARTZ, 0, 2).addLore(ChatColor.DARK_RED + "Click to equip an item you own");
				Element tertiary = new Element(getPane(), ChatColor.RED + "No Tertiary Weapon", Material.QUARTZ, 1, 2).addLore(ChatColor.DARK_RED + "Click to equip an item you own");
				Element tool = new Element(getPane(), ChatColor.RED + "No Tool", Material.QUARTZ, 1, 3).addLore(ChatColor.DARK_RED + "Click to equip an item you own");
				Element utility = new Element(getPane(), ChatColor.RED + "No Utility", Material.QUARTZ, -1, 3).addLore(ChatColor.DARK_RED + "Click to equip an item you own");
				Element projectile = null;
				Element ability = new Element(getPane(), ChatColor.RED + "No Ability", Material.QUARTZ, 0, 4).addLore(ChatColor.DARK_RED + "Click to equip an ability you own");
				
				final Obtainable pw = gameController.getObtainableBank().resolve(loadout.getPrimaryWeapon());
				final Obtainable sw = gameController.getObtainableBank().resolve(loadout.getSecondaryWeapon());
				final Obtainable tw = gameController.getObtainableBank().resolve(loadout.getTertiaryWeapon());
				Obtainable t = gameController.getObtainableBank().resolve(loadout.getTool());
				Obtainable u = gameController.getObtainableBank().resolve(loadout.getUtility());
				final Obtainable pjt = gameController.getObtainableBank().resolve(loadout.getProjectile());
				Obtainable a = gameController.getObtainableBank().resolve(loadout.getAbility());
				
				if(pw != null)
				{
					primary = configure(getPlayer(), getPane(), pw, 12);
				}
				
				if(sw != null)
				{
					secondary = configure(getPlayer(), getPane(), sw, 13);
				}
				
				if(tw != null)
				{
					tertiary = configure(getPlayer(), getPane(), tw, 14);
				}
				
				if(a != null)
				{
					ability = configure(getPlayer(), getPane(), a, 31);
				}
				
				ability.setOnLeftClickListener(new ElementClickListener()
				{
					public void run()
					{
						close();
						selectAbility(this.getPlayer());
					}
				});
				
				primary.setOnLeftClickListener(new ElementClickListener()
				{
					public void run()
					{
						close();
						selectWeapon(this.getPlayer(), WeaponEnclosureType.PRIMARY, pw);
					}
				});
				
				secondary.setOnLeftClickListener(new ElementClickListener()
				{
					public void run()
					{
						close();
						selectWeapon(this.getPlayer(), WeaponEnclosureType.SECONDARY, sw);
					}
				});
				
				tertiary.setOnLeftClickListener(new ElementClickListener()
				{
					public void run()
					{
						close();
						selectWeapon(this.getPlayer(), WeaponEnclosureType.TERTIARY, tw);
					}
				});
				
				boolean pjtx = false;
				
				if(pw != null)
				{
					if(gameController.getObtainableBank().getObtainableFilter().isRangedWeapon(pw))
					{
						pjtx = true;
					}
				}
				
				if(sw != null)
				{
					if(gameController.getObtainableBank().getObtainableFilter().isRangedWeapon(sw))
					{
						pjtx = true;
					}
				}
				
				if(tw != null)
				{
					if(gameController.getObtainableBank().getObtainableFilter().isRangedWeapon(tw))
					{
						pjtx = true;
					}
				}
				
				if(t != null)
				{
					tool.setMaterial(((Item) t).getMaterial());
					tool.setData(((Item) t).getMaterialMeta());
					tool.setTitle(ChatColor.BLUE + "Tool: " + t.getName()).clearLore().addLore(ChatColor.BLUE + sw.getDescription());
				}
				
				if(u != null)
				{
					utility.setMaterial(((Item) u).getMaterial());
					utility.setData(((Item) u).getMaterialMeta());
					utility.setTitle(ChatColor.LIGHT_PURPLE + "Utility: " + u.getName()).clearLore().addLore(ChatColor.DARK_PURPLE + tw.getDescription());
				}
				
				if(pjtx)
				{
					projectile = new Element(getPane(), ChatColor.RED + "No Projectile", Material.QUARTZ, 0, 3).addLore(ChatColor.DARK_RED + "Click to equip an item you own");
					
					if(pjt != null)
					{
						projectile = configure(getPlayer(), getPane(), pjt, 22);
						
						projectile.setOnLeftClickListener(new ElementClickListener()
						{
							public void run()
							{
								close();
								selectProjectile(this.getPlayer(), ProjectileType.ARROW, pjt);
							}
						});
					}
					
					else
					{
						projectile.setOnLeftClickListener(new ElementClickListener()
						{
							public void run()
							{
								close();
								selectProjectile(this.getPlayer(), ProjectileType.ARROW, pjt);
							}
						});
					}
				}
			}
		}).setIX(-3).setIY(2);
		
		Shortcut sHelp = new Shortcut(ChatColor.RED + "Help & Information", Material.QUARTZ, 2, 1).onShortcutLaunch(new ShortcutLaunchListener()
		{
			public void run()
			{
			
			}
		}).setIX(2).setIY(2);
		
		sSquad = new Shortcut(ChatColor.DARK_BLUE + "Squad", Material.SUGAR, -4, 1).onShortcutLaunch(new ShortcutLaunchListener()
		{
			public void run()
			{
				Element ns = new Element(getPane(), ChatColor.BLUE + "New Squad", Material.EMERALD, -4, 1);
				ns.clearLore();
				
				Game g = gameController.getGame(getPlayer());
				
				if(g == null || !g.getType().equals(GameType.REGIONED))
				{
					ns.setTitle(ChatColor.RED + "You must be in a game to create a squad");
				}
				
				else
				{
					if(((RegionedGame) g).getSquadHandler().inSquad(getPlayer()))
					{
						getPane().remove(ns);
						showSquadMenu(getPane(), getPlayer());
					}
					
					else
					{
						ns.addLore(ChatColor.AQUA + "Create a squad and add allies.");
						ns.addLore(ChatColor.AQUA + "Create objectives, and waypoints");
						ns.addLore(ChatColor.AQUA + "for all the members you invite to see");
						
						ns.setOnLeftClickListener(new ElementClickListener()
						{
							public void run()
							{
								createSquad(getPlayer());
							}
						});
						
						int c = 1;
						
						for(Squad i : ((RegionedGame) g).getSquadHandler().getSquads(((RegionedGame) g).getFactionHandler().getFaction(getPlayer())))
						{
							Element e = new Element(getPane(), i.getColor() + i.getGreek().symbol() + " " + i.getGreek().fName(), Material.STAINED_GLASS_PANE, c);
							
							if(((RegionedGame) g).getSquadHandler().isInvited(getPlayer(), i))
							{
								e.addLore(ChatColor.GREEN + "You are INVITED, Click to join!");
								
								e.setOnLeftClickListener(new ElementClickListener()
								{
									public void run()
									{
										((RegionedGame) g).getSquadHandler().join(getPlayer(), i);
										getPane().getUi().close();
										getPane().getUi().getPanes().clear();
									}
								});
							}
							
							else
							{
								e.addLore(ChatColor.RED + "You are not yet invited.");
								e.addLore(ChatColor.YELLOW + "Ask " + i.getLeader().getName() + " to join");
							}
							
							c++;
						}
					}
				}
			}
		}).setIX(-4).setIY(2);
		
		uiController.addShortcut(sSquad);
		uiController.addShortcut(sShop);
		uiController.addShortcut(sLoadout);
		uiController.addShortcut(sHelp);
		uiController.addShortcut(sGames);
		uiController.addShortcut(sSettings);
		uiController.addShortcut(sStats);
	}
	
	public void showStats(String cat, Player p)
	{
		uiController.get(p).close();
		uiController.get(p).getPanes().clear();
		
		Pane pane = new Pane(uiController.get(p), ChatColor.GOLD + "Statistics for " + cat);
		
		GMap<String, GList<Statistic>> map = Statistic.filter();
		
		int c = 0;
		
		for(Statistic i : map.get(cat))
		{
			Element e = new Element(pane, ChatColor.GOLD + i.tag(gpd(p).getPlayerStatistics().get(i)), Material.COOKIE, c);
			e.addLore(ChatColor.YELLOW + i.describe(gpd(p).getPlayerStatistics().get(i)));
			
			c++;
		}
		
		Element cl = new Element(pane, "Back to Stats", Material.BARRIER, 4, 6);
		cl.setOnLeftClickListener(new ElementClickListener()
		{
			public void run()
			{
				uiController.get(getPlayer()).close();
				uiController.get(getPlayer()).getPanes().clear();
				sStats.launch(uiController.get(getPlayer()));
			}
		});
		
		uiController.get(p).open(pane);
	}
	
	public void buyWeapons(Player player)
	{
	
	}
	
	public void showSquadMenu(Pane pane, Player p)
	{
		final RegionedGame rg = (RegionedGame) gameController.getGame(p);
		final Squad s = rg.getSquadHandler().getSquad(p);
		final Boolean lead = s.getLeader().equals(p);
		
		Element beacon = new Element(pane, ChatColor.BLUE + "Waypoint", Material.ARROW, -1, 2);
		beacon.clearLore();
		
		if(s.getBeacon() == null)
		{
			beacon.addLore(ChatColor.RED + "No Beacon Set");
		}
		
		else
		{
			Region r = rg.getMap().getRegion(s.getBeacon());
			beacon.addLore(ChatColor.AQUA + "Nearby: " + ChatColor.BLUE + r.getName());
			beacon.addLore(ChatColor.DARK_AQUA + "Left click your compass to view it.");
		}
		
		if(lead)
		{
			beacon.addLore(ChatColor.YELLOW + "You can change this to your location");
			beacon.addLore(ChatColor.GOLD + "Simply use /squad point");
			beacon.addLore(ChatColor.GREEN + "THIS WILL SHOW ON SQUAD MEMBERS COMPASSES. Be sure to keep it updated.");
		}
		
		Element objective = new Element(pane, ChatColor.YELLOW + "Objective", Material.IRON_SWORD, 0, 2);
		objective.clearLore();
		objective.addLore(ChatColor.AQUA + s.getObjective());
		
		if(lead)
		{
			objective.addLore(ChatColor.YELLOW + "You can change this anytime with");
			objective.addLore(ChatColor.YELLOW + "/squad objective <text>");
			objective.addLore(ChatColor.GREEN + "THIS WILL SHOW ON SQUAD MEMBERS COMPASSES. Be sure to keep it updated.");
		}
		
		Element members = new Element(pane, ChatColor.GREEN + "Members", Material.BOOK_AND_QUILL, 0, 3);
		members.clearLore();
		
		if(lead)
		{
			members.addLore(ChatColor.GREEN + "Here, you can add members, kick members, or just view them.");
		}
		
		else
		{
			members.addLore(ChatColor.GREEN + "Here, you can view members in your squad.");
		}
		
		members.setOnLeftClickListener(new ElementClickListener()
		{
			public void run()
			{
				showMembers(getPlayer());
			}
		});
		
		Element leave = new Element(pane, ChatColor.DARK_RED + "Leave Squad", Material.BARRIER, 4, 6);
		leave.clearLore();
		
		if(lead)
		{
			if(s.getMembers().size() == 1)
			{
				leave.addLore(ChatColor.RED + "Leaving This squad will DISBAND the squad. Someone else will be able to create a squad with this symbol. Since no one else is in the squad.");
			}
			
			else
			{
				leave.addLore(ChatColor.RED + "Leaving This squad will make another RANDOM player in the squad as the new leader. You can use /squad setleader <player> instead BEFORE LEAVING.");
			}
		}
		
		else
		{
			leave.addLore(ChatColor.RED + "By leaving this squad, you will not be able to join it again unless the leader invites you!");
		}
		
		leave.setOnLeftClickListener(new ElementClickListener()
		{
			public void run()
			{
				rg.getSquadHandler().leave(getPlayer());
				getUi().close();
			}
		});
	}
	
	public void showMembers(Player p)
	{
		uiController.get(p).close();
		uiController.get(p).getPanes().clear();
		
		Pane pane = new Pane(uiController.get(p), ChatColor.GREEN + "Squad Members");
		final RegionedGame rg = (RegionedGame) gameController.getGame(p);
		final Squad s = rg.getSquadHandler().getSquad(p);
		Integer c = 0;
		
		Element bk = new Element(pane, ChatColor.RED + "Back", Material.BARRIER, 4, 6);
		bk.addLore(ChatColor.RED + "(Squad Overview)");
		
		bk.setOnLeftClickListener(new ElementClickListener()
		{
			public void run()
			{
				close();
				getUi().getPanes().clear();
				sSquad.launch(getUi());
			}
		});
		
		for(Player i : s.getMembers())
		{
			if(i.equals(p))
			{
				continue;
			}
			
			Element e = new Element(pane, s.getColor() + i.getName(), Material.BOOK, c);
			
			if(rg.getSquadHandler().isLeader(p, s))
			{
				e.addLore(ChatColor.RED + "RIGHT CLICK TO KICK PLAYER");
				e.addLore(ChatColor.RED + "or use /squad kick " + i.getName());
				
				e.setOnRightClickListener(new ElementClickListener()
				{
					public void run()
					{
						rg.getSquadHandler().leave(i);
						close();
						getUi().getPanes().clear();
						showMembers(getPlayer());
					}
				});
			}
			
			c++;
		}
		
		if(rg.getSquadHandler().isLeader(p, s))
		{
			Element ad = new Element(pane, ChatColor.AQUA + "Invite a Player", Material.EMERALD, c);
			ad.addLore(ChatColor.YELLOW + "Use /squad invite <player>");
		}
		
		getUiController().get(p).open(pane);
	}
	
	public void createSquad(Player p)
	{
		uiController.get(p).close();
		uiController.get(p).getPanes().clear();
		RegionedGame rg = (RegionedGame) gameController.getGame(p);
		GList<Greek> gck = rg.getSquadHandler().squadsForCreation(rg.getFactionHandler().getFaction(p));
		
		if(gck.isEmpty())
		{
			p.closeInventory();
			p.sendMessage(ChatColor.RED + "No Squads can be created at this time (join one)");
			return;
		}
		
		Pane pane = new Pane(uiController.get(p), "Select a Color / Symbol");
		int s = 0;
		
		for(final Greek i : gck)
		{
			Element e = new Element(pane, i.color() + "[" + i.symbol() + "] " + i.fName(), Material.STAINED_GLASS_PANE, s);
			e.clearLore();
			e.addLore(i.color() + "Click to make " + i.fName() + " your squad symbol. This will show up in many ways.");
			e.addLore(i.color() + "[" + i.symbol() + "] Will show up in your members chat tags");
			e.addLore(i.color() + "" + i.symbol() + " Will show up on region banners you capture");
			e.addLore(i.color() + "Your members helmets will be colored " + i.dye().toString().toLowerCase());
			e.addLore(i.color() + "Some particles from your squad are " + i.dye().toString().toLowerCase());
			
			e.setOnLeftClickListener(new ElementClickListener()
			{
				public void run()
				{
					rg.getSquadHandler().create(p, i);
					pane.getUi().close();
					getUi().getPanes().clear();
					p.closeInventory();
					uiController.get(getPlayer()).close();
					uiController.get(getPlayer()).getPanes().clear();
					sSquad.launch(uiController.get(getPlayer()));
				}
			});
			
			s++;
		}
		
		pane.getUi().open(pane);
	}
	
	public void selectAbility(Player player)
	{
		Pane pane = new Pane(uiController.get(player), "Select an ability");
		int s = 0;
		
		for(final Ability i : gameController.gpo(player).abilities())
		{
			if(gameController.getObtainableBank().getObtainableFilter().has(player, i))
			{
				Element e = configure(player, pane, i, s);
				e.setOnLeftClickListener(new ElementClickListener()
				{
					public void run()
					{
						gameController.gpd(getPlayer()).getLoadoutSet().getLoadout().setAbility(i.getId());
						
						uiController.get(getPlayer()).close();
						uiController.get(getPlayer()).getPanes().clear();
						sLoadout.launch(uiController.get(getPlayer()));
					}
				});
				
				s++;
			}
		}
		
		uiController.get(player).open(pane);
	}
	
	public void selectProjectile(Player player, final ProjectileType type, Obtainable current)
	{
		Pane pane = new Pane(uiController.get(player), "Select an " + type.toString().toLowerCase());
		int s = 0;
		
		for(final Projectile i : gameController.gpo(player).projectiles())
		{
			if(i.getProjectileType().equals(type))
			{
				if(gameController.getObtainableBank().getObtainableFilter().has(player, i))
				{
					Element e = configure(player, pane, i, s);
					e.setOnLeftClickListener(new ElementClickListener()
					{
						public void run()
						{
							gameController.gpd(getPlayer()).getLoadoutSet().getLoadout().setProjectile(i.getId());
							
							uiController.get(getPlayer()).close();
							uiController.get(getPlayer()).getPanes().clear();
							sLoadout.launch(uiController.get(getPlayer()));
						}
					});
					
					s++;
				}
			}
		}
		
		uiController.get(player).open(pane);
	}
	
	public void selectWeapon(Player player, final WeaponEnclosureType type, Obtainable current)
	{
		Pane pane = new Pane(uiController.get(player), "Select a " + type.toString().toLowerCase() + " weapon");
		int s = 0;
		
		for(final Weapon i : gameController.gpo(player).weapons())
		{
			if(i.getWeaponEnclosureType().equals(type))
			{
				if(gameController.getObtainableBank().getObtainableFilter().has(player, i))
				{
					Element e = configure(player, pane, i, s);
					e.setOnLeftClickListener(new ElementClickListener()
					{
						public void run()
						{
							if(type.equals(WeaponEnclosureType.PRIMARY))
							{
								gameController.gpd(getPlayer()).getLoadoutSet().getLoadout().setPrimaryWeapon(i.getId());
							}
							
							else if(type.equals(WeaponEnclosureType.SECONDARY))
							{
								gameController.gpd(getPlayer()).getLoadoutSet().getLoadout().setSecondaryWeapon(i.getId());
							}
							
							else if(type.equals(WeaponEnclosureType.TERTIARY))
							{
								gameController.gpd(getPlayer()).getLoadoutSet().getLoadout().setTertiaryWeapon(i.getId());
							}
							
							uiController.get(getPlayer()).close();
							uiController.get(getPlayer()).getPanes().clear();
							sLoadout.launch(uiController.get(getPlayer()));
						}
					});
					
					s++;
				}
			}
		}
		
		uiController.get(player).open(pane);
	}
	
	public Element configure(Player p, Pane pane, Obtainable o, int slot)
	{
		ObtainableFilter f = gameController.getObtainableBank().getObtainableFilter();
		Element e = new Element(pane, ChatColor.AQUA + o.getName(), Material.COOKIE, slot);
		e.addLore(ChatColor.AQUA + o.getDescription());
		
		if(f.has(p, o))
		{
			e.addLore(ChatColor.GREEN + "OWNED");
		}
		
		else
		{
			Long sk = gameController.gpd(p).getSkill();
			
			if(o.getCost() <= sk)
			{
				e.addLore(ChatColor.GREEN + "- Costs " + o.getCost() + " Skill Points");
			}
			
			else
			{
				e.addLore(ChatColor.RED + "- Costs " + o.getCost() + " Skill Points");
			}
		}
		
		if(f.isUpgrade(o))
		{
			Upgrade u = (Upgrade) o;
			
			e.setMaterial(Material.COOKIE);
			
			if(u.getUpgradeType().equals(UpgradeType.PROJECTILE))
			{
				ProjectileUpgrade uu = (ProjectileUpgrade) u;
				e.addLore(ChatColor.YELLOW + "- Projectile Upgrade");
				e.addLore(ChatColor.GOLD + "- Damage: +" + (int) ((double) uu.getDamageModifier() / (double) 100) + "% dmg");
				e.addLore(ChatColor.GOLD + "- Ammunition: +" + (int) ((double) uu.getAmmunitionModifier() / (double) 100) + "% ammo");
				e.addLore(ChatColor.GOLD + "- Velocity: +" + (int) ((double) uu.getVelocityModifier() / (double) 100) + "% speed");
				e.addLore(ChatColor.RED + "- Compatible with " + uu.getProjectileType().toString().toLowerCase() + "s");
				e.setMaterial(Material.ARROW);
				
				return e;
			}
			
			else if(u.getUpgradeType().equals(UpgradeType.MELEE_WEAPON))
			{
				MeleeWeaponUpgrade uu = (MeleeWeaponUpgrade) u;
				e.addLore(ChatColor.YELLOW + "- Melee Weapon Upgrade");
				e.addLore(ChatColor.GOLD + "- Damage: +" + 100 * ((int) ((double) uu.getDamageModifier() / (double) 100)) + "% dmg");
				e.addLore(ChatColor.RED + "- Compatible with Melee Weapons");
				e.setMaterial(Material.IRON_SWORD);
				
				return e;
			}
			
			else if(u.getUpgradeType().equals(UpgradeType.RANGED_WEAPON))
			{
				RangedWeaponUpgrade uu = (RangedWeaponUpgrade) u;
				e.addLore(ChatColor.YELLOW + "- Ranged Weapon Upgrade (" + uu.getProjectileType().toString().toLowerCase() + "s)");
				e.addLore(ChatColor.GOLD + "- Rate Of Fire: +" + 100 * ((int) ((double) uu.getRateOfFireModifier() / (double) 100)) + "% rof");
				e.addLore(ChatColor.RED + "- Compatible with Ranged Weapons (" + uu.getProjectileType().toString().toLowerCase() + "s)");
				e.setMaterial(Material.BOW);
				
				return e;
			}
			
			else if(u.getUpgradeType().equals(UpgradeType.TOOL))
			{
			
			}
			
			else if(u.getUpgradeType().equals(UpgradeType.UTILITY))
			{
			
			}
			
			return e;
		}
		
		else if(f.isItem(o))
		{
			Item i = (Item) o;
			e.setMaterial(i.getMaterial());
			e.setData(i.getMaterialMeta());
			
			if(f.isWeapon(o))
			{
				Weapon w = (Weapon) i;
				
				if(f.isMeleeWeapon(o))
				{
					MeleeWeapon mw = (MeleeWeapon) w;
					
					e.addLore(ChatColor.YELLOW + "- Melee Weapon");
					e.addLore(ChatColor.GOLD + "- Damage: " + mw.getDamage());
					
					for(MeleeWeaponUpgrade j : getGameController().getObtainableBank().getObtainableFilter().getMeleeWeaponUpgrades())
					{
						if(gameController.getObtainableBank().getObtainableFilter().has(p, j))
						{
							e.addLore(ChatColor.LIGHT_PURPLE + "Enchantment: " + j.getName());
						}
					}
					
					return e;
				}
				
				else if(f.isRangedWeapon(o))
				{
					RangedWeapon rw = (RangedWeapon) w;
					
					e.addLore(ChatColor.YELLOW + "- Ranged Weapon");
					e.addLore(ChatColor.GOLD + "- Damage Multiplier: " + rw.getDamageMultiplier());
					e.addLore(ChatColor.GOLD + "- Shoots: " + rw.getProjectileType().toString().toLowerCase() + "s");
					e.addLore(ChatColor.GOLD + "- Fire Rate: " + rw.getRateOfFire());
					e.addLore(ChatColor.GOLD + "- Automatic: " + (rw.getAutomatic() ? "Yes" : "No"));
					
					for(RangedWeaponUpgrade j : getGameController().getObtainableBank().getObtainableFilter().getRangedWeaponUpgrades())
					{
						if(gameController.getObtainableBank().getObtainableFilter().has(p, j))
						{
							e.addLore(ChatColor.LIGHT_PURPLE + "Enchantment: " + j.getName());
						}
					}
					
					return e;
				}
			}
			
			else if(f.isTool(o))
			{
			
			}
			
			else if(f.isUtility(o))
			{
			
			}
		}
		
		else if(f.isAbility(o))
		{
			Ability aa = (Ability) o;
			
			e.addLore(ChatColor.YELLOW + "- Ability (press [drop] to activate)");
			e.addLore(ChatColor.GOLD + "- Duration: " + aa.getDuration());
			e.addLore(ChatColor.GOLD + "- Cooldown: " + aa.getCooldown());
			e.setMaterial(Material.SUGAR);
		}
		
		else if(f.isProjectile(o))
		{
			Projectile pj = (Projectile) o;
			
			if(pj.getProjectileType().equals(ProjectileType.ARROW))
			{
				ProjectileArrow pja = (ProjectileArrow) pj;
				
				e.setMaterial(Material.ARROW);
				e.addLore(ChatColor.YELLOW + "- Arrow Projectile");
				e.addLore(ChatColor.GOLD + "- Damage: " + pja.getDamage());
				e.addLore(ChatColor.GOLD + "- Velocity: " + pja.getVelocity());
				
				for(ProjectileUpgrade j : getGameController().getObtainableBank().getObtainableFilter().getProjectileUpgrades(ProjectileType.ARROW))
				{
					if(gameController.getObtainableBank().getObtainableFilter().has(p, j))
					{
						e.addLore(ChatColor.LIGHT_PURPLE + "Enchantment: " + j.getName());
					}
				}
				
				return e;
			}
		}
		
		return e;
	}
	
	public UIController getUiController()
	{
		return uiController;
	}
	
	public GameController getGameController()
	{
		return gameController;
	}
	
	public CommandController getCommandController()
	{
		return commandController;
	}
	
	public MapDataController getMapDataController()
	{
		return mapDataController;
	}
}
