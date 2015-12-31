package com.ulticraft.item;

public class Utility extends Obtainable
{
	private static final long serialVersionUID = 1L;
	
	protected Integer quantity;
	
	public Utility(String name, String description, String id, Integer cost)
	{
		super(name, description, id, cost);
		
		quantity = 0;
	}

	public Integer getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}
}
