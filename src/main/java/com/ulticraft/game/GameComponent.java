package com.ulticraft.game;

public interface GameComponent
{
	void onTick(GameState state);
	void onStart(GameState state);
	void onStop(GameState state);
}
