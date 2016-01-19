package com.glacialrush.component;

import java.util.Random;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.component.Controller;
import com.glacialrush.api.object.GList;
import com.glacialrush.market.Item;
import com.glacialrush.market.ItemType;
import com.glacialrush.market.Product;
import com.glacialrush.market.ProductType;
import com.glacialrush.market.Weapon;
import com.glacialrush.market.WeaponType;
import com.glacialrush.market.item.ItemColdBlade;
import com.glacialrush.market.item.ItemIceBlade;
import net.md_5.bungee.api.ChatColor;

public class MarketController extends Controller
{
	private GList<Product> products;
	private Random random;
	
	public ItemColdBlade itemColdBlade;
	public ItemIceBlade itemIceBlade;
			
	public MarketController(GlacialServer pl)
	{
		super(pl);
		
		products = new GList<Product>();
	}
	
	public void preEnable()
	{
		super.preEnable();
		
		products = new GList<Product>();
		random = new Random();
		random.setSeed(1337);
		
		itemColdBlade = new ItemColdBlade(this);
		itemIceBlade = new ItemIceBlade(this);
	}
	
	public Product get(String id)
	{
		for(Product i : products)
		{
			if(i.getId().equals(id))
			{
				return i;
			}
		}
		
		return null;
	}
	
	public GList<Product> search(String query)
	{
		GList<Product> result = new GList<Product>();
		
		for(Product i : products)
		{
			if(i.getName().toLowerCase().contains(query.toLowerCase()))
			{
				result.add(i);
			}
		}
		
		return result;
	}
	
	public GList<Product> filter(ProductType type)
	{
		GList<Product> result = new GList<Product>();
		
		for(Product i : products)
		{
			if(i.getProductType().equals(type))
			{
				result.add(i);
			}
		}
		
		return result;
	}
	
	public GList<Item> filter(ItemType type)
	{
		GList<Item> result = new GList<Item>();
		
		for(Product i : filter(ProductType.ITEM))
		{
			if(((Item)i).getItemType().equals(type))
			{
				result.add((Item)i);
			}
		}
		
		return result;
	}
	
	public GList<Weapon> filter(WeaponType type)
	{
		GList<Weapon> result = new GList<Weapon>();
		
		for(Product i : filter(ItemType.WEAPON))
		{
			if(((Weapon)i).getWeaponType().equals(type))
			{
				result.add((Weapon)i);
			}
		}
		
		return result;
	}
	
	public void postEnable()
	{
		super.postEnable();
				
		for(Product i : products)
		{
			o("Product Registered: " + i.getName() + " :: " + ChatColor.GREEN + i.getId());
		}
	}
	
	public void preDisable()
	{
		super.preDisable();
	}
	
	public void postDisable()
	{
		super.postDisable();
	}
	
	public String newID()
	{
		StringBuffer buffer = new StringBuffer();
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		
		int charactersLength = characters.length();
		
		for(int i = 0; i < 64; i++)
		{
			double index = random.nextDouble() * charactersLength;
			buffer.append(characters.charAt((int) index));
		}
		
		return buffer.toString();
	}

	public GList<Product> getProducts()
	{
		return products;
	}

	public void setProducts(GList<Product> products)
	{
		this.products = products;
	}
}
