package com.ulticraft.composite;

import com.ulticraft.uapi.UList;

public class Poly
{
	private UList<GridLocation> shape;
	
	public Poly(UList<GridLocation> shape)
	{
		this.shape = shape;
	}
	
	public GridLocation calculateCentroid()
	{
		double x = 0.;
		double y = 0.;
		int pointCount = shape.size();
		
		for(int i = 0; i < pointCount - 1; i++)
		{
			final GridLocation point = shape.get(i);
			x += point.getX();
			y += point.getY();
		}
		
		x = x / pointCount;
		y = y / pointCount;
		
		return new GridLocation((int)x, (int)y);
	}
	
	public boolean contains(GridLocation test)
	{
		int i;
		int j;
		
		GridLocation[] points = shape.toArray(new GridLocation[shape.size()]);
		
		boolean result = false;
		for(i = 0, j = points.length - 1; i < points.length; j = i++)
		{
			if((points[i].getY() > test.getY()) != (points[j].getY() > test.getY()) && (test.getX() < (points[j].getX() - points[i].getX()) * (test.getY() - points[i].getY()) / (points[j].getY() - points[i].getY()) + points[i].getX()))
			{
				result = !result;
			}
		}
		
		return result;
	}
	
	public double area()
	{
		double[] x = new double[shape.size()];
		double[] y = new double[shape.size()];
		double area = 0;
		int points = shape.size();
		int c = 0;
		int j = points - 1;
		
		for(GridLocation i : shape)
		{
			x[c] = i.getX();
			y[c] = i.getY();
			c++;
		}
		
		for(int i = 1; i < points; i++)
		{
			area += (x[j] + x[i]) + (y[j] - y[i]);
			j = i;
		}
		
		return area / 2;
	}
}
