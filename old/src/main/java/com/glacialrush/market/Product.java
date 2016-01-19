package com.glacialrush.market;

import org.bukkit.Material;
import com.glacialrush.component.MarketController;

public class Product
{
	private String name;
	private String id;
	private Material material;
	private Integer cost;
	private Boolean standard;
	private ProductType productType;
	
	public Product(String name, MarketController controller)
	{
		this.name = name;
		this.id = controller.newID();
		this.material = Material.LAPIS_ORE;
		this.cost = -1;
		this.standard = false;
		controller.getProducts().add(this);
	}
	
	public ProductType getProductType()
	{
		return productType;
	}
	
	public void setProductType(ProductType productType)
	{
		this.productType = productType;
	}
	
	public Boolean getStandard()
	{
		return standard;
	}
	
	public Boolean isStandard()
	{
		return standard;
	}
	
	public void setStandard(Boolean standard)
	{
		this.standard = standard;
	}
	
	public Integer getCost()
	{
		return cost;
	}
	
	public void setCost(Integer cost)
	{
		this.cost = cost;
	}
	
	public Material getMaterial()
	{
		return material;
	}
	
	public void setMaterial(Material material)
	{
		this.material = material;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
}
