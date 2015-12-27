package com.ulticraft.component;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ulticraft.GlacialRush;
import com.ulticraft.Info;
import com.ulticraft.uapi.Component;
import net.md_5.bungee.api.ChatColor;

public class CommandComponent extends Component implements CommandExecutor
{
	public CommandComponent(final GlacialRush pl)
	{
		super(pl);
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
						if(args[0].equals("sel") || args[0].equals("select"))
						{
							
						}
					}
				}
			}
		}
		
		return false;
	}
}
