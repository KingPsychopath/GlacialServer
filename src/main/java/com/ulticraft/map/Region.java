package com.ulticraft.map;

import java.io.Serializable;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import com.ulticraft.GlacialRush;
import com.ulticraft.component.WorldComponent;
import com.ulticraft.faction.Faction;
import com.ulticraft.uapi.UChunk;
import com.ulticraft.uapi.UList;
import com.ulticraft.uapi.ULocation;

public class Region implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected String name;
	protected UChunk centerChunk;
	protected UList<UChunk> chunks;
	protected UList<CapturePoint> capturePoints;
	protected UList<ULocation> accents;
	protected Faction faction;
	protected GlacialRush pl;
	
	public Region(GlacialRush pl, String name, Chunk chunk)
	{
		this.name = name;
		this.pl = pl;
		this.centerChunk = new UChunk(chunk);
	}
	
	public Faction getFaction()
	{
		return faction;
	}
	
	@SuppressWarnings("deprecation")
	public void outline(Player player)
	{
		Chunk chunk = centerChunk.toChunk();
		World world = chunk.getWorld();
		Chunk tl = chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ() + 1);
		Location tlr = tl.getBlock(0, player.getLocation().getBlockY(), 15).getLocation();
		Location lbt = tl.getBlock(0, player.getLocation().getBlockY(), 0).getLocation();
		
		for(int i = tlr.getBlockX(); i < tlr.getBlockX() + 16; i++)
		{
			pl.getWorldComponent().addJob(player, new Location(world, i, player.getLocation().getY(), 15), Material.GLOWSTONE);
			pl.getWorldComponent().addJob(player, new Location(world, i, player.getLocation().getY(), 0), Material.GLOWSTONE);
		}
		
		for(int i = lbt.getBlockY(); i < lbt.getBlockX() + 16; i++)
		{
			pl.getWorldComponent().addJob(player, new Location(world, 15, player.getLocation().getY(), i), Material.GLOWSTONE);
			pl.getWorldComponent().addJob(player, new Location(world, 0, player.getLocation().getY(), i), Material.GLOWSTONE);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void setFaction(Faction faction)
	{
		this.faction = faction;
		
		DyeColor dye = faction.getDyeColor();
		
		for(ULocation j : accents)
		{
			pl.getWorldComponent().addJob(j.toLocation(), dye);
		}
		
		for(CapturePoint i : capturePoints)
		{
			setFactionCapture(i, faction);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void setFactionCapture(CapturePoint cap, Faction faction)
	{
		Block b = cap.getBlock().toLocation().getBlock();
		
		if(b.getType().equals(Material.BEACON))
		{
			WorldComponent w = pl.getWorldComponent();
			
			w.addJob(b.getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS);
			
			b.getRelative(BlockFace.UP).setData(faction.getDyeColor().getData());
			
			b.getRelative(BlockFace.UP).setData(faction.getDyeColor().getData());
			
			b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setData(faction.getDyeColor().getData());
			
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setData(faction.getDyeColor().getData());
			
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setData(faction.getDyeColor().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setData(faction.getDyeColor().getData());
			
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).setData(faction.getDyeColor().getData());
		}
	}
	
	public void reset()
	{
		reset(Faction.neutral());
	}
	
	public void reset(Faction faction)
	{
		Chunk chunk = centerChunk.toChunk();
		
		chunks = new UList<UChunk>();
		capturePoints = new UList<CapturePoint>();
		accents = new UList<ULocation>();
		
		chunks.add(new UChunk(chunk));
		chunks.add(new UChunk(chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ() + 1)));
		chunks.add(new UChunk(chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ() + 1)));
		chunks.add(new UChunk(chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ() + 1)));
		chunks.add(new UChunk(chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ())));
		chunks.add(new UChunk(chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ())));
		chunks.add(new UChunk(chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ() - 1)));
		chunks.add(new UChunk(chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ() - 1)));
		chunks.add(new UChunk(chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ() - 1)));
		
		World world = chunk.getWorld();
		
		for(UChunk i : chunks)
		{
			int bx = i.getX() << 4;
			int bz = i.getZ() << 4;
			
			for(int xx = bx; xx < bx + 16; xx++)
			{
				for(int zz = bz; zz < bz + 16; zz++)
				{
					for(int yy = 0; yy < 128; yy++)
					{
						Block block = world.getBlockAt(xx, yy, zz);
						
						if(block.getType().equals(Material.WOOL) || block.getType().equals(Material.STAINED_GLASS) || block.getType().equals(Material.STAINED_GLASS_PANE) || block.getType().equals(Material.STAINED_CLAY) || block.getType().equals(Material.BANNER) || block.getType().equals(Material.STANDING_BANNER) || block.getType().equals(Material.WALL_BANNER) || block.getType().equals(Material.CARPET))
						{
							accents.add(new ULocation(block.getLocation()));
						}
					}
				}
			}
		}
		
		for(UChunk i : chunks)
		{
			int bx = i.getX() << 4;
			int bz = i.getZ() << 4;
			
			for(int xx = bx; xx < bx + 16; xx++)
			{
				for(int zz = bz; zz < bz + 16; zz++)
				{
					for(int yy = 0; yy < 128; yy++)
					{
						Block block = world.getBlockAt(xx, yy, zz);
						
						if(block.getType().equals(Material.BEACON))
						{
							capturePoints.add(new CapturePoint("Capture Point", block));
							
							block.getRelative(BlockFace.DOWN).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).setType(Material.IRON_BLOCK);
						}
					}
				}
			}
		}
		
		setFaction(faction);
	}
}
