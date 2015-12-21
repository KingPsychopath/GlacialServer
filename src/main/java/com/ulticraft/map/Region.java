package com.ulticraft.map;

import java.io.Serializable;
import org.bukkit.Chunk;
import org.bukkit.DyeColor;
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
	protected WorldComponent w;
	protected ULocation spawn;
	
	public Region(GlacialRush pl, String name, Chunk chunk)
	{
		this.name = name;
		this.pl = pl;
		this.centerChunk = new UChunk(chunk);
		this.w = pl.getWorldComponent();
	}
	
	public Faction getFaction()
	{
		return faction;
	}
	
	public boolean contains(Player player)
	{
		for(UChunk i : chunks)
		{
			if(player.getLocation().getChunk().getX() == i.getX() && player.getLocation().getChunk().getZ() == i.getZ())
			{
				return true;
			}
		}
		
		return false;
	}
	
	public UList<Player> getPlayers()
	{
		UList<Player> players = new UList<Player>();
		
		for(Player i : pl.onlinePlayers())
		{
			if(contains(i))
			{
				players.add(i);
			}
		}
		
		return players;
	}
	
	public void outline(Player player)
	{
		Material material = Material.SEA_LANTERN;
		int y = player.getLocation().getBlockY();
		
		Chunk chunk = centerChunk.toChunk();
		Chunk tl = chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ() + 1);
		Chunk tr = chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ() + 1);
		Chunk tt = chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ() + 1);
		Chunk ll = chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ());
		Chunk rr = chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ());
		Chunk bl = chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ() - 1);
		Chunk br = chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ() - 1);
		Chunk bb = chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ() - 1);
		
		for(int i = 0; i < 16; i ++)
		{
			w.addJob(player, tl.getBlock(i, y, 15).getLocation(), material);
			w.addJob(player, tt.getBlock(i, y, 15).getLocation(), material);
			w.addJob(player, tr.getBlock(i, y, 15).getLocation(), material);
			
			w.addJob(player, bl.getBlock(i, y, 0).getLocation(), material);
			w.addJob(player, bb.getBlock(i, y, 0).getLocation(), material);
			w.addJob(player, br.getBlock(i, y, 0).getLocation(), material);
			
			w.addJob(player, tl.getBlock(0, y, i).getLocation(), material);
			w.addJob(player, ll.getBlock(0, y, i).getLocation(), material);
			w.addJob(player, bl.getBlock(0, y, i).getLocation(), material);
			
			w.addJob(player, tr.getBlock(15, y, i).getLocation(), material);
			w.addJob(player, rr.getBlock(15, y, i).getLocation(), material);
			w.addJob(player, br.getBlock(15, y, i).getLocation(), material);
		}
	}
	
	public void setFaction(Faction faction)
	{
		this.faction = faction;
		
		DyeColor dye = faction.getDyeColor();
		
		for(ULocation j : accents)
		{
			w.addJob(j.toLocation(), dye);
		}
		
		for(CapturePoint i : capturePoints)
		{
			setFactionCapture(i, faction);
		}
	}
	
	public void setFactionCapture(CapturePoint cap, Faction faction)
	{
		Block b = cap.getBlock().toLocation().getBlock();
		DyeColor dye = faction.getDyeColor();
		
		if(b.getType().equals(Material.BEACON))
		{
			w.addJob(b.getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS);			
			w.addJob(b.getRelative(BlockFace.UP).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation(), dye);
			w.addJob(b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation(), dye);
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
							
							w.addJob(block.getRelative(BlockFace.DOWN).getLocation(), Material.IRON_BLOCK);
							w.addJob(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getLocation(), Material.IRON_BLOCK);
							w.addJob(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), Material.IRON_BLOCK);
							w.addJob(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), Material.IRON_BLOCK);
							w.addJob(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), Material.IRON_BLOCK);
							w.addJob(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), Material.IRON_BLOCK);
							w.addJob(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getLocation(), Material.IRON_BLOCK);
							w.addJob(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).getLocation(), Material.IRON_BLOCK);
							w.addJob(block.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).getLocation(), Material.IRON_BLOCK);
							w.addJob(block.getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS);			
							w.addJob(block.getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), Material.STAINED_CLAY);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_CLAY);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_CLAY);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_CLAY);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_CLAY);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), Material.STAINED_CLAY);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_CLAY);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_CLAY);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), Material.STAINED_CLAY);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).getLocation(), Material.STAINED_GLASS_PANE);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS);
							w.addJob(block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getLocation(), Material.STAINED_GLASS);
						}
					}
				}
			}
		}
		
		setFaction(faction);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public UChunk getCenterChunk()
	{
		return centerChunk;
	}

	public void setCenterChunk(UChunk centerChunk)
	{
		this.centerChunk = centerChunk;
	}

	public UList<UChunk> getChunks()
	{
		return chunks;
	}

	public void setChunks(UList<UChunk> chunks)
	{
		this.chunks = chunks;
	}

	public UList<CapturePoint> getCapturePoints()
	{
		return capturePoints;
	}

	public void setCapturePoints(UList<CapturePoint> capturePoints)
	{
		this.capturePoints = capturePoints;
	}

	public UList<ULocation> getAccents()
	{
		return accents;
	}

	public void setAccents(UList<ULocation> accents)
	{
		this.accents = accents;
	}

	public GlacialRush getPl()
	{
		return pl;
	}

	public void setPl(GlacialRush pl)
	{
		this.pl = pl;
	}

	public WorldComponent getW()
	{
		return w;
	}

	public void setW(WorldComponent w)
	{
		this.w = w;
	}

	public ULocation getSpawn()
	{
		return spawn;
	}

	public void setSpawn(ULocation spawn)
	{
		this.spawn = spawn;
	}
}
