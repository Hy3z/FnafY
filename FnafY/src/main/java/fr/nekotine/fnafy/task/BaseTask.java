package fr.nekotine.fnafy.task;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

public abstract class BaseTask implements ConfigurationSerializable{
	
	private TaskManager taskManager;
	private boolean isAsked=false;
	
	public enum Difficulty{
		COMMON,
		RARE,
		EPIC,
		LEGENDARY;
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
	
	public abstract void setup();

	public boolean isAsked() {
		return isAsked;
	}

	public void setAsked(boolean isAsked) {
		this.isAsked = isAsked;
	}
	
	public abstract void reset();
	
	public static void registerSerialisable(){};
	
}
