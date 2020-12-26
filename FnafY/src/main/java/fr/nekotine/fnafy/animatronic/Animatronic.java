package fr.nekotine.fnafy.animatronic;

import org.bukkit.Material;

public enum Animatronic {
	BONNIE(Material.BLUE_WOOL),
	FREDDY(Material.BROWN_WOOL),
	CHICA(Material.YELLOW_WOOL),
	FOXY(Material.RED_WOOL),
	MANGLE(Material.PINK_WOOL),
	SPRINGTRAP(Material.GREEN_WOOL);
	Animatronic(Material wool) {
		this.wool=wool;
	}
	private final Material wool;
	public Material getWool() {
		return wool;
	}
	public static Animatronic getFromWool(Material wool) {
		for(Animatronic a : values()) {
			if(a.wool==wool) {
				return a;
			}
		}
		return null;
	}
}
