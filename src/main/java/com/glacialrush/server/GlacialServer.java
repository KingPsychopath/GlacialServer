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
import com.glacialrush.api.game.GameController;
import com.glacialrush.api.game.RegionedGame;
import com.glacialrush.api.game.data.ChunkletData;
import com.glacialrush.api.game.data.MapData;
import com.glacialrush.api.game.data.PlayerData;
import com.glacialrush.api.game.data.RegionData;
import com.glacialrush.api.game.loadout.Loadout;
import com.glacialrush.api.game.obtainable.Item;
import com.glacialrush.api.game.obtainable.Obtainable;
import com.glacialrush.api.game.obtainable.item.weapon.MeleeWeapon;
import com.glacialrush.api.game.obtainable.item.weapon.RangedWeapon;
import com.glacialrush.api.gui.Element;
import com.glacialrush.api.gui.ElementClickListener;
import com.glacialrush.api.gui.Shortcut;
import com.glacialrush.api.gui.ShortcutLaunchListener;
import com.glacialrush.api.map.Chunklet;
import com.glacialrush.api.map.Map;
import com.glacialrush.api.map.RegionType;
import com.glacialrush.api.map.region.Edge;
import com.glacialrush.api.map.region.Scenery;
import com.glacialrush.api.map.region.Territory;
import com.glacialrush.api.map.region.Village;
import com.glacialrush.api.rank.Rank;
import com.glacialrush.api.sfx.Audio;
import net.md_5.bungee.api.ChatColor;

public class GlacialServer extends GlacialPlugin implements Listener
{
	private GameController gameController;
	private CommandController commandController;
	private MapDataController mapDataController;
	private PlayerDataComponent playerDataComponent;
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
			
			if(md.getLocked())
			{
				map.lock();
			}
			
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
								verbose.setMaterial(Material.MAGMA_CREAM);
								verbose.setTitle(ChatColor.YELLOW + "Overbose OFF");
								verbose.clearLore();
								verbose.addLore(ChatColor.YELLOW + "You are safe from the overwhelming console");
							}
							
							else
							{
								gpd(getPlayer()).setVerbose(true);
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
			
			}
		}).setIX(-2).setIY(2);
		
		Shortcut sLoadout = new Shortcut(ChatColor.BLUE + "Loadout", Material.COOKIE, -3, 1).onShortcutLaunch(new ShortcutLaunchListener()
		{
			public void run()
			{
				Loadout loadout = gpd(getPlayer()).getLoadoutSet().getLoadout();
				Element primary = new Element(getPane(), ChatColor.RED + "No Primary Weapon", Material.QUARTZ, -1, 2).addLore(ChatColor.DARK_RED + "Click to equip an item you own");
				Element secondary = new Element(getPane(), ChatColor.RED + "No Secondary Weapon", Material.QUARTZ, 0, 2).addLore(ChatColor.DARK_RED + "Click to equip an item you own");
				Element tertiary = new Element(getPane(), ChatColor.RED + "No Tertiary Weapon", Material.QUARTZ, 1, 2).addLore(ChatColor.DARK_RED + "Click to equip an item you own");
				Element tool = new Element(getPane(), ChatColor.RED + "No Tool", Material.QUARTZ, 1, 3).addLore(ChatColor.DARK_RED + "Click to equip an item you own");
				Element utility = new Element(getPane(), ChatColor.RED + "No Utility", Material.QUARTZ, -1, 3).addLore(ChatColor.DARK_RED + "Click to equip an item you own");
				
				Obtainable pw = gameController.getObtainableBank().resolve(loadout.getPrimaryWeapon());
				Obtainable sw = gameController.getObtainableBank().resolve(loadout.getSecondaryWeapon());
				Obtainable tw = gameController.getObtainableBank().resolve(loadout.getTertiaryWeapon());
				Obtainable t = gameController.getObtainableBank().resolve(loadout.getTool());
				Obtainable u = gameController.getObtainableBank().resolve(loadout.getUtility());
				
				if(pw != null)
				{
					primary.setMaterial(((Item) pw).getMaterial());
					primary.setData(((Item)pw).getMaterialMeta());
					primary.setTitle(ChatColor.AQUA + "Primary: " + pw.getName()).clearLore().addLore(ChatColor.DARK_AQUA + pw.getDescription());
					
					if(gameController.getObtainableBank().getObtainableFilter().isRangedWeapon(pw))
					{
						primary.addLore(ChatColor.GREEN + "- Type: Ranged (" + ((RangedWeapon)pw).getProjectileType().toString().toLowerCase() + ")");
						primary.addLore(ChatColor.GREEN + "- Fire Rate: " + ((RangedWeapon)pw).getRateOfFire());
						primary.addLore(ChatColor.GREEN + "- Automatic: " + ((RangedWeapon)pw).getAutomatic());
						primary.addLore(ChatColor.GREEN + "- Damage: Depends on projectile");
					}
					
					else
					{
						primary.addLore(ChatColor.GREEN + "- Type: Melee");
						primary.addLore(ChatColor.GREEN + "- Damage: " + ((MeleeWeapon)pw).getDamage());
					}
				}
				
				if(sw != null)
				{
					secondary.setMaterial(((Item) sw).getMaterial());
					secondary.setData(((Item)sw).getMaterialMeta());
					secondary.setTitle(ChatColor.GREEN + "Secondary: " + sw.getName()).clearLore().addLore(ChatColor.DARK_GREEN + sw.getDescription());
					
					if(gameController.getObtainableBank().getObtainableFilter().isRangedWeapon(sw))
					{
						secondary.addLore(ChatColor.GREEN + "- Type: Ranged (" + ((RangedWeapon)sw).getProjectileType().toString().toLowerCase() + ")");
						secondary.addLore(ChatColor.GREEN + "- Fire Rate: " + ((RangedWeapon)sw).getRateOfFire());
						secondary.addLore(ChatColor.GREEN + "- Automatic: " + ((RangedWeapon)sw).getAutomatic());
						secondary.addLore(ChatColor.GREEN + "- Damage: Depends on projectile");
					}
					
					else
					{
						secondary.addLore(ChatColor.GREEN + "- Type: Melee");
						secondary.addLore(ChatColor.GREEN + "- Damage: " + ((MeleeWeapon)sw).getDamage());
					}
				}
				
				if(tw != null)
				{
					tertiary.setMaterial(((Item) tw).getMaterial());
					tertiary.setData(((Item)tw).getMaterialMeta());
					tertiary.setTitle(ChatColor.YELLOW + "Tertiary: " + tw.getName()).clearLore().addLore(ChatColor.GOLD + tw.getDescription());
					
					if(gameController.getObtainableBank().getObtainableFilter().isRangedWeapon(tw))
					{
						tertiary.addLore(ChatColor.GREEN + "- Type: Ranged (" + ((RangedWeapon)tw).getProjectileType().toString().toLowerCase() + ")");
						tertiary.addLore(ChatColor.GREEN + "- Fire Rate: " + ((RangedWeapon)tw).getRateOfFire());
						tertiary.addLore(ChatColor.GREEN + "- Automatic: " + ((RangedWeapon)tw).getAutomatic());
						tertiary.addLore(ChatColor.GREEN + "- Damage: Depends on projectile");
					}
					
					else
					{
						tertiary.addLore(ChatColor.GREEN + "- Type: Melee");
						tertiary.addLore(ChatColor.GREEN + "- Damage: " + ((MeleeWeapon)tw).getDamage());
					}
				}
				
				if(t != null)
				{
					tool.setMaterial(((Item) t).getMaterial());
					tool.setData(((Item)t).getMaterialMeta());
					tool.setTitle(ChatColor.BLUE + "Tool: " + t.getName()).clearLore().addLore(ChatColor.BLUE + sw.getDescription());
				}
				
				if(u != null)
				{
					utility.setMaterial(((Item) u).getMaterial());
					utility.setData(((Item)u).getMaterialMeta());
					utility.setTitle(ChatColor.LIGHT_PURPLE + "Utility: " + u.getName()).clearLore().addLore(ChatColor.DARK_PURPLE + tw.getDescription());
				}

			}
		}).setIX(-3).setIY(2);
		
		Shortcut sHelp = new Shortcut(ChatColor.RED + "Help & Information", Material.QUARTZ, 2, 1).onShortcutLaunch(new ShortcutLaunchListener()
		{
			public void run()
			{
			
			}
		}).setIX(2).setIY(2);
		
		Shortcut sSquad = new Shortcut(ChatColor.DARK_BLUE + "Squad", Material.SUGAR, -4, 1).onShortcutLaunch(new ShortcutLaunchListener()
		{
			public void run()
			{
				Element ns = new Element(getPane(), ChatColor.BLUE + "New Squad", Material.EMERALD, -4, 1);
				ns.clearLore();
				ns.addLore(ChatColor.AQUA + "Create a squad and add allies.");
				ns.addLore(ChatColor.AQUA + "Create objectives, and waypoints");
				ns.addLore(ChatColor.AQUA + "for all the members to see");
				ns.addLore(ChatColor.AQUA + "Bonus 5% xp for all members and leaders");
			}
		}).setIX(-4).setIY(2);
		
		uiController.addShortcut(sSquad);
		uiController.addShortcut(sShop);
		uiController.addShortcut(sLoadout);
		uiController.addShortcut(sHelp);
		uiController.addShortcut(sGames);
		uiController.addShortcut(sSettings);
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
