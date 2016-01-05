package com.ulticraft.game;

public interface GameComponent
{
	void onTick(Game g);
	void onStart(Game g);
	void onStop(Game g);
}
