package com.glacialrush.game;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.glacialrush.api.GlacialPlugin;
import com.glacialrush.api.dispatch.Dispatcher;

public class GDispatcher extends Dispatcher
{
	protected GlacialHandler c;
	
	public GDispatcher(GlacialPlugin pl, GlacialHandler c)
	{
		super(pl);
		
		this.c = c;
	}

	@Override
	public void log(String s, String... o)
	{
		String msg = s + "";

		for(String i : o)
		{
			msg = msg + i;
		}

		pl.getServer().getConsoleSender().sendMessage(ChatColor.WHITE + pl.getName() + ChatColor.GOLD + "/" + c.getName() + ": " + ChatColor.WHITE + msg);
		
		for(Player i : listeners)
		{
			i.sendMessage("$ " + ChatColor.WHITE + pl.getName() + ChatColor.GRAY + "/" + c.getName() + ": " + msg);
		}
	}
	
	public void info(String... o)
	{
		log("" + ChatColor.WHITE, o);
	}

	public void success(String... o)
	{
		log("" + ChatColor.GREEN, o);
	}

	public void failure(String... o)
	{
		log("" + ChatColor.RED, o);
	}

	public void warning(String... o)
	{
		log("" + ChatColor.YELLOW, o);
	}

	public void verbose(String... o)
	{
		log("" + ChatColor.LIGHT_PURPLE, o);
	}

	public void overbose(String... o)
	{
		log("" + ChatColor.AQUA, o);
	}

	public void sinfo(String... o)
	{
		log("" + ChatColor.GRAY, o);
	}

	public void ssuccess(String... o)
	{
		log("" + ChatColor.DARK_GREEN, o);
	}

	public void sfailure(String... o)
	{
		log("" + ChatColor.DARK_RED, o);
	}

	public void swarning(String... o)
	{
		log("" + ChatColor.GOLD, o);
	}

	public void sverbose(String... o)
	{
		log("" + ChatColor.DARK_PURPLE, o);
	}

	public void soverbose(String... o)
	{
		log("" + ChatColor.DARK_AQUA, o);
	}
}
