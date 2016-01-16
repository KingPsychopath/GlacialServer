package com.glacialrush.composite.data;

import java.io.Serializable;
import com.glacialrush.GlacialServer;
import com.glacialrush.api.object.GMap;
import com.glacialrush.composite.Faction;
import com.glacialrush.composite.WarpgateArray;

public class WarpgateArrayData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private GMap<Faction, WarpgateData> gates;
	
	public WarpgateArrayData(GMap<Faction, WarpgateData> gates)
	{
		this.gates = gates;
	}

	public GMap<Faction, WarpgateData> getGates()
	{
		return gates;
	}

	public void setGates(GMap<Faction, WarpgateData> gates)
	{
		this.gates = gates;
	}
	
	public WarpgateArray toWarpgateArray(GlacialServer pl)
	{
		WarpgateArray wa = new WarpgateArray(pl);
		
		for(Faction i : gates.keySet())
		{
			wa.getGates().put(i, gates.get(i).toWarpgate(pl, wa));
		}
		
		return wa;
	}
}
