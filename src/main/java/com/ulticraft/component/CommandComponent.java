package com.ulticraft.component;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import com.ulticraft.GlacialRush;
import com.ulticraft.Info;
import com.ulticraft.uapi.Component;
import com.ulticraft.uapi.UMap;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class CommandComponent extends Component implements CommandExecutor, Listener
{
	private UMap<Player, String> mode;
	
	public CommandComponent(GlacialRush pl)
	{
		super(pl);
		
		mode = new UMap<Player, String>();
	}
	
	public void enable()
	{
		super.enable();
	}
	
	public void disable()
	{
		super.disable();
	}
	
	public void msg(Player p, String msg)
	{
		String tag = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "GRush";
		
		if(mode.containsKey(p))
		{
			String mod = mode.get(p).toUpperCase().substring(0, 1) + mode.get(p).toLowerCase().substring(1);
			tag = tag + ChatColor.GREEN + " " + mod + ChatColor.DARK_GRAY + "]" + ChatColor.AQUA + ": ";
		}
		
		else
		{
			tag = tag + ChatColor.DARK_GRAY + "]" + ChatColor.AQUA + ": ";
		}
		
		p.sendMessage(tag + msg);
	}
	
	@EventHandler
	public void onChat(PlayerChatEvent e)
	{
		if(mode.containsKey(e.getPlayer()))
		{
			String msg = e.getMessage();
			String mod = mode.get(e.getPlayer());
			
			e.setCancelled(true);
			
			if(msg.equalsIgnoreCase("exit") || msg.equalsIgnoreCase("esc")) 
			{
				msg(e.getPlayer(), ChatColor.GOLD + "Exiting Mode: " + mod);
				mode.remove(e.getPlayer());
				return;
			}
			
			if(mod.equals("geography"))
			{
				
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		boolean isPlayer = false;
		boolean isGod = sender.hasPermission(Info.PERM_GOD);
		Player player = null;
		
		if(sender instanceof Player)
		{
			isPlayer = true;
			player = (Player) sender;
		}
		
		if(cmd.getName().equalsIgnoreCase(Info.CMD_GR))
		{
			if(!isGod)
			{
				return true;
			}
			
			if(args.length == 0)
			{
				
			}
			
			if(args.length == 1)
			{
				if(isPlayer)
				{
					if(args[0].equalsIgnoreCase("geo") || args[0].equalsIgnoreCase("map"))
					{
						mode.put(player, "geography");
						msg(player, ChatColor.GREEN + "Geography Mode Enabled");
					}
				}
			}
		}
		
		return false;
	}
}
