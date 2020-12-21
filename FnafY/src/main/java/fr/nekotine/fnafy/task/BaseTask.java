package fr.nekotine.fnafy.task;

public abstract class BaseTask {
	
	private final TaskManager taskManager;
	
	public BaseTask(TaskManager mnger) {
		taskManager=mnger;
	}
	
	public enum Difficulty{
		COMMON,
		RARE,
		EPIC,
		LEGENDARY;
	}
	
	public abstract Difficulty getDifficulty();
	
	public void complete() {
		taskManager.completeTask(this);
	}
	
	public abstract void setup();

	public TaskManager getTaskManager() {
		return taskManager;
	}
	
}
