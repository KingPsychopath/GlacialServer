package com.ulticraft.game;

public interface GameRegistrant
{
	void onStart(Game m, GameData g);
	void onStop(Game m, GameData g);
	void onTick(Game m, GameData g);
}
