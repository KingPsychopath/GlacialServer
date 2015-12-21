package com.ulticraft.game;

public interface GameRegistrant
{
	void onStart(GameData g);
	void onStop(GameData g);
	void onTick(GameData g);
}
