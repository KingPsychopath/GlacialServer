package com.glacialrush.component;

import org.bukkit.entity.Player;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.component.Controller;
import com.glacialrush.api.thread.GlacialTask;

public class TabController extends Controller
{
	private int delay;
	
	public TabController(GlacialServer pl)
	{
		super(pl);
		delay = 0;
	}
	
	public void preEnable()
	{
		super.preEnable();
		
		pl.newThread(new GlacialTask("Tab Controller")
		{
			@Override
			public void run()
			{
				TabController.this.delay++;
				
				if(TabController.this.delay > 10)
				{
					TabController.this.delay = 0;
					updateTab();
				}
			}
		});
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
	
	public void updateTab()
	{
		if(((GlacialServer) pl).getGameController().isRunning())
		{	
			for(Player i : pl.onlinePlayers())
			{
				updateTab(i);
			}
		}
	}
	
	public void updateTab(Player p)
	{
//		GlacialServer pl = (GlacialServer) this.pl;
//		GameController g = pl.getGameController();
//		Faction f = g.getPlayerHandler().getFaction(p);
//		Region r = g.getMap().getRegion(p);
//		Map m = g.getMap();
//		Long xp = pl.getExperienceController().getExperience(p);
//		Long sk = pl.getExperienceController().getSkill(p);
//		Integer br = pl.getExperienceController().getBattleRank(p);
//		Long bru = pl.getExperienceController().getExperienceForRank(p);
//		
//		TabAPI.clearAllItems(p);
//		
//		TabAPI.setHeader(p, ChatColor.AQUA + "Glacial Rush");
//		TabAPI.setFooter(p, ChatColor.GREEN + "TCC: " + pl.getThreadComponent().getTasks().size() + " " + ChatColor.YELLOW + "MPC: " + (pl.getThreadComponent().getCycleTime() / pl.getThreadComponent().getCycles()) + " " + ChatColor.AQUA + "TPS: " + TPS.getTPS() / 3);
//				
//		TabAPI.addItem(p, new TabItem(f.getColor() + f.getName()));
//		TabAPI.addItem(p, new TabItem(ChatColor.AQUA + "Skill: " + ChatColor.GREEN + NF.c(sk)));
//		TabAPI.addItem(p, new TabItem(ChatColor.AQUA + "XP: " + ChatColor.GREEN + NF.a(xp)));
//		TabAPI.addItem(p, new TabItem(ChatColor.AQUA + "Rank: " + ChatColor.GREEN + br));
//		TabAPI.addItem(p, new TabItem(ChatColor.AQUA + "Next: " + ChatColor.GREEN + NF.a(bru) + " XP"));
//		//TabAPI.updateTab(p);
	}
}
