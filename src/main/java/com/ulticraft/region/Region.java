package com.ulticraft.region;

import org.bukkit.Chunk;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import com.ulticraft.data.Faction;
import com.ulticraft.uapi.UList;
import net.md_5.bungee.api.ChatColor;

public class Region
{
	protected UList<Chunk> chunks;
	protected UList<Block> accents;
	protected UList<Block> capturePoints;
	protected UList<GridLocation> corners;
	protected Faction faction;
	protected Chunk centerChunk;
	protected World world;
	
	public Region(Chunk chunk)
	{
		chunks = new UList<Chunk>();
		accents = new UList<Block>();
		capturePoints = new UList<Block>();
		corners = new UList<GridLocation>();
		centerChunk = chunk;
		world = chunk.getWorld();
		
		chunks.add(chunk);
		chunks.add(chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ()));
		chunks.add(chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ()));
		chunks.add(chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ() + 1));
		chunks.add(chunk.getWorld().getChunkAt(chunk.getX(), chunk.getZ() - 1));
		chunks.add(chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ() + 1));
		chunks.add(chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ() - 1));
		chunks.add(chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ() + 1));
		chunks.add(chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ() - 1));
		
		corners.add(new GridLocation(chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ() + 1).getBlock(15, 0, 15).getLocation()));
		corners.add(new GridLocation(chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ() - 1).getBlock(0, 0, 0).getLocation()));
		corners.add(new GridLocation(chunk.getWorld().getChunkAt(chunk.getX() + 1, chunk.getZ() - 1).getBlock(15, 0, 0).getLocation()));
		corners.add(new GridLocation(chunk.getWorld().getChunkAt(chunk.getX() - 1, chunk.getZ() + 1).getBlock(0, 0, 15).getLocation()));
		
		for(Chunk i : chunks)
		{
			int bx = i.getX() << 4;
			int bz = i.getZ() << 4;
			
			World world = i.getWorld();
			
			for(int xx = bx; xx < bx + 16; xx++)
			{
				for(int zz = bz; zz < bz + 16; zz++)
				{
					for(int yy = 0; yy < 128; yy++)
					{
						Block block = world.getBlockAt(xx, yy, zz);
						
						if(block.getType().equals(Material.WOOL) || block.getType().equals(Material.STAINED_GLASS) || block.getType().equals(Material.STAINED_GLASS_PANE) || block.getType().equals(Material.STAINED_CLAY) || block.getType().equals(Material.BANNER) || block.getType().equals(Material.STANDING_BANNER) || block.getType().equals(Material.WALL_BANNER))
						{
							accents.add(block);
						}
						
						if(block.getType().equals(Material.BEACON))
						{
							capturePoints.add(block);
							
							block.getRelative(BlockFace.DOWN).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.SOUTH).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.EAST).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.DOWN).getRelative(BlockFace.WEST).setType(Material.IRON_BLOCK);
							block.getRelative(BlockFace.UP).setType(Material.STAINED_GLASS);
							
							block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setType(Material.STAINED_CLAY);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).setType(Material.STAINED_CLAY);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).setType(Material.STAINED_CLAY);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).setType(Material.STAINED_CLAY);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).setType(Material.STAINED_CLAY);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setType(Material.STAINED_CLAY);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setType(Material.STAINED_CLAY);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setType(Material.STAINED_CLAY);
							
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setType(Material.STAINED_GLASS_PANE);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).setType(Material.STAINED_GLASS_PANE);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).setType(Material.STAINED_GLASS_PANE);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).setType(Material.STAINED_GLASS_PANE);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).setType(Material.STAINED_GLASS_PANE);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setType(Material.STAINED_GLASS_PANE);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setType(Material.STAINED_GLASS_PANE);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setType(Material.STAINED_GLASS_PANE);
							
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setType(Material.STAINED_GLASS_PANE);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).setType(Material.STAINED_GLASS_PANE);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).setType(Material.STAINED_GLASS_PANE);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).setType(Material.STAINED_GLASS_PANE);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).setType(Material.STAINED_GLASS_PANE);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setType(Material.STAINED_GLASS_PANE);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setType(Material.STAINED_GLASS_PANE);
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setType(Material.STAINED_GLASS_PANE);
							
							block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).setType(Material.STAINED_GLASS);
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void setFactionCapture(Block b, Faction f)
	{
		if(b.getType().equals(Material.BEACON))
		{
			b.getRelative(BlockFace.UP).setType(Material.STAINED_GLASS);
			b.getRelative(BlockFace.UP).setData(f.getDye().getData());
			
			b.getRelative(BlockFace.UP).setData(f.getDye().getData());
			
			b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setData(f.getDye().getData());
			
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setData(f.getDye().getData());
			
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.WEST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.NORTH).getRelative(BlockFace.EAST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.WEST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).getRelative(BlockFace.EAST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.EAST).setData(f.getDye().getData());
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.WEST).setData(f.getDye().getData());
			
			b.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).setData(f.getDye().getData());
			
			world.strikeLightningEffect(b.getLocation());
			world.strikeLightningEffect(b.getLocation());
			world.strikeLightningEffect(b.getLocation());
			world.strikeLightningEffect(b.getLocation());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void setFaction(Faction faction)
	{
		this.faction = faction;
		
		DyeColor c = faction.getDye();
		
		for(Block i : accents)
		{
			if(i.getType().equals(Material.BANNER) || i.getType().equals(Material.WALL_BANNER) || i.getType().equals(Material.STANDING_BANNER))
			{
				Banner banner = (Banner) i.getState();
				banner.setBaseColor(c);
				banner.update();
				
				continue;
			}
			
			i.setData(c.getData());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void drawOutline(Player p, Material material, int height)
	{
		for(GridLocation i : corners)
		{
			p.sendBlockChange(i.toLocation(world, height), material, (byte) 0);
		}
	}
	
	public boolean contains(Location location)
	{
		if(location.getWorld().equals(world) && chunks.contains(world.getChunkAt(location)))
		{
			return true;
		}
		
		else
		{
			return false;
		}
	}
	
	public void neutralize()
	{
		setFaction(new Faction("Neutral", ChatColor.WHITE));
	}
	
	public UList<Chunk> getChunks()
	{
		return chunks;
	}
	
	public void setChunks(UList<Chunk> chunks)
	{
		this.chunks = chunks;
	}
	
	public UList<Block> getAccents()
	{
		return accents;
	}
	
	public void setAccents(UList<Block> accents)
	{
		this.accents = accents;
	}
	
	public Faction getFaction()
	{
		return faction;
	}
	
	public UList<Block> getCapturePoints()
	{
		return capturePoints;
	}
	
	public void setCapturePoints(UList<Block> capturePoints)
	{
		this.capturePoints = capturePoints;
	}

	public Chunk getCenterChunk()
	{
		return centerChunk;
	}

	public World getWorld()
	{
		return world;
	}
}
