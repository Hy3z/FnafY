package fr.nekotine.fnafy.task.tasks;

import java.util.Map;

import fr.nekotine.fnafy.task.BaseTask;

public class Task_unclogToilet extends BaseTask {

	@Override
	public Difficulty getDifficulty() {
		return Difficulty.LEGENDARY;
	}

	@Override
	public void setup() {
	}

	@Override
	public void reset() {
	}

	@Override
	public Map<String, Object> serialize() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return "Unclog the toilets";
	}
	
	public static Task_unclogToilet deserialize(Map<String, Object> args) {
		return new Task_unclogToilet();
	}
	
}
