package com.glacialrush.game;

public interface GameHandler
{
	void start();
	void begin();
	void finish();
	void stop();
	void tick();
	void setCycles(long cycles);
	void setCycleTime(long cycleTime);
	long getCycles();
	long getCycleTime();
}
