package com.glacialrush.game;

public interface GameComponent
{
	void onTick(Game g);
	void onStart(Game g);
	void onStop(Game g);
}
