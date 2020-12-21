package fr.nekotine.fnafy.task.tasks;

import fr.nekotine.fnafy.task.BaseTask;
import fr.nekotine.fnafy.task.TaskManager;

public class Task_unclogToilet extends BaseTask {

	public Task_unclogToilet(TaskManager mnger) {
		super(mnger);
	}

	@Override
	public Difficulty getDifficulty() {
		return Difficulty.LEGENDARY;
	}

	@Override
	public void setup() {
	}
	
}
