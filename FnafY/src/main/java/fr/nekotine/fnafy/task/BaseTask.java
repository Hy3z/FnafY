package fr.nekotine.fnafy.task;

import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import fr.nekotine.fnafy.task.tasks.Task_unclogToilet;

public abstract class BaseTask implements ConfigurationSerializable{
	
	public static void registerSerialisables(){
		ConfigurationSerialization.registerClass(Task_unclogToilet.class, "Task_unclogToilet");
	};
	
	private TaskManager taskManager;
	private boolean isAsked=false;
	
	public enum Difficulty{
		COMMON((byte)1,ChatColor.GREEN),
		RARE((byte)2,ChatColor.AQUA),
		EPIC((byte)3,ChatColor.DARK_PURPLE),
		LEGENDARY((byte)4,ChatColor.GOLD);
		private byte displayPower;
		private ChatColor displayColor;
		private Difficulty(byte pow,ChatColor c) {
			displayPower=pow;
			displayColor=c;
		}
		public byte getDisplayPower() {
			return displayPower;
		}
		public ChatColor getDisplayColor() {
			return displayColor;
		}
	}
	
	public void setTaskManager(TaskManager mnger) {
		taskManager=mnger;
	}
	
	public abstract Difficulty getDifficulty();
	
	public void complete() {
		if (taskManager!=null) {
			taskManager.completeTask(this);
		}
	}
	
	public TaskManager getTaskManager() {
		return taskManager;
	}
	
	public abstract void setup();

	public boolean isAsked() {
		return isAsked;
	}

	public void setAsked(boolean isAsked) {
		this.isAsked = isAsked;
	}
	
	public abstract void reset();
	
	public abstract String getDisplayName();
	
}
