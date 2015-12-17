package com.ulticraft.exception;

public class InvalidRegionPlacementException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	public InvalidRegionPlacementException()
	{
		super();
	}
	
	public InvalidRegionPlacementException(String message)
	{
		super(message);
	}
	
	public InvalidRegionPlacementException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public InvalidRegionPlacementException(Throwable cause)
	{
		super(cause);
	}
}
