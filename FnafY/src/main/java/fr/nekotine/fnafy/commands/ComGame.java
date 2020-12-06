package fr.nekotine.fnafy.commands;

import fr.nekotine.fnafy.FnafYMain;

public class ComGame {
	FnafYMain main;
	public ComGame(FnafYMain _main) {
		main=_main;
	}
	public void registerGameCommands() {
		main.getLogger().info("Registering Game commands");
		main.getLogger().info("Game Commands registered");
	}
}
